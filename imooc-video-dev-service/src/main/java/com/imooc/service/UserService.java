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


    /* 用户修改信息 */
    public void updateUserInfo(Users user);

    /* 查询用户信息 */
    public Users queryUserInfo(String userId);

    /* 查询用户是否喜欢点赞视频 */
    public boolean isUserLikeVideo(String userId, String videoId);

    /* 增加用户和粉丝的关系*/
    public void saveUserFanRelation(String userID,String fanId);

    /* 删除用户和粉丝的关系*/
    public void deleteUserFanRelation(String userId, String fanId);
}
