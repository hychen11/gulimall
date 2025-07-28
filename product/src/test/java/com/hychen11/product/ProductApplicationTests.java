package com.hychen11.product;

import com.hychen11.product.dao.AttrDao;
import com.hychen11.product.dao.SkuSaleAttrValueDao;
import com.hychen11.product.service.CategoryService;
import com.hychen11.product.vo.SkuItemSaleAttrsVo;
import com.hychen11.product.vo.SpuItemAttrGroupVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Slf4j
class ProductApplicationTests {
    @Resource
    private CategoryService categoryService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private AttrDao attrDao;
    @Resource
    private SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Test
    public void testFindPath() {
        Long[] catelogPath = categoryService.findCatelogPath(226L);
        log.info("完整路径：{}", Arrays.asList(catelogPath));
    }

    @Test
    public void testRedisson() {
        System.out.println(redissonClient);
    }

    @Test
    public void testGetSaleAttrsBySpuId() {
        List<SkuItemSaleAttrsVo> saleAttrsBySpuId = skuSaleAttrValueDao.getSaleAttrsBySpuId(19L);
        System.out.println(saleAttrsBySpuId.toString());
    }

    @Test
    public void testGetAttrGroupWithAttrsBySpuId() {
        List<SpuItemAttrGroupVo> attrGroupWithAttrsBySpuId = attrDao.getAttrGroupWithAttrsBySpuId(19L, 225L);
        System.out.println(attrGroupWithAttrsBySpuId.toString());
    }

    @Test
    void contextLoads() {
    }

    @Autowired
    StringRedisTemplate redisTemplate;//引入这个实例就可以进行操作了

    @Test
    public void testStringRedisTemplate(){
        //首先使用Template获取我们想要操作数据的操作对象（比如redis的五大基本类型，下面的value就表示操作的简单字符串）
        ValueOperations<String, String> ops = redisTemplate.opsForValue();//ops=operations
        //保存
        ops.set("hello","world_"+ UUID.randomUUID().toString());
        //查询
        String hello = ops.get("hello");
        System.out.println("hello "+hello);
    }

}
