package com.fh.shop.member.parme;

import com.fh.shop.member.po.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class MemberParme extends Member implements Serializable {

    @ApiModelProperty(value = "确认密码")
    private String password1;

    @ApiModelProperty(value = "验证码")
    private String smmCord;

}
