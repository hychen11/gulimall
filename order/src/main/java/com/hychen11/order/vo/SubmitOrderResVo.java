package com.hychen11.order.vo;

import com.hychen11.order.entity.OrderEntity;
import lombok.Data;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: SubmitOrderResVo
 * @date ：2025/8/14 19:00
 */
@Data
public class SubmitOrderResVo {
    private OrderEntity orderEntity;
    private Integer code; //错误状态码 0成功，1代表删除token成功
}
