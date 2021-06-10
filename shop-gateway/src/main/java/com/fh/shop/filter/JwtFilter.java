package com.fh.shop.filter;

import com.alibaba.fastjson.JSON;
import com.fh.shop.common.*;
import com.fh.shop.util.Md5Util;
import com.fh.shop.util.RedisUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;

@Component
public class JwtFilter extends ZuulFilter {

    @Value("${fh.shop.checkUrls}")
    private List<String> checkUrls;


    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @SneakyThrows
    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        //获取的是我们得路径
        String requestURI = request.getRequestURI();
        boolean isCheck=false;
        for (String checkUrl : checkUrls) {
            if (requestURI.indexOf(checkUrl)>0){
                //白名单
                isCheck=true;
                break;
            }
        }
        if (!isCheck){
            //默认相当于放行
            return null;
        }

        //需要验证的
        //验证 x-auth:x,y
        //判断是否有头信息
        String header = request.getHeader("x-auth");
        //先判断客户端传的请求头是否存在 不存在的话 直接拦截 如何给前端返回提示信息
        if (StringUtils.isEmpty(header)){
            //return false;要给前端相应信息
            return bulidResponse(ResponseEnum.MEMBER_LOGIN_HEADER_IS_NONE);
        }
        //判断我们头信息是否完全
        //分割获取我们64位进制的vojson和签名
        String[] heraderArr = header.split("\\.");
        //当其长度不为二时
        if (heraderArr.length!=2){
            return bulidResponse(ResponseEnum.MEMBER_LOGIN_HEADER_IS_ERROR);
            //throw new ShopException(ResponseEnum.MEMBER_LOGIN_HEADER_IS_ERROR);
        }
        //进行验证
        //64进制格式的信息
        String memberVoJsonBase64 = heraderArr[0];
        String signBase64 = heraderArr[1];
        //常规样式
        String memberVoJson = new String(Base64.getDecoder().decode(memberVoJsonBase64));
        String signBase = new String(Base64.getDecoder().decode(signBase64));

        //比较
        String newSign = Md5Util.eign(memberVoJson, Secret.getSecret);
        if (!newSign.equals(signBase)){
            return bulidResponse(ResponseEnum.MEMBER_LOGIN_IS_ERROR);
            //throw new ShopException(ResponseEnum.MEMBER_LOGIN_IS_ERROR);
        }

        MemberVo memberVo = JSON.parseObject(memberVoJson, MemberVo.class);
        Long id = memberVo.getId();

        if (!RedisUtils.exist(KeyUtil.buildMemberKey(id))){
            return bulidResponse(ResponseEnum.MEMBER_LOGIN_TIME_OVER);
            //throw new ShopException(ResponseEnum.MEMBER_LOGIN_TIME_OVER);
        }

        RedisUtils.expire(KeyUtil.buildMemberKey(id),30*60);


        //将信息存入我们的request中
        //request.setAttribute(Secret.getMember,memberVo);
        currentContext.addZuulRequestHeader(Secret.getMember,URLEncoder.encode(memberVoJson,"utf-8"));
        return null;
    }

    private Object bulidResponse(ResponseEnum responseEnum) {
        RequestContext currentContext=RequestContext.getCurrentContext();
        HttpServletResponse response = currentContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        currentContext.setSendZuulResponse(false); //拦截过后，不会再进行路由转发
        ServerResponse error = ServerResponse.error(responseEnum);
        //转为json格式字符串
        String res = JSON.toJSONString(error);
        currentContext.setResponseBody(res);
        //throw new ShopException(ResponseEnum.MEMBER_LOGIN_HEADER_IS_NONE);
        return null;
    }
}
