package com.github.vspro.mcg.example.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CsSysUserDo {

   /**inject customized property through customized template
    * hello_customize
    */

    private Long id;

    /** ÓÃ»§Ãû */
    private String userName;

    private String password;

    private Boolean enabled;

    private Boolean atDeleted;

    private LocalDateTime atCreateTime;

    private LocalDateTime atModifyTime;


}
