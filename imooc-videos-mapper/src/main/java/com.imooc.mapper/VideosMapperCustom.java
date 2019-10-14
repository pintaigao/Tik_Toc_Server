package com.imooc.mapper;

import com.imooc.pojo.Videos;
import com.imooc.pojo.vo.VideosVO;
import com.imooc.utils.MyMapper;

import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideosMapperCustom extends MyMapper<Videos> {

    public List<VideosVO> queryAllVideos(@Param("videoDesc") String videoDesc);

    /* 对视频喜欢的数量进行累加 */
    void addVideoLikeCount(String videoId);

    /* 对视频喜欢的数量进行累减 */
    void reduceVideoLikeCount(String videoId);


}