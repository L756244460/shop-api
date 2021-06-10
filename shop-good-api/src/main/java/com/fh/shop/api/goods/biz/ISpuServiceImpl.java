package com.fh.shop.api.goods.biz;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.goods.mapper.ISkuMapper;
import com.fh.shop.api.goods.mapper.ISpuMapper;
import com.fh.shop.api.goods.vo.SpuStatusVo;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.goods.po.Sku;
import com.fh.shop.util.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service("spuService")
public class ISpuServiceImpl implements ISpuService {



    @Autowired
    private ISpuMapper spuMapper;

    @Autowired
    private ISkuMapper skuMapper;




    @Override
    public ServerResponse findStatusList() {
        String skuList1 = RedisUtils.get("skuList");
        if (StringUtils.isNotEmpty(skuList1)){
            List<SpuStatusVo> spuStatusVos = JSON.parseArray(skuList1, SpuStatusVo.class);
            return ServerResponse.success(spuStatusVos);
        }
        List<Sku> skuList=spuMapper.findStatusList();
        List<SpuStatusVo> skuStatusList = skuList.stream().map(x -> {
            SpuStatusVo spuStatusVo = new SpuStatusVo();
            Long skuId = x.getId();
            BigDecimal spuPrice = x.getPrice();
            String skuName = x.getSkuName();
            String image = x.getImage();
            spuStatusVo.setId(skuId);
            spuStatusVo.setImage(image);
            //因为jackson默认不会给我们的价格进行特殊处理，所以我们将其转成string显示小数
            spuStatusVo.setPrice(spuPrice.toString());
            spuStatusVo.setSkuName(skuName);
            return spuStatusVo;
        }).collect(Collectors.toList());
        String spuList = JSON.toJSONString(skuStatusList);
        RedisUtils.setex("skuList",spuList,30);
        return ServerResponse.success(skuStatusList);
    }

    @Override
    public ServerResponse<Sku> findSku(Long id) {
        Sku sku = skuMapper.selectById(id);
        return ServerResponse.success(sku);
    }


    private Sku buildSku(Long spuId, String val, String s, String specInfo, String skuName, String colorId, String skuImage) {
        Sku sku = new Sku();
        sku.setPrice(new BigDecimal(val));
        sku.setStock(Integer.parseInt(s));
        sku.setSpecInfo(specInfo);
        sku.setSpuId(spuId);
        sku.setSkuName(skuName);
        sku.setImage(skuImage);
        sku.setColorId(Long.parseLong(colorId));
        return sku;
    }


}
