package com.hychen11.member.vo;

import lombok.Data;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: WeiboUserVo
 * @date ：2025/8/2 10:51
 */
@Data
public class WeiboUserVo {
    private String access_token;
    private Long remind_in;
    private Long expires_in;
    private String uid;
}
