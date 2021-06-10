package com.fh.shop.goods;

import com.fh.shop.common.ServerResponse;
import com.fh.shop.goods.po.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "shop-good-api")
public interface GoodsFeigService {

    @GetMapping(value = "/api/findSku")
    public ServerResponse<Sku> findSku(@RequestParam("id") Long id);

}
