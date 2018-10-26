package com.imooc.controller;

import com.imooc.utils.RedisOperator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {

    @Autowired
    public RedisOperator redis;

    public static final String USER_REDIS_SESSION = "user-redis-session";

    public static final String FILE_SPACE = "/Users/hptg/Documents/WeChat/IMOOV_VIDEO_FILE";

    public static final String FFMPEG_EXE = "/usr/local/bin/ffmpeg";

}
