package com.fh.shop.api.goods.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class SkuImage implements Serializable {

    private Long id;

    private String image;

    private Long spuId;

    private Long colorId;

}
