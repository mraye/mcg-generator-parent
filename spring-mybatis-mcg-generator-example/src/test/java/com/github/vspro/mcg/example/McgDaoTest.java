package com.github.vspro.mcg.example;

import com.alibaba.fastjson.JSON;
import com.github.vspro.mcg.example.dao.SysUserDao;
import com.github.vspro.mcg.example.domain.SysUserDo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/*.xml"})
public class McgDaoTest {

    @Autowired
    SysUserDao sysUserDao;

    @Test
    public void insetTest(){
        SysUserDo sysUserDo = new SysUserDo();
        sysUserDo.setUserName("test2");
        sysUserDo.setEnabled(true);
        sysUserDo.setAtCreateTime(LocalDateTime.now());
        sysUserDo.setAtModifyTime(LocalDateTime.now());
        sysUserDo.setPassword("1212");
        sysUserDo.setAtDeleted(true);
        sysUserDao.insert(sysUserDo);
    }
    @Test
    public void insetOrUpdateTest(){
        SysUserDo sysUserDo = new SysUserDo();
        sysUserDo.setId(8L);
        sysUserDo.setUserName("test2");
        sysUserDo.setEnabled(true);
        sysUserDo.setAtCreateTime(LocalDateTime.now());
        sysUserDo.setAtModifyTime(LocalDateTime.now());
        sysUserDo.setPassword("1212kdjskkd");
        sysUserDo.setAtDeleted(false);
        sysUserDao.insertOrUpdate(sysUserDo);
    }
    @Test
    public void selectByPrimaryKeyTest(){
        SysUserDo sysUserDo  = sysUserDao.selectByPrimaryKey(1L);
        if (sysUserDo != null) {
            System.out.println(JSON.toJSONString(sysUserDo));
        }
    }

    @Test
    public void selectOneSelective(){
        SysUserDo sysUserDo = new SysUserDo();
        sysUserDo.setUserName("test");
        sysUserDo.setEnabled(true);
        sysUserDo  = sysUserDao.selectOneSelective(sysUserDo);
        if (sysUserDo != null) {
            System.out.println(JSON.toJSONString(sysUserDo));
        }
    }
    @Test
    public void updateByPrimaryKeySelective(){
        SysUserDo sysUserDo = new SysUserDo();
        sysUserDo.setId(1L);
        sysUserDo.setUserName("testme");
        sysUserDo.setEnabled(true);
        sysUserDao.updateByPrimaryKeySelective(sysUserDo);

    }
    @Test
    public void updateByPrimaryKeyTest(){
        SysUserDo sysUserDo = new SysUserDo();
        sysUserDo.setId(1L);
        sysUserDo.setUserName("tesatme");
        sysUserDo.setEnabled(true);
        sysUserDao.updateByPrimaryKey(sysUserDo);

    }

    @Test
    public void deleteLogicalByPrimaryKeyTest(){
        sysUserDao.deleteLogicalByPrimaryKey(1L);
    }
}
