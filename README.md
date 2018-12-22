# Tik_Toc_Server
简单的来讲，就是模仿抖音的后台
To simply said, this is the backend server of the app - TIK_TOC

## Procedure of setting up configuration 
手把手教你怎么搭建这个Spring Boot环境（妈的这个真是一堆坑），Using IDEA

完成后待我细细慢来怎么踩完这堆坑



### 开发过程

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

