package com.fh.shop.api.goods.param;

import com.fh.shop.api.goods.po.Spu;
import lombok.Data;

import java.io.Serializable;

@Data
public class SpuParam implements Serializable {

    private Spu spu = new Spu();

    private String prices;

    private String stocks;

    private String specInfos;

    private String skuImages;

}
