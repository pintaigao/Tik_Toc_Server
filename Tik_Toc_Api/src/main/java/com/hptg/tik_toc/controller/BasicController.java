package com.hptg.tik_toc.controller;

import com.hptg.tik_toc.utils.RedisOperator;

import org.springframework.beans.factory.annotation.Autowired;

public class BasicController {

    @Autowired
    public RedisOperator redis;

    public static final String USER_REDIS_SESSION = "user-redis-session";
}
