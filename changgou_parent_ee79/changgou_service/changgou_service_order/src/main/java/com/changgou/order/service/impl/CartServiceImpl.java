package com.changgou.order.service.impl;

import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 购物车业务实现类
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SpuFeign spuFeign;


    /**
     * 添加购物车
     * @param num：购买商品数量
     * @param skuId：购买商品的shuId
     * @param username：购买的用户
     */
    @Override
    public void add(Integer num, Long skuId, String username) {

        if (num<0){
            //如果商品小于等于0,删除当前商品
            redisTemplate.boundHashOps("Cart_"+username).delete(skuId);
        }

        //查询SKU
        Result<Sku> skuResult = skuFeign.findById(skuId);

        if (skuResult != null && skuResult.isFlag()) {
            //获取SKU
            Sku sku = skuResult.getData();
            //获取SPU
            Spu spu = spuFeign.findById(sku.getSpuId()).getData();
            //将SKU转换成OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setSpuId(sku.getSpuId());
            orderItem.setSkuId(sku.getId());
            orderItem.setName(sku.getName());
            orderItem.setPrice(sku.getPrice());
            orderItem.setNum(num);
            orderItem.setMoney(num * orderItem.getPrice());//单价*数量
            orderItem.setPayMoney(num * orderItem.getPrice());//实付金额
            orderItem.setImage(sku.getImage());
            orderItem.setWeight(sku.getWeight() * num);//重量=单价*数量
            //分类ID设置
            orderItem.setCategoryId1(spu.getCategory1Id());
            orderItem.setCategoryId2(spu.getCategory2Id());
            orderItem.setCategoryId3(spu.getCategory3Id());

            /**
             * 购物车数据存入到Redis
             * namespace = Cart_[username]
             * key=skuId
             * value=OrderItem
             */
            redisTemplate.boundHashOps("Cart_" + username).put(skuId, orderItem);

        }

    }

    /**
     * 查询用户购物车数据
     * @param username
     * @return
     */
    @Override
    public List<OrderItem> findCartList(String username) {
        //查询所有购物车信息
        List<OrderItem> orderItems = redisTemplate.boundHashOps("Cart_" + username).values();
        return orderItems;
    }
}
