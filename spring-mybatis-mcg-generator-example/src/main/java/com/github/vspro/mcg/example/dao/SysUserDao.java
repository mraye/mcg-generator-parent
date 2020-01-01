package com.github.vspro.mcg.example.dao;

import com.github.vspro.mcg.example.domain.SysUserDo;
import org.apache.ibatis.annotations.Param;
import java.util.Collection;

public interface SysUserDao {

    int insert(SysUserDo sysuserdo);

    int insertOrUpdate(SysUserDo sysuserdo);

    SysUserDo selectByPrimaryKey(@Param("id")Long id);

    SysUserDo selectOneSelective(SysUserDo sysuserdo);

    int updateByPrimaryKeySelective(SysUserDo sysuserdo);

    int updateByPrimaryKey(SysUserDo sysuserdo);

    int deleteByPrimaryKey(@Param("id") Long id);

    int deleteLogicalByPrimaryKey(@Param("id") Long id);

    int batchInsert(@Param("list") Collection<SysUserDo> list);

}

