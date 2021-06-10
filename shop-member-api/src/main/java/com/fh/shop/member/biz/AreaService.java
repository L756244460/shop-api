package com.fh.shop.member.biz;

import com.fh.shop.common.ServerResponse;
import com.fh.shop.member.po.Area;

public interface AreaService {
    ServerResponse queryArea(Long memberId);

    ServerResponse addArea(Area area);

    ServerResponse updateStatu(Long memberId, Long id);

    ServerResponse updateArea(Area area);

    ServerResponse queryAreaById(Long id);

    ServerResponse deleteArea(Long id);
}
