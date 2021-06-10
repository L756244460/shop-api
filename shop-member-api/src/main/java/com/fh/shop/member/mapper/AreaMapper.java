package com.fh.shop.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.member.po.Area;

import java.util.List;

public interface AreaMapper extends BaseMapper<Area> {
    List<Area> queryByMemberId(Long memberId);

}
