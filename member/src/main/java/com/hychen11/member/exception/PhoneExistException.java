package com.hychen11.member.exception;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: PhoneExistException
 * @date ：2025/8/2 10:52
 */
public class PhoneExistException extends RuntimeException{
    public PhoneExistException() {
        super("手机号已经存在");
    }
}
