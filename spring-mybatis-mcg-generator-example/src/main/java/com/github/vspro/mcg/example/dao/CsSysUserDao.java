package com.github.vspro.mcg.example.dao;

import com.github.vspro.mcg.example.domain.CsSysUserDo;
import org.apache.ibatis.annotations.Param;
import java.util.Collection;

public interface CsSysUserDao {

    int insert(CsSysUserDo cssysuserdo);

    int insertOrUpdate(CsSysUserDo cssysuserdo);

    CsSysUserDo selectByPrimaryKey(@Param("id") Long id);

    CsSysUserDo selectOneSelective(CsSysUserDo cssysuserdo);

    int updateByPrimaryKeySelective(CsSysUserDo cssysuserdo);

    int updateByPrimaryKey(CsSysUserDo cssysuserdo);

    int deleteByPrimaryKey(@Param("id") Long id);


    int batchInsert(@Param("list") Collection<CsSysUserDo> list);

}

