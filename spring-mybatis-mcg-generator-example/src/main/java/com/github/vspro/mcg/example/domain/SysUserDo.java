package com.github.vspro.mcg.example.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysUserDo{



    private Long id;

    /** 用户名 */
    private String userName;

    private String password;

    private Boolean enabled;

    private Boolean atDeleted;

    private LocalDateTime atCreateTime;

    private LocalDateTime atModifyTime;

}
