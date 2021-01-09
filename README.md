# README

HelloFlower是一款物联网时代智慧家居app，用户可以使用这款软件远程管理植物景观系统，并可与其他用户进行互动。

## Features

* 使用Google推荐的Kotlin作为项目开发语言
* 使用Retrofit2作为网络库
* 使用阿里云ECS云服务器，基于Apache搭建php后端环境，接入MySQL实现数据查询
* 使用阿里云IOT开发平台，基于Mqtt协议实现传感器数据上报与控制命令下达
* 遵循Material Design进行界面、布局、组件的设计
* 使用LiveData实现数据传感器数据动态更新
* 使用Room进行本地数据库处理，减少网络请求次数
* 使用PreferenceScreen操作本地SharedPreference进行数据持久化

## Log

* 2020-12-21
  * 将APP接入IOT封装成工具类，实现了传参直接创建设备、自动订阅topic、自动发布topic
  * 实现了app内绑定传感器设备、手机设备的功能
  * 实现了数据流转
  * 创建了Room保存传感器信息
  * 修改设置界面，使用PreferenceScreen布局结合SharedPreference保存app配置信息
* 添加了“添加装置”的功能（但存在闪退的问题）
  * 实现了进入详情页后设备连接IOT平台
  
* 2020-12-20
  * 修复了反馈闪退的问题（线程里不能修改UI主线程）
  * 修复了设置闪退的问题（测试时使用了其他布局的控件）
  * 实现了APP接入阿里云IOT平台的功能

* 2020-12-15
  * 基本实现了反馈功能

* 2020-12-13
  * 更新了drawer的选项
  * 初步添加setting页面
  * 初步添加反馈页面
* 2020-12-10
  * 导入了Retrofit库，使用阿里云服务器实现了基础的网络数据获取：具体途径是点击进入某个列表项的详情菜单后，再点击浮动按钮
  * ESP32开发板到手，WIFI模块测试正常
  * 和组员讨论IOT选用阿里IOT平台+mqtt或者阿里ECS云服务器+http，初步决定使用前者
* 2020-12-8
  * 添加首页刷新的视觉效果
  * 增加详情页，使用collapse布局
  * 为首页列表项添加点击事件，打开对应详情页 
  * 修改详情页标题栏，实现视差、状态栏隐藏
* 2020-12-7
  * 首次推送至git
  * 为首页添加drawer布局
  * 为首页添加侧边3按钮
  * 为首页添加滑动卡片布局

## Challenges

* 使用retrofit2发送post请求时无法，php端解析$_POST为空
  * retrofit2发送json，请求头是application/json而不是application/x-www-form-urlencoded或者multipart/form-data，php不会解析成post数组
  * 解决方法是手动转数组[^1]
* gradle build经常出现证书错误问题"unable to find valid certification path to requested target"
  * 手动下载证书并添加到本地[^2]
* Unable to add window -- token null is not valid; is your activity running? 使用acticity的上下文context

## References

[^1]: https://stackoverflow.com/questions/34274390/can-not-get-post-parameters-when-sending-post-request-with-retrofit/34275428#34275428

[^2]: https://stackoverflow.com/questions/21076179/pkix-path-building-failed-and-unable-to-find-valid-certification-path-to-requ



---

1. 《第一行代码（第三版）》郭霖
2. [Google官方推荐的应用内Icons](https://material.io/resources/icons/)
3. [Material Design - 一套便捷的界面开发标准](https://material.io/components)
4. [获取应用程序版本信息](https://blog.csdn.net/true_maitian/article/details/74963867?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.control&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.control) (使用BuildConfig下的结果即可)
5. [阿里云官网提供的Android SDK接入标准](https://help.aliyun.com/document_detail/146630.html?spm=a2c4g.11186623.2.29.4ffc7c80rLjicv#task-2362441)

