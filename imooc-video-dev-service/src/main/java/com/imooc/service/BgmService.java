package com.imooc.service;

import com.imooc.pojo.Bgm;
import com.imooc.pojo.Users;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BgmService {


    public List<Bgm> queryBgmList();

    public Bgm queryBgmById(String bgmId);


}
