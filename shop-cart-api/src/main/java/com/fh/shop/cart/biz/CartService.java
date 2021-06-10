package com.fh.shop.cart.biz;

import com.fh.shop.common.ServerResponse;

public interface CartService {

    ServerResponse addCart(Long memberId, Long skuId, Long count);

    ServerResponse queryCart(Long memberId);
}
