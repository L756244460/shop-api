package com.fh.shop.cart.controller;

import com.fh.shop.BaseController;
import com.fh.shop.cart.biz.CartService;
import com.fh.shop.common.MemberVo;
import com.fh.shop.common.Secret;
import com.fh.shop.common.ServerResponse;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@Api(tags = "购物车接口")
public class CartController extends BaseController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private CartService cartService;

    @PostMapping(value = "/cart/addCart")
    public ServerResponse addCart(Long skuId, Long count){
        MemberVo memberVo = bulideMemberVo(request);
        Long memberId = memberVo.getId();
        return cartService.addCart(memberId,skuId,count);
    }

    @GetMapping(value = "/cart/queryCart")
    public ServerResponse queryCart(){
       /* MemberVo memberVo = (MemberVo) request.getAttribute(Secret.getMember);*/
        MemberVo memberVo = bulideMemberVo(request);
        Long memberId = memberVo.getId();
        return cartService.queryCart(memberId);
    }


}
