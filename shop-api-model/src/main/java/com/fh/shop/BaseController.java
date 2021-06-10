package com.fh.shop;

import com.alibaba.fastjson.JSON;
import com.fh.shop.common.MemberVo;
import com.fh.shop.common.Secret;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class BaseController {

    public MemberVo bulideMemberVo(HttpServletRequest request){
        try {
            String decode = URLDecoder.decode(request.getHeader(Secret.getMember), "utf-8");
            MemberVo memberVo = JSON.parseObject(decode, MemberVo.class);
            return memberVo;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new  RuntimeException(e);
        }
    }

}
