package com.fh.shop.member.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class Area implements Serializable {
    @ApiModelProperty(value = "会员地址Id",example = "0")
    private Long id;
    @ApiModelProperty(value = "会员Id",example = "0")
    private Long memberId;
    //收件人
    @ApiModelProperty(value = "收件人")
    private String recipient;
    //收件地址
    @ApiModelProperty(value = "收件地址")
    private String area;
    //收件人电话
    @ApiModelProperty(value = "收件人电话")
    private String recipientPhone;
    @ApiModelProperty(value = "状态")
    private String statu;


}
