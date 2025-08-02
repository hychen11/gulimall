package com.hychen11.member.exception;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: UserNameExistException
 * @date ：2025/8/2 10:53
 */
public class UserNameExistException extends RuntimeException{
    public UserNameExistException() {
        super("用户名已经存在");
    }
}
