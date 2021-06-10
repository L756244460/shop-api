package com.fh.shop.member.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.member.mapper.AreaMapper;
import com.fh.shop.member.po.Area;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("areaService")
@Transactional(rollbackFor = Exception.class)
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaMapper areaMapper;

    @Override
    public ServerResponse queryArea(Long memberId) {
        if (memberId==null){
            return ServerResponse.error(ResponseEnum.MEMBER_AREA_MEMBERID_NONO);
        }
        //Area area = areaMapper.selectById(memberId);
        List<Area> areas = areaMapper.queryByMemberId(memberId);
        return ServerResponse.success(areas);
    }

    @Override
    public ServerResponse addArea(Area area) {
        Area area1=new Area();
        area1.setMemberId(area.getMemberId());
        area1.setArea(area.getArea());
        area1.setRecipient(area.getRecipient());
        area1.setRecipientPhone(area.getRecipientPhone());
        areaMapper.insert(area1);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse updateStatu(Long memberId, Long id) {
        //先重置
        Area area=new Area();
        area.setStatu("0");
        QueryWrapper<Area> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("memberId",memberId);
        areaMapper.update(area, updateWrapper);
        //后修改
        Area area1=new Area();
        area1.setId(id);
        area1.setStatu("1");
        areaMapper.updateById(area1);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse updateArea(Area area) {
        areaMapper.updateById(area);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse queryAreaById(Long id) {
        QueryWrapper<Area> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Area area = areaMapper.selectOne(queryWrapper);
        return ServerResponse.success(area);
    }

    @Override
    public ServerResponse deleteArea(Long id) {
        areaMapper.deleteById(id);
        return ServerResponse.success();
    }
}
