package com.hychen11.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: NoStockException
 * @date ：2025/8/6 22:54
 */
@Getter
@Setter
public class NoStockException extends RuntimeException {
    private Long skuId;

    public NoStockException(Long skuId) {
        super("商品id：" + skuId + "库存不足！");
    }

    public NoStockException(String msg) {
        super(msg);
    }

}

