package com.hychen11.order.feign;

import com.hychen11.order.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: MemberFeignService
 * @date ：2025/8/12 22:23
 */
@FeignClient("member")
public interface MemberFeignService {
    @GetMapping("/member/memberreceiveaddress/{memberId}/address")
    List<MemberAddressVo> getAddress(@PathVariable("memberId") Long memberId);
}
