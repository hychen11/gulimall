package com.hychen11.auth.vo;

import lombok.Data;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: WeiboUserVo
 * @date ：2025/8/2 13:49
 */
@Data
public class WeiboUserVo {
    private String access_token;
    private Long remind_in;
    private Long expires_in;
    private String uid;
}
