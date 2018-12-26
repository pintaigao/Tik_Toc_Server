package com.hptg.tik_toc.controller;

import com.hptg.tik_toc.pojo.Users;
import com.hptg.tik_toc.pojo.UsersReport;
import com.hptg.tik_toc.pojo.vo.PublisherVideo;
import com.hptg.tik_toc.pojo.vo.UsersVO;
import com.hptg.tik_toc.service.UserService;
import com.hptg.tik_toc.utils.IMoocJSONResult;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "用户相关业务的接口", tags = {"用户相关业务的controller"})
@RequestMapping("/user")
public class UserController extends BasicController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户上传头像", notes = "用户上传头像的接口")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query")
    @PostMapping("/uploadFace")
    public IMoocJSONResult uploadFace(String userId, @RequestParam("file") MultipartFile[] files) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("用户id不能为空...");
        }
        /* 两个常量 */
        // 1.文件保存的命名空间
        String fileSpace = "/Users/hptg/Documents/Project/Spring/Tik_Toc/Resources";
        // 2.保存到数据库中的相对路径
        String uploadPathDB = "/" + userId + "/face";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            if (files != null && files.length > 0) {
                String fileName = files[0].getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {
                    // 文件上传的最终保存路径
                    String finalFacePath = fileSpace + uploadPathDB + "/" + fileName;
                    // 设置数据库保存的路径
                    uploadPathDB += ("/" + fileName);

                    File outFile = new File(finalFacePath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = files[0].getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }

            } else {
                return IMoocJSONResult.errorMsg("上传出错...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return IMoocJSONResult.errorMsg("上传出错...");
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        Users user = new Users();
        user.setId(userId);
        user.setFaceImage(uploadPathDB);
        /* 将上传图像的相对路径同步到数据库中*/
        userService.updateUserInfo(user);

        return IMoocJSONResult.ok(uploadPathDB);
    }

    @ApiOperation(value = "查询用户信息", notes = "查询用户信息的接口")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query")
    @GetMapping("/query")
    public IMoocJSONResult query(String userId, String fanId) throws Exception {
        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("用户id不能为空...");
        }
        Users userInfo = userService.queryUserInfo(userId);
        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(userInfo, userVO);

//        userVO.setFollow(userService.queryIfFollow(userId, fanId));

        return IMoocJSONResult.ok(userVO);
    }


    /**
     * Description: 当用户点进具体的视频时候要获取的信息的查询接口
     */
    @GetMapping("/queryPublisher")
    public IMoocJSONResult queryPublisher(String loginUserId, String videoId, String publishUserId) throws Exception {

        if (StringUtils.isBlank(publishUserId)) {
            return IMoocJSONResult.errorMsg("");
        }

        // 1. 查询视频发布者的信息
        Users userInfo = userService.queryUserInfo(publishUserId);
        UsersVO publisher = new UsersVO();
        BeanUtils.copyProperties(userInfo, publisher);

        // 2. 查询当前登录者和视频的点赞关系
        boolean userLikeVideo = userService.isUserLikeVideo(loginUserId, videoId);
        PublisherVideo bean = new PublisherVideo();
        bean.setPublisher(publisher);
        bean.setUserLikeVideo(userLikeVideo);

        return IMoocJSONResult.ok(bean);
    }

    /**
     * Description: 成为某用户的粉丝
     * userId:被成为粉丝的ID，fanId:登录的用户
     */
    @PostMapping("/beyourfans")
    public IMoocJSONResult beyourfans(String userId, String fanId) throws Exception {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
            return IMoocJSONResult.errorMsg("");
        }
        userService.saveUserFanRelation(userId, fanId);
        return IMoocJSONResult.ok("关注成功...");
    }

    /**
     * Description: 取消成为某用户的粉丝
     * userId:被成为粉丝的ID，fanId:登录的用户
     */
    @PostMapping("/dontbeyourfans")
    public IMoocJSONResult dontbeyourfans(String userId, String fanId) throws Exception {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
            return IMoocJSONResult.errorMsg("");
        }

        userService.deleteUserFanRelation(userId, fanId);

        return IMoocJSONResult.ok("取消关注成功...");
    }

    @PostMapping("/reportUser")
    public IMoocJSONResult reportUser(@RequestBody UsersReport usersReport) throws Exception {

        // 保存举报信息
        userService.reportUser(usersReport);

        return IMoocJSONResult.errorMsg("举报成功...有你平台变得更美好...");
    }

}
