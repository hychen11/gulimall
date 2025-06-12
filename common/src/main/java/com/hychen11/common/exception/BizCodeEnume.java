package com.hychen11.common.exception;

public enum BizCodeEnume {
    UNKNOW_EXCEPTION(10000,"unknow exception"),
    VALID_EXCEPTION(10001,"pattern exception");

    private int code;
    private String msg;

    BizCodeEnume (int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
