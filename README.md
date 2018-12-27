# Tik_Toc_Server
简单的来讲，就是模仿抖音的后台
To simply said, this is the backend server of the app - TIK_TOC

## Procedure of setting up configuration 
手把手教你怎么搭建这个Spring Boot环境（妈的这个真是一堆坑），Using IDEA

### 开发过程

#### 一. 用户信息的开发：

* 开发Redis
  1. 按照视频把Redis搭建好
  2. 引入**RedisOperator.java**这个操作Redis的文件

* 我的个人信息接口的开发

* 用户头像上传的套路

  1. 新建/user/uploadFace Controller，这个Controller接受两个参数，一个是**userId**（url的Params中获取），另外一个是需要POST的文件（@RequestParam）

  2. 文件保存逻辑：两个常量，一个是保存在硬盘中的路径，另一个是保存到数据库中的绝对路径

     ```java
     String fileSpace = "/Users/hptg/Documents/Project/Spring/Tik_Toc/Resources";
     String uploadPathDB = "/" + userId + "/face";
     ```

     FileOutputStream 和InputStream分别对应输出到文件夹和客户端中输入进来的文件：

     ```java
     FileOutputStream fileOutputStream = null;
     InputStream inputStream = null;
     ```

     将从客户端传来的文件保存到硬盘中：（这个outFile不是文件而是指定位置的文件夹）

     ```java
     fileOutputStream = new FileOutputStream(outFile);
     inputStream = files[0].getInputStream();
     IOUtils.copy(inputStream, fileOutputStream);
     ```

  3. 将这个路径上传到数据库中(需要在Service中实现**updateUserInfo**的方法)：

     ```java
     Users user = new Users();
     user.setId(userId);
     user.setFaceImage(uploadPathDB); //创建新的user instance 只是为了查找出某个userId的user
     userService.updateUserInfo(user);
     ```

     然后在**updateUserInfo**中（创建Example.Criteria，并用内部方法updateByExampleSelective更新）：

     ```java
     Example userExample = new Example(Users.class);
     Example.Criteria criteria = userExample.createCriteria();
     criteria.andEqualTo("id", user.getId());
     usersMapper.updateByExampleSelective(user, userExample);
     ```

* 展示图片（先展示用户头像图片）

  1. 配置WebConfiguration，即配置Tomcat的虚拟目录并映射成web资源以达到图片的展示

     ```java
     @Configuration
     public class WebMvcConfig extends WebMvcConfigurerAdapter {
         @Override
         public void addResourceHandlers(ResourceHandlerRegistry registry) {
             registry.addResourceHandler("/**")
                 	.addResourceLocations("classpath:/META-INF/resources/").addResourceLocations("file:/Users/hptg/Documents/Project/Spring/Tik_Toc/Resources/");
         }
     
     ```

     此时就可以如下访问到图片：（用户名+face+图片名）
     http://localhost:8081/181219HBR49WA614/face/3236623630a84217bbdc81989a83dea6.png

  2. 程序中，直接使用上面那个地址来展示图片

* 查询用户信息（根据登陆的用户ID查询用户信息然后展示：包括昵称，头像图片等等）

  ```java
   @GetMapping("/query")
      public IMoocJSONResult query(String userId, String fanId) throws Exception {
          if (StringUtils.isBlank(userId)) {
              return IMoocJSONResult.errorMsg("用户id不能为空...");
          }
          Users userInfo = userService.queryUserInfo(userId);
          UsersVO userVO = new UsersVO();
          BeanUtils.copyProperties(userInfo, userVO);
          return IMoocJSONResult.ok(userVO);
      }
  ```



#### 二.短视频上传业务的开发

* 上传视频，文件的上传和存储逻辑上都和上传头像是一样的:

  唯一注意的是，上传的格式是**headers = "content-type=multipart/form-data"**(Postman中选择form-data但不用设置header)

  ```java
  @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
      public IMoocJSONResult upload(String userId, String bgmId, double videoSeconds, int videoWidth, int videoHeight, String desc, @ApiParam(value = "短视频", required = true) MultipartFile file) throws Exception
  ```

* videoService中saveVideo

  ```java
  videosMapper.insertSelective(video);
  ```

* 用ffmpeg截取视频的第一秒的图：

  ```java
  
  ```

* 截取一个视频的封面（小程序中的封面图是自动获取的)，然后上传和更新数据库:

  ```java
  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public void updateVideo(String videoId, String coverPath) {
  	Videos video = new Videos();
  	video.setId(videoId);
  	video.setCoverPath(coverPath);
  	videosMapper.updateByPrimaryKeySelective(video);
  }
  ```


#### 三. BGM选择业务的开发

* 获取所有的Bgm列表（/bgm/list），这个没什么好讲的，建立一个controller然后用Service去query所有的bgm

  ```java
  @Transactional(propagation = Propagation.SUPPORTS)
  @Override
  public List<Bgm> queryBgmList() {
  	return bgmMapper.selectAll();
  }
  ```

  同样获得某一个bgm直接：http://localhost:8081/BGM/TT.mp3, 因为之前已经设置过Tomcat的虚拟路径了


#### 四.小视频的展示

* 编写自定义的mapper：因为在视频展示页面，同时要获取两个东西，视频和用户信息同时展示，所以要自定义mapper，修改query语句来实现**关联查询**

  1. 新建自定义的VideoVO，匹配首页展示的数据（其实就是把User Pojo和Video Pojo的一些东西结合起来）

  2. 新建VideosMapperCustom

     ``` java
     /**
      * @Description: 条件查询所有视频列表
      */
     public List<VideosVO> queryAllVideos(@Param("videoDesc") String videoDesc,@Param("userId") String userId);
     /**
      * @Description: 查询关注的视频
      */
     public List<VideosVO> queryMyFollowVideos(String userId);
     /**
      * @Description: 查询点赞视频
      */
     public List<VideosVO> queryMyLikeVideos(@Param("userId") String userId);
     /**
      * @Description: 对视频喜欢的数量进行累加
      */
     public void addVideoLikeCount(String videoId);
     /**
      * @Description: 对视频喜欢的数量进行累减
      */
     public void reduceVideoLikeCount(String videoId);
     ```

  3. 新建VideosMapperCustom.xml，这里进行SQL语句的修改

     ```sql
      <select id="queryAllVideos" resultMap="BaseResultMap" parameterType="String">
       	select v.*,u.face_image as face_image,u.nickname as nickname from videos v
       	left join users u on u.id = v.user_id
       	where
       		1 = 1 // 这个就这样子了我也不知道为什么
       		<if test=" videoDesc != null and videoDesc != '' ">
       			and v.video_desc like '%${videoDesc}%'
       		</if>
       		<if test=" userId != null and userId != '' ">  
     			and v.user_id = #{userId}
     		</if>
       		and v.status = 1
       	order by v.create_time desc
       </select>
     ```

  4. 分页查询，使用的是外部的一个PageHelper.java(com.github.pagehelper)，自定义的一个PageResult（用于展示页面的信息封装）

     ![PageHelper原理](/Users/hptg/Documents/Project/Spring/Tik_Toc/Resources/PageHelper原理.png)

  5. 查询视频的接口


#### 五.热搜词的接口开发

#### 六.拦截器接口的开发（为防止未认证用户暴力查询到所有信息）

* 实现HandlerInterceptor的接口（preHandle，postHandle，afterCompletion），return true or false

  ```java
   /**
    * 拦截请求，在controller调用之前
    */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
      ...
  }
   /**
    * 请求controller之后，渲染视图之前
    */
  @Override
  public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {
      ...
  }
  
  /**
   * 请求controller之后，视图渲染之后
   */
  @Override
  public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
      ...
  }
  ```


#### 七.视频点赞和取消点赞的开发

* 对自定义的VideoCustomMapper改造：Java 文件新增addVideoLikeCount，xml文件新增相关sql语句

  ```java
  public void addVideoLikeCount(String videoId);
  ```

  ```xml
  <update id="addVideoLikeCount" parameterType="String">
    	update videos set like_counts=like_counts+1 where id=#{videoId}
  </update>
  ```



#### 八.进入某个视频出查询所有视频的信息

#### 九.关注与取消关注用户的接口

#### 十.用户评论和用户评评论



#### 十一.举报用户
















