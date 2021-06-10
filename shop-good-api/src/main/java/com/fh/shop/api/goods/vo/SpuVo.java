package com.fh.shop.api.goods.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SpuVo implements Serializable {


    private List<SpuAttrVo> spuAttrVoList = new ArrayList<>();

    private List<SpuSpecVo> spuSpecVoList = new ArrayList<>();

}
