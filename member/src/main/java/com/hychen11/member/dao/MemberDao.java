package com.hychen11.member.dao;

import com.hychen11.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author hychen11
 * @email 
 * @date 2025-02-10 16:22:04
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
