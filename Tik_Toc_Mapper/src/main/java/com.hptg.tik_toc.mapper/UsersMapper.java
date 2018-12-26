package com.hptg.tik_toc.mapper;

import com.hptg.tik_toc.pojo.Users;
import com.hptg.tik_toc.utils.MyMapper;

import org.apache.ibatis.annotations.Mapper;

public interface UsersMapper extends MyMapper<Users> {

    /**
     * @Description: 用户受喜欢数累加
     */
    public void addReceiveLikeCount(String userId);

    /**
     * @Description: 用户受喜欢数累减
     */
    public void reduceReceiveLikeCount(String userId);

    /**
     * @Description: 增加粉丝数
     */
    public void addFansCount(String userId);

    /**
     * @Description: 增加关注数
     */
    public void addFollersCount(String userId);

    /**
     * @Description: 减少粉丝数
     */
    public void reduceFansCount(String userId);

    /**
     * @Description: 减少关注数
     */
    public void reduceFollersCount(String userId);
}
