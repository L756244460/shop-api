package com.fh.shop.member.biz;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.common.KeyUtil;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.Secret;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.member.mapper.MemberMapper;
import com.fh.shop.member.po.Member;
import com.fh.shop.member.vo.MemberVo;
import com.fh.shop.util.Md5Util;
import com.fh.shop.util.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service("memberService")
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;


    @Override
    public ServerResponse login(String memberName, String passWord) {
        //验证数据是否为空
         if (StringUtils.isEmpty(memberName) || StringUtils.isEmpty(passWord)){
            return ServerResponse.error(ResponseEnum.MEMBER_LOGIN_NONE);
        }
        //验证用户名是否存在
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("memberName",memberName);
        Member member = memberMapper.selectOne(queryWrapper);
        if (member==null){
            return ServerResponse.error(ResponseEnum.MEMBER_LOGIN_NAME_IS_NONE);
        }
        Integer statu = member.getStatu();
        if (statu==1){
            String mail = member.getMail();
            String uuid = UUID.randomUUID().toString();
            String key = KeyUtil.buildActivateKey(uuid);
            RedisUtils.setex(key,member.getId()+"",7*60);
            Map<String,String> map=new HashMap<>();
            map.put("mail",mail);
            map.put("uuid",key);
            return ServerResponse.error(ResponseEnum.MEMBER_RESTE_ACTIVE_ERROR,map);
        }
        //验证密码是否正确
        if (!Md5Util.md5(passWord).equals(member.getPassWord())){
            return ServerResponse.error(ResponseEnum.MEMBER_LOGIN_PASSWORD_IS_ERROR);
        }
        //获取签名
        MemberVo memberVo=new MemberVo();
        Long id = member.getId();
        memberVo.setId(id);
        memberVo.setMemeberName(member.getMemberName());
        memberVo.setNeckName(member.getNeckName());
        //将会员对象转换为字符串
        String memberVoJson = JSON.toJSONString(memberVo);
        String sign = Md5Util.eign(memberVoJson ,Secret.getSecret);
        //将签名用64位***
        String memberVoJsonBase64 = Base64.getEncoder().encodeToString(memberVoJson.getBytes());
        String signBase64 = Base64.getEncoder().encodeToString(sign.getBytes());
        RedisUtils.setex(KeyUtil.buildMemberKey(id),"",2*60);
        return ServerResponse.success(memberVoJsonBase64+"."+signBase64);
    }


}
