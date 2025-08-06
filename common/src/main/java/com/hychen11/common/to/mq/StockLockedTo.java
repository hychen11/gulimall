package com.hychen11.common.to.mq;

import lombok.Data;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: StockLockedTo
 * @date ：2025/8/6 22:52
 */
@Data
public class StockLockedTo {
    private Long taskId; //库存工作单id
    private StockDetailTo taskDetail; //库存工作单详情id
}
