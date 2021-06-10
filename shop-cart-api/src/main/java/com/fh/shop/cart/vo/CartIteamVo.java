package com.fh.shop.cart.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CartIteamVo implements Serializable {

    private Long skuId;

    private String skuName;

    private String skuImage;

    private String price;

    private Long count;

    private String countPrice;


}
