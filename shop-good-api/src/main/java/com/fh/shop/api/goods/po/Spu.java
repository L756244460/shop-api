package com.fh.shop.api.goods.po;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Spu implements Serializable {

    private Long id;

    private Long brandId;

    private Long cate1;

    private Long cate2;

    private Long cate3;

    private String attrInfo;

    private String specInfo;

    private Integer stock;

    private BigDecimal price;

    private String spuName;

    private String brandName;

    private String cateName;

    private String downJia;

    private String oldOrnew;

    private String place;

}
