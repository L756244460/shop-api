package com.fh.shop.goods.po;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Sku implements Serializable {

    private Long id;

    private String skuName;

    private Long spuId;

    private BigDecimal price;

    private Integer stock;

    private String specInfo;

    private Long colorId;

    private String image;

    private String downJia;

    private String oldOrnew;

    private String place;

    private Integer sales;

}
