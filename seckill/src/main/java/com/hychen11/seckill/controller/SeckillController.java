package com.hychen11.seckill.controller;

import com.hychen11.common.utils.R;
import com.hychen11.seckill.service.SecKillService;
import com.hychen11.seckill.to.SeckillSkuRedisTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: SeckillController
 * @date ：2025/8/23 23:07
 */
@Controller
public class SeckillController {
    @Resource
    private SecKillService secKillService;

    /**
     * 返回当前时间参与的秒杀商品信息
     *
     * @return
     */
    @ResponseBody
    @GetMapping("/currentSeckillSkus")
    public R getCurrentTimeSeckillSkus() {
        List<SeckillSkuRedisTo> skus = secKillService.getCurrentTimeSeckillSkus();
        return R.ok().setData(skus);
    }

    /**
     * 查询商品秒杀信息
     *
     * @param skuId
     * @return
     */
    @ResponseBody
    @GetMapping("/sku/seckill/{skuId}")
    public R getSkuSeckillInfo(@PathVariable Long skuId) {
        SeckillSkuRedisTo to = secKillService.getSkuSeckillInfo(skuId);
        return R.ok().setData(to);
    }

    /**
     * 秒杀抢购功能
     *
     * @param killId
     * @param key
     * @param num
     * @return
     */
    @GetMapping("/kill")
    public String seckill(@RequestParam("killId") String killId,
                          @RequestParam("key") String key,
                          @RequestParam("num") Integer num,
                          Model model) {
        String orderSn = secKillService.kill(killId, key, num);
        model.addAttribute("orderSn", orderSn);
        return "success";
    }
}
