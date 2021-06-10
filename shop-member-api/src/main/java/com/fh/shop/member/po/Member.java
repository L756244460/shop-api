package com.fh.shop.member.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class Member implements Serializable {

    @ApiModelProperty(value = "会员Id",example = "0")
    private Long id;

    @ApiModelProperty(value = "会员名称")
    private String memberName;
    @ApiModelProperty(value = "会员昵称")
    private String neckName;

    @ApiModelProperty(value = "会员密码")
    private String passWord;

    @ApiModelProperty(value = "会员邮箱")
    private String mail;

    @ApiModelProperty(value = "会员电话")
    private String phone;

    @ApiModelProperty(value = "是否激活",example = "0")
    private Integer statu;

    @ApiModelProperty(value = "积分",example = "0")
    private Integer score;


}
