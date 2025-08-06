package com.hychen11.common.to.mq;

import lombok.Data;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: StockDetailTo
 * @date ：2025/8/6 22:53
 */
@Data
public class StockDetailTo {
    /**
     * id
     */
    private Long id;
    /**
     * sku_id
     */
    private Long skuId;
    /**
     * sku_name
     */
    private String skuName;
    /**
     * 购买个数
     */
    private Integer skuNum;
    /**
     * 工作单id
     */
    private Long taskId;
    /**
     * 仓库id
     */
    private Long wareId;
    /**
     * 1-已锁定  2-已解锁  3-扣减
     */
    private Integer lockStatus;
}
