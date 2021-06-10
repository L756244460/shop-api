package com.fh.shop.api.goods.controller;

import com.fh.shop.api.goods.biz.ISpuService;

import com.fh.shop.common.ServerResponse;
import com.fh.shop.goods.po.Sku;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
@Api(tags = "spu接口")
public class SpuController extends BaseController {

    @Resource(name = "spuService")
    private ISpuService spuService;



    @GetMapping(value = "/spus")
    //@Check
    public ServerResponse findStatusList(){

        return spuService.findStatusList();
    }

    @GetMapping(value = "/findSku")
    public ServerResponse<Sku> findSku(@RequestParam("id") Long id){
        return spuService.findSku(id);
    }



}
