package com.hychen11.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hychen11.common.to.MemberRegisterTo;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.member.entity.MemberEntity;
import com.hychen11.member.vo.MemberLoginVo;
import com.hychen11.member.vo.WeiboUserVo;

import java.util.Map;

/**
 * 会员
 *
 * @author hychen11
 * @email 
 * @date 2025-02-10 16:22:04
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    MemberEntity ouathLogin(WeiboUserVo weiboUser);

    void register(MemberRegisterTo registerTo);

    MemberEntity login(MemberLoginVo member);
}

