package com.fh.shop.member.controller;

import com.alibaba.fastjson.JSON;
import com.fh.shop.common.KeyUtil;
import com.fh.shop.common.Secret;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.member.biz.MemberService;
import com.fh.shop.member.vo.MemberVo;
import com.fh.shop.util.RedisUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
@RequestMapping("/api")
public class MemberController {

    @Resource
    private MemberService memberService;
    @Autowired
    private HttpServletRequest request;

    @GetMapping(value = "/member/loginOut")
    public ServerResponse loginOut() throws UnsupportedEncodingException {
        //MemberVo memberVo= (MemberVo) request.getAttribute(Secret.getMember);
        String header = URLDecoder.decode(request.getHeader(Secret.getMember), "utf-8");
        MemberVo memberVo = JSON.parseObject(header, MemberVo.class);
        Long id = memberVo.getId();
        RedisUtils.delete(KeyUtil.buildMemberKey(id));
        return ServerResponse.success();
    }


    //登录
    @PostMapping("/member/login")
    public ServerResponse login(String memberName, String passWord){
        return memberService.login(memberName,passWord);
    }


    @GetMapping(value = "/member/findMember")
    public ServerResponse findMember() throws UnsupportedEncodingException {
        //MemberVo memberVo= (MemberVo) request.getAttribute(Secret.getMember);
        String header = URLDecoder.decode(request.getHeader(Secret.getMember), "utf-8");
        MemberVo memberVo = JSON.parseObject(header, MemberVo.class);
        return ServerResponse.success(memberVo);
    }



}
