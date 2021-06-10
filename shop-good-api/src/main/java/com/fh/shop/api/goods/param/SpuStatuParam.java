package com.fh.shop.api.goods.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpuStatuParam implements Serializable {

    private Long spuId;

    private String status;

    private String flag;



}
