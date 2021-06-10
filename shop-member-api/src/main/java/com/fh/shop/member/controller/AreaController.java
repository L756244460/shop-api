package com.fh.shop.member.controller;

import com.fh.shop.common.Secret;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.member.biz.AreaService;
import com.fh.shop.member.po.Area;
import com.fh.shop.member.vo.MemberVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@Api(tags = "会员订单接口")
public class AreaController {

    @Resource
    private AreaService areaService;

    @Resource
    private HttpServletRequest request;

    @GetMapping("/area/queryArea")
    @ApiOperation("查询会员收件地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId",value = "会员Id",dataType = "java.lang.Long"),
            @ApiImplicitParam(name = "x-auth",value = "头信息",dataType = "java.lang.String",required = true,paramType ="header")
    })
    public ServerResponse queryArea(Long memberId){
        MemberVo memberVo= (MemberVo) request.getAttribute(Secret.getMember);
        Long id = memberVo.getId();
        memberId=id;
        return areaService.queryArea(memberId);
    }

    @PostMapping("/area/addArea")
    @ApiOperation("新增会员收件地址")
    public ServerResponse addArea(Area area){
        MemberVo memberVo= (MemberVo) request.getAttribute(Secret.getMember);
        Long id = memberVo.getId();
        area.setMemberId(id);
        return areaService.addArea(area);
    }


    @PostMapping("/area/updateStatu")
    @ApiOperation("修改会员收件地址状态")
    public ServerResponse updateStatu(Long id){
        MemberVo memberVo= (MemberVo) request.getAttribute(Secret.getMember);
        Long memberId = memberVo.getId();
        return areaService.updateStatu(memberId,id);
    }


    @GetMapping("/area/queryAreaById")
    @ApiOperation("根据Id查询会员收件地址")
    public ServerResponse queryAreaById(Long id){
        return areaService.queryAreaById(id);
    }


    @PostMapping("/area/updateArea")
    @ApiOperation("修改会员收件地址")
    public ServerResponse updateArea(Area area){
        MemberVo memberVo= (MemberVo) request.getAttribute(Secret.getMember);
        Long memberId = memberVo.getId();
        area.setMemberId(memberId);
        return areaService.updateArea(area);
    }

    @PostMapping("/area/deleteArea")
    @ApiOperation("删除")
    public ServerResponse deleteArea(Long id){
        return areaService.deleteArea(id);
    }


}
