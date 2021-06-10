package com.fh.shop.member.parme;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MemberUpdate {
    @ApiModelProperty(value = "会员密码")
    private String password;
    @ApiModelProperty(value = "确认密码")
    private String password1;
    @ApiModelProperty(value = "会员Id",example = "0",required = true)
    private Long id;
}
