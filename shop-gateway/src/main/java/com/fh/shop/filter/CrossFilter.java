package com.fh.shop.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class CrossFilter extends ZuulFilter {




    //pre所有的客户端请求在访问真正的微服务前执行
    //route
    //post：在 访问微服务之后执行
    //error
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    //起到一个顺序的作用 在同一种请求中 数值越低 执行顺序越靠前
    @Override
    public int filterOrder() {
        return 0;
    }

    //过滤器是否生效 true=开启 false=关闭
    @Override
    public boolean shouldFilter() {
        return true;
    }

    //执行核心业务逻辑
    //永远要返回null
    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletResponse response = currentContext.getResponse();
        //处理预请求跨域问题
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
        //处理自定义的请求头
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"x-auth,content-type,x-token");
        //处理特殊请求
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,"DELETE,POST,GET,PUT");
        HttpServletRequest request = currentContext.getRequest();
        String requestMethod = request.getMethod();
        if (requestMethod.equalsIgnoreCase("options")){
            currentContext.setSendZuulResponse(false);
        }
        return null;
    }
}
