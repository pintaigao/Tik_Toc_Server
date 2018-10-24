package com.imooc.service;

import com.imooc.pojo.Users;

import org.springframework.stereotype.Repository;

@Repository
public interface UserService {

    /* 判断用户名是否存在*/
    boolean queryUsernameIsExist(String username);


    /*保存用户*/
    void saveUser(Users user);


    /*用户登陆，根据用户名和密码查询用户*/
    Users queryUserForLogin(String username, String md5Str);
}
