package com.fh.shop.api.goods.vo;

import com.fh.shop.api.goods.po.Spu;
import com.fh.shop.goods.po.Sku;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SpuQueryVo {

    private Spu spu = new Spu();

    private List<Sku> skuList = new ArrayList<>();

}
