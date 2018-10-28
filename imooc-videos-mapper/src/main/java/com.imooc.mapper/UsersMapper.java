package com.imooc.mapper;

import com.imooc.pojo.Users;
import com.imooc.utils.MyMapper;

public interface UsersMapper extends MyMapper<Users> {
    public void addReceiveLikeCount(String userId);

    public void reduceReceiveLikeCount(String userId);

    /* 增加粉丝数量 */
    public void addFansCount(String userId);

    /* 减少粉丝数量 */
    public void reduceFansCount(String userId);

    /* 增加关注数*/
    public void addFollowersCount(String userId);


    /* 减少关注数 */
    public void reduceFollowerCount(String userId);


}