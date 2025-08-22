package com.hychen11.ware.feign;

import com.hychen11.common.utils.R;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: MemberFeignService
 * @date ：2025/8/11 01:18
 */
@FeignClient("member")
public interface MemberFeignService {
    @RequestMapping("/member/memberreceiveaddress/info/{id}")
    R info(@PathVariable("id") Long id);
}
