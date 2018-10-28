package com.imooc.service;

import com.imooc.pojo.Videos;
import com.imooc.utils.PagedResult;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoService {


    /* 保存视频 */
    public String saveVideo(Videos videos);

    /* 保存封面 */
    public void updateVideo(String videoId, String coverPath);

    /* 分页查询视频列表 */
    public PagedResult getAllVideos(Videos video, Integer isSaveRecord, Integer page, Integer pageSize);

    /* 获取热搜词列表 */
    public List<String> getHotwords();

    public void userLikeVideo(String userId, String videoId, String videoCreaterId);

    public void userUnLikeVideo(String userId, String videoId, String videoCreaterId);
}
