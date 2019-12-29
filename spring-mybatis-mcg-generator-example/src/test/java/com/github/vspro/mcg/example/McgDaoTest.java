package com.github.vspro.mcg.example;

import com.alibaba.fastjson.JSON;
import com.github.vspro.mcg.example.dao.SysUserDao;
import com.github.vspro.mcg.example.domain.SysUserDo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/*.xml"})
public class McgDaoTest {

    @Autowired
    SysUserDao sysUserDao;

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
}
