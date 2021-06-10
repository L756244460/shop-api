package com.fh.shop.api.goods.biz;

import com.fh.shop.common.ServerResponse;

public interface ISpuService {


    ServerResponse findStatusList();

    ServerResponse findSku(Long id);
}
