package com.hychen11.common.exception;

import lombok.Getter;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: ErrorCodeEnum
 * @date ：2025/7/25 00:08
 */
@Getter
public enum ErrorCodeEnum {
    UNKNOW_EXCEPTION(10000,"系统未知异常"),
    VAILD_EXCEPTION(10001,"参数格式校验失败"),
    PRODUCT_UP_EXCEPTION(11000,"商品上架异常");
    private final Integer code;
    private final String message;
    ErrorCodeEnum(Integer code,String message){
        this.code = code;
        this.message = message;
    }


}
