package com.fh.shop.cart.biz;

import com.alibaba.fastjson.JSON;
import com.fh.shop.cart.vo.CartIteamVo;
import com.fh.shop.cart.vo.CartVo;
import com.fh.shop.common.KeyUtil;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.Secret;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.goods.GoodsFeigService;
import com.fh.shop.goods.po.Sku;
import com.fh.shop.util.BigdecimalUtil;
import com.fh.shop.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service("cartService")
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private GoodsFeigService goodsFeigService;

    @Override
    public ServerResponse addCart(Long memberId, Long skuId, Long count) {
        //判断商品是否存在
        ServerResponse<Sku> sku1 = goodsFeigService.findSku(skuId);
        log.info("====={}",sku1);
        Sku sku = sku1.getData();

        if (sku==null){
            return ServerResponse.error(ResponseEnum.CART_SKU_NONO);
        }
        //商品是否上架
        if (sku.getDownJia().equals("0")){
            return ServerResponse.error(ResponseEnum.CART_SKU_NOJIA);
        }
        //商品内存是否足够
        if (sku.getStock()<count){
            return ServerResponse.error(ResponseEnum.CART_SKU_STICK_NONO);
        }
        //判断购物车是否存在
        //String memberCart = RedisUtils.get(KeyUtil.buildCartKey(memberId));
        String memberCart = RedisUtils.hget(KeyUtil.buildCartKey(memberId), Secret.CART_JSON);
        if (StringUtils.isEmpty(memberCart)){
            String price = sku.getPrice().toString();
            //不存在的话创建购物车
            CartIteamVo cartIteamVo = new CartIteamVo();
            cartIteamVo.setCount(count);
            cartIteamVo.setPrice(price);
            cartIteamVo.setSkuId(sku.getId());
            cartIteamVo.setSkuImage(sku.getImage());
            cartIteamVo.setSkuName(sku.getSkuName());
            BigDecimal countPrice = BigdecimalUtil.mul(price, count + "");
            cartIteamVo.setCountPrice(countPrice.toString());
            CartVo cartVo = new CartVo();
            cartVo.getIteamVoList().add(cartIteamVo);
            cartVo.setTotalCount(count);
            cartVo.setTotalPrice(cartIteamVo.getCountPrice());
            String cartStr = JSON.toJSONString(cartVo);
            //RedisUtils.set(KeyUtil.buildCartKey(memberId),cartStr);
            RedisUtils.hset(KeyUtil.buildCartKey(memberId),Secret.CART_JSON,cartStr);
            RedisUtils.hset(KeyUtil.buildCartKey(memberId),Secret.CART_COUNT,cartVo.getTotalCount()+"");
        }else {
            //存在的话判断购物车中是否有该商品
            CartVo cartVo = JSON.parseObject(memberCart, CartVo.class);
            List<CartIteamVo> iteamVoList = cartVo.getIteamVoList();
            /*if (iteamVoList.size()<=0){
                return ServerResponse.error(ResponseEnum.CART_SKU_MEMBER_NONO);
            }*/
            Optional<CartIteamVo> iteamVo = iteamVoList.stream().filter(x -> x.getSkuId().longValue() == skuId).findFirst();
            if (iteamVo.isPresent()){
                //购物车中有这款商品，找到这款商品，更新商品的数量小计
                CartIteamVo cartIteamVo = iteamVo.get();
                cartIteamVo.setCount(cartIteamVo.getCount()+count);
                if (cartIteamVo.getCount()>10){
                    return ServerResponse.success();
                }

                if (cartIteamVo.getCount()<=0){
                    iteamVoList.removeIf(x ->x.getSkuId().longValue()==cartIteamVo.getSkuId().longValue());

                    List<CartIteamVo> iteamVoList1 = cartVo.getIteamVoList();
                    long totalCount = 0;
                    BigDecimal totalPrice = new BigDecimal(0);
                    for (CartIteamVo vo : iteamVoList1) {
                        totalCount += vo.getCount();
                        totalPrice=totalPrice.add(new BigDecimal(vo.getCountPrice()));
                    }
                    cartVo.setTotalCount(totalCount);
                    cartVo.setTotalPrice(totalPrice.toString());
                    String cartStr = JSON.toJSONString(cartVo);
                    //RedisUtils.set(KeyUtil.buildCartKey(memberId),cartStr);
                    RedisUtils.hset(KeyUtil.buildCartKey(memberId),Secret.CART_JSON,cartStr);
                    RedisUtils.hset(KeyUtil.buildCartKey(memberId),Secret.CART_COUNT,cartVo.getTotalCount()+"");
                    return ServerResponse.success();
                }


                BigDecimal countPrice = new BigDecimal(cartIteamVo.getCountPrice());
                String countPriceStr = countPrice.add(BigdecimalUtil.mul(cartIteamVo.getPrice(), count+ "")).toString();
                cartIteamVo.setCountPrice(countPriceStr);

                List<CartIteamVo> iteamVoList1 = cartVo.getIteamVoList();
                long totalCount = 0;
                BigDecimal totalPrice = new BigDecimal(0);
                for (CartIteamVo vo : iteamVoList1) {
                    totalCount += vo.getCount();
                    totalPrice=totalPrice.add(new BigDecimal(vo.getCountPrice()));
                }
                cartVo.setTotalCount(totalCount);
                cartVo.setTotalPrice(totalPrice.toString());
                String cartStr = JSON.toJSONString(cartVo);
                //RedisUtils.set(KeyUtil.buildCartKey(memberId),cartStr);
                RedisUtils.hset(KeyUtil.buildCartKey(memberId),Secret.CART_JSON,cartStr);
                RedisUtils.hset(KeyUtil.buildCartKey(memberId),Secret.CART_COUNT,cartVo.getTotalCount()+"");
            }else {
                //购物车中没有这款商品，直接将商品加入购物车
                if (count<=0){
                    return ServerResponse.error(ResponseEnum.CART_SKU_MEMBER_WGCA);
                }
                CartIteamVo cartIteamVo = new CartIteamVo();
                cartIteamVo.setCount(count);
                String price = sku.getPrice().toString();
                cartIteamVo.setPrice(price);
                cartIteamVo.setSkuId(sku.getId());
                cartIteamVo.setSkuImage(sku.getImage());
                cartIteamVo.setSkuName(sku.getSkuName());
                BigDecimal countPrice = BigdecimalUtil.mul(price, count + "");
                cartIteamVo.setCountPrice(countPrice.toString());
                cartVo.getIteamVoList().add(cartIteamVo);

                long totalCount = 0;
                BigDecimal totalPrice = new BigDecimal(0);
                List<CartIteamVo> iteamVoList1 = cartVo.getIteamVoList();
                for (CartIteamVo vo : iteamVoList1) {
                    totalCount += vo.getCount();
                    totalPrice=totalPrice.add(new BigDecimal(vo.getCountPrice()));
                }
                cartVo.setTotalCount(totalCount);
                cartVo.setTotalPrice(totalPrice.toString());
                String cartStr = JSON.toJSONString(cartVo);
                //RedisUtils.set(KeyUtil.buildCartKey(memberId),cartStr);
                RedisUtils.hset(KeyUtil.buildCartKey(memberId),Secret.CART_JSON,cartStr);
                RedisUtils.hset(KeyUtil.buildCartKey(memberId),Secret.CART_COUNT,cartVo.getTotalCount()+"");
            }

        }

        return ServerResponse.success();
    }

    @Override
    public ServerResponse queryCart(Long memberId) {
        //String cartList = RedisUtils.get(KeyUtil.buildCartKey(memberId));
        String cartList = RedisUtils.hget(KeyUtil.buildCartKey(memberId), Secret.CART_JSON);
        CartVo cartVo = JSON.parseObject(cartList, CartVo.class);
        return ServerResponse.success(cartVo);
    }
}
