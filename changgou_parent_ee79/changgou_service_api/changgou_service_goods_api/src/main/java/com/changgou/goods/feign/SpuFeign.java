package com.changgou.goods.feign;

import com.changgou.goods.pojo.Spu;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



/**
 * @author Steven
 * @version 1.0
 * @description com.changgou.goods.feign
 * @date 2019-12-25
 */
//FeignClient(name:微服务的名称)

@RequestMapping("/spu")
@FeignClient(name = "goods")
public interface SpuFeign {
    /***
     * 根据ID查询Spu数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Spu> findById(@PathVariable Long id);
}
