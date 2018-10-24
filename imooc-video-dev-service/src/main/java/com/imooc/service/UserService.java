package com.imooc.service;

import com.imooc.pojo.Users;

import org.springframework.stereotype.Repository;

@Repository
public interface UserService {

    /* 判断用户名是否存在*/
    boolean queryUsernameIsExist(String username);


    /*保存用户*/
    void saveUser(Users user);
}
