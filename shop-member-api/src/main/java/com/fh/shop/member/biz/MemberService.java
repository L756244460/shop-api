package com.fh.shop.member.biz;

import com.fh.shop.common.ServerResponse;

public interface MemberService {


    ServerResponse login(String memberName, String passWord);

}
