package com.hychen11.cart.to;

import lombok.Data;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: UserInfoTo
 * @date ：2025/7/31 21:58
 */
@Data
public class UserInfoTo {
    private Long userId;
    private String userKey;
    private boolean tempUser = false;
}
