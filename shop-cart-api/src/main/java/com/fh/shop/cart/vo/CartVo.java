package com.fh.shop.cart.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CartVo implements Serializable {

    private List<CartIteamVo> iteamVoList=new ArrayList<>();

    private Long totalCount;

    private String totalPrice;

}
