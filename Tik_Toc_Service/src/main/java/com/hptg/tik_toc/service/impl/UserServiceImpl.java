package com.hptg.tik_toc.service.impl;

import com.hptg.tik_toc.mapper.UsersMapper;
import com.hptg.tik_toc.pojo.Users;
import com.hptg.tik_toc.pojo.UsersReport;
import com.hptg.tik_toc.service.UserService;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        /*Users user = new Users();
        user.setUsername(username);
        Users result = usersMapper.selectOne(user);*/
        /* 使用Example的同等效果*/
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username",username);
        Users result = usersMapper.selectOneByExample(userExample);
        return result != null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUser(Users user) {
        String userId = sid.nextShort();
        user.setId(userId);
        usersMapper.insert(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {

        /* 创建Example（然后才能创建criteria）的作用是：才能根据一个条件去做一个匹配*/
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username",username);
        criteria.andEqualTo("password",password);
        Users result = usersMapper.selectOneByExample(userExample);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserInfo(Users user) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id", user.getId());
        usersMapper.updateByExampleSelective(user, userExample);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id", userId);
        Users user = usersMapper.selectOneByExample(userExample);
        return user;
    }

    @Override
    public boolean isUserLikeVideo(String userId, String videoId) {
        return false;
    }

    @Override
    public void saveUserFanRelation(String userId, String fanId) {

    }

    @Override
    public void deleteUserFanRelation(String userId, String fanId) {

    }

    @Override
    public boolean queryIfFollow(String userId, String fanId) {
        return false;
    }

    @Override
    public void reportUser(UsersReport userReport) {

    }
}
