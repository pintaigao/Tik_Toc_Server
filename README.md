# Tik_Toc_Server
简单的来讲，就是模仿抖音的后台
To simply said, this is the backend server of the app - TIK_TOC

## Procedure of setting up configuration 
手把手教你怎么搭建这个Spring Boot环境（妈的这个真是一堆坑），Using IDEA

### 开发过程

####一. 用户信息的开发：

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

