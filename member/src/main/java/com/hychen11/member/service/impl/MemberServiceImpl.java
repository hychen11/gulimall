package com.hychen11.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hychen11.common.to.MemberRegisterTo;
import com.hychen11.member.dao.MemberLevelDao;
import com.hychen11.member.entity.MemberLevelEntity;
import com.hychen11.member.exception.PhoneExistException;
import com.hychen11.member.exception.UserNameExistException;
import com.hychen11.member.vo.MemberLoginVo;
import com.hychen11.member.vo.WeiboUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hychen11.common.utils.PageUtils;
import com.hychen11.common.utils.Query;

import com.hychen11.member.dao.MemberDao;
import com.hychen11.member.entity.MemberEntity;
import com.hychen11.member.service.MemberService;

import javax.annotation.Resource;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private MemberLevelDao memberLevelDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public MemberEntity ouathLogin(WeiboUserVo weiboUser) {
        String uid = weiboUser.getUid();
        MemberEntity memberEntity = memberDao.selectOne(new LambdaQueryWrapper<MemberEntity>().eq(MemberEntity::getSocialUid, uid));
        if (Objects.nonNull(memberEntity)) {
            MemberEntity update = new MemberEntity();
            // 更新令牌和过期时间
            update.setAccessToken(weiboUser.getAccess_token());
            update.setExpiresIn(weiboUser.getExpires_in());
            update.setId(memberEntity.getId());

            memberDao.insert(update);
            memberEntity.setAccessToken(weiboUser.getAccess_token());
            memberEntity.setExpiresIn(weiboUser.getExpires_in());
            return memberEntity;
        } else {
            MemberEntity register = new MemberEntity();
            register.setSocialUid(weiboUser.getUid());
            register.setExpiresIn(weiboUser.getExpires_in());
            register.setAccessToken(weiboUser.getAccess_token());

            register.setUsername(UUID.randomUUID().toString().substring(0, 10));
            register.setNickname(UUID.randomUUID().toString().substring(0, 10));

            MemberLevelEntity level = memberLevelDao.getDefaultLevel();
            register.setLevelId(level.getId());
            memberDao.insert(register);
            return register;

        }

    }

    @Override
    public void register(MemberRegisterTo registerTo) {
        MemberEntity memberEntity = new MemberEntity();
        MemberLevelEntity level = memberLevelDao.getDefaultLevel();
        memberEntity.setLevelId(level.getId());
        //检查用户名和手机号唯一性
        chechUserNameUnique(registerTo.getUserName());
        memberEntity.setUsername(registerTo.getUserName());
        chechPhoneUnique(registerTo.getPhone());
        memberEntity.setMobile(registerTo.getPhone());
        memberEntity.setNickname(registerTo.getUserName());
        //密码加密处理：盐值加密,即在原来密码的基础上再加一些别的内容
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberEntity.setPassword(passwordEncoder.encode(registerTo.getPassword()));
        memberDao.insert(memberEntity);

    }

    private void chechPhoneUnique(String phone) {
        Long countPhone = memberDao.selectCount(new LambdaQueryWrapper<MemberEntity>().eq(MemberEntity::getMobile, phone));
        if (countPhone > 0) throw new PhoneExistException();
    }

    private void chechUserNameUnique(String userName) {
        Long countUser = memberDao.selectCount(new LambdaQueryWrapper<MemberEntity>().eq(MemberEntity::getUsername, userName));
        if (countUser > 0) throw new UserNameExistException();
    }

    @Override
    public MemberEntity login(MemberLoginVo member) {
        MemberEntity memberEntity = memberDao.selectOne(new LambdaQueryWrapper<MemberEntity>()
                .eq(MemberEntity::getUsername, member.getLoginAccount())
                .or()
                .eq(MemberEntity::getMobile, member.getPassword()));
        if (Objects.nonNull(memberEntity)) {
            return null;
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = memberEntity.getPassword();
        if (!passwordEncoder.matches(member.getPassword(), password)) {
            return null;
        }
        return memberEntity;
    }

}