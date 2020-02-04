package com.changgou.order.controller;

import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import entity.StatusCode;
import entity.TokenDecode;
import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 购物车控制层
 */

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@RestController
@CrossOrigin
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping(value = "/add")
    public Result add(Integer num, Long id) {
        //用户名
        String username = TokenDecode.getUserInfo().get("username");

        //将商品加入购物车
        cartService.add(num, id, username);
        return new Result(true, StatusCode.OK, "加入购物车成功！");
    }

    /**
     * 查询用户购物车数据
     * @return
     */
    @GetMapping(value = "/list")
    public Result findCartList() {
        //用户名
        String username = TokenDecode.getUserInfo().get("username");
        List<OrderItem> orderItems = cartService.findCartList(username);
        return new Result(true, StatusCode.OK, "查询购物车列表成功!", orderItems);
    }

}
