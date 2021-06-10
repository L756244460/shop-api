package com.fh.shop.api.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.api.goods.po.Spu;
import com.fh.shop.goods.po.Sku;

import java.util.List;


public interface ISpuMapper extends BaseMapper<Spu> {

    List<Sku> findStatusList();
}
