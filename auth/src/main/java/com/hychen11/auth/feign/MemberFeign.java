package com.hychen11.auth.feign;

import com.hychen11.common.to.MemberRegisterTo;
import com.hychen11.common.to.MemberTo;
import com.hychen11.common.utils.R;
import com.hychen11.auth.vo.UserLoginVo;
import com.hychen11.auth.vo.WeiboUserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: MemberFeign
 * @date ：2025/8/2 13:46
 */
@FeignClient("member")
public interface MemberFeign {
    @PostMapping("/member/member/register")
    R register(@RequestBody MemberRegisterTo registerTo);

    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVo member);

    @PostMapping("/member/member/oauth2/login")
    MemberTo oauthLogin(@RequestBody WeiboUserVo weiboUser);
}
