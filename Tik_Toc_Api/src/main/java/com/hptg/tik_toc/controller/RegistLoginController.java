package com.hptg.tik_toc.controller;

import com.hptg.tik_toc.pojo.Users;
import com.hptg.tik_toc.pojo.vo.UsersVO;
import com.hptg.tik_toc.service.UserService;
import com.hptg.tik_toc.utils.IMoocJSONResult;
import com.hptg.tik_toc.utils.MD5Utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "用户注册登录的接口", tags = {"注册和登录的controller"})
public class UserController extends BasicController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户注册", notes = "用户注册的接口")
    @PostMapping("/regist")
    public IMoocJSONResult regist(@RequestBody Users user) throws Exception {
        // 1. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return IMoocJSONResult.errorMsg("用户名和密码不能为空");
        }

        // 2. 判断用户名是否存在
        boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());

        // 3. 保存用户，注册信息
        if (!usernameIsExist) {
            user.setNickname(user.getUsername());
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            user.setFansCounts(0);
            user.setReceiveLikeCounts(0);
            user.setFollowCounts(0);
            userService.saveUser(user);
        } else {
            return IMoocJSONResult.errorMsg("用户名已经存在，请换一个再试");
        }
        user.setPassword("");
        UsersVO userVO = setUserRedisSessionToken(user);
        return IMoocJSONResult.ok(userVO);

    }

    /**
     * Description: 用户登陆接口
     */
    @ApiOperation(value = "用户登录", notes = "用户登录的接口")
    @PostMapping("/login")
    public IMoocJSONResult login(@RequestBody Users user) throws Exception {
        String username = user.getUsername();
        String password = user.getPassword();

		// Thread.sleep(3000);

        // 1. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return IMoocJSONResult.ok("用户名或密码不能为空...");
        }

        // 2. 判断用户是否存在
        Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(user.getPassword()));

        // 3. 判读用户信息是否正确
        if (userResult != null) {
            userResult.setPassword("");
            UsersVO userVO = setUserRedisSessionToken(userResult);
            return IMoocJSONResult.ok(userVO);
        } else {
            return IMoocJSONResult.errorMsg("用户名或密码不正确, 请重试...");
        }
    }

    /**
     * Description: 用户注销的接口
     */
    @ApiOperation(value="用户注销", notes="用户注销的接口")
    //@ApiImplicitParam(name="userId", value="用户id", required=true, dataType="String", paramType="query")
    @PostMapping("/logout")
    public IMoocJSONResult logout(String userId) throws Exception {
        // 即把Redis的Key删掉就行了
        redis.del(USER_REDIS_SESSION + ":" + userId);
        return IMoocJSONResult.ok();
    }


    /* Redis Set Key 和 Value 一气呵成的方法*/
    public UsersVO setUserRedisSessionToken(Users userModel) {
        /* 继承BasicController(i.e：添加对Redis的支持)后干的事情：1. 生成UniqueKey(即value)，对应key：UserId*/
        String uniqueToken = UUID.randomUUID().toString();
        redis.set(USER_REDIS_SESSION + ":" + userModel.getId(), uniqueToken, 1000 * 60 * 30);
        /* 2.更改并新建另一个User Class,以将User Redis Token放进去*/
        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(userModel, userVO); // 把原来user（上面建立的）中的属性拷贝到这个新的instance中
        userVO.setUserToken(uniqueToken);
        return userVO;
    }

}
