package com.fh.shop.member.parme;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdatePassword implements Serializable {

    private String password;

    private String newPsssword;

    private String newPsssword1;

    private Long id;


}
