# Self Growth Android 
***

**自我生长：成为更好的自己**

**自我生成手机应用使用情况统计Android客户端**

初始版本功能完善与开发中

## 简介
自律性辅助软件，成为更好的自己

定时的检测收集当前手机的顶层应用，将其记录到本地存储中，用于统计和展示手机APP的使用情况

有简单的任务清单功能，提供辅助的任务规划安排

## 功能简介
客户端主要功能定位是：

- [x] 收集手机当前应用使用情况
- [x] 任务列表
- [x] 今日当前截止手机应用使用情况
- [x] 统计展示一定时间周期内的APP和任务数据

数据均是存储在本地，在无网络的情况下，完全可以正常使用所有功能

任务相关的模块在登录后，可以和Web进行同步使用

数据在登录的情况下，可选择同步到服务器，用于数据丢失恢复和跨手机设备同步

## 工程运行说明
本工程开发基于下面的版本：

- Android SDK 11
- Java 8

注：高版本的 Android Studio 也能正常运行，但由于当前还没有Lombok相应的支持，其相关的Get等方法在编辑器中会被标红，但不影响运行,使用本地强行安装的方式可解决

## 相关的工程与软件
因为本软件是基于检测手机软件进行相关的数据统计的，某些活动可能没有合适的APP，所以写了一些纯展示的APP，用于配合该APP使用

* 读纸质书
  * github: [readbook app download](https://github.com/lw1243925457/ReadBookEmptyApp/releases/download/V0/readbook.apk)
  * gitee: [readbook app download](https://gitee.com/free-love/ReadBookEmptyApp/attach_files/1017157/download/ReadBook.apk)
  
* 代码编程
  * github: [coding app download](https://github.com/lw1243925457/CodeEmptyApp/releases/download/V0/coding.apk)
  * gitee: [coding app download](https://gitee.com/free-love/CodeEmptyApp/attach_files/1017159/download/coding.apk)

## 手机应用情况本地存储统计策略
### 活动
频率：每十秒记录一次当前的顶层手机应用Activity

当前使用SharedPreferences进行数据的存储

目前对于每天的时间约定稍微有些不同，如下：

获取当前时间日期，到天，如 2021_02_21,注意，22点后按照第二天算，也就是不是0点到第二天，而是10点到第二天，恩，早点睡觉比较好

### 任务

## 参考链接
### 资源
- [Awesome Android](https://github.com/JStumpp/awesome-android)
- [Android DOCUMENTATION](https://developer.android.com/reference)
- [字节开源的，Apache2 使用起来放心点：IconPark](https://github.com/bytedance/iconpark)
- [iconparkBETA](https://iconpark.oceanengine.com/official)
- [有哪些无版权、免费、高清图片素材网站？](https://www.zhihu.com/question/318961106/answer/767074512)
- [官方Vector资源:res文件夹右键也可以添加资源](https://fonts.google.com/icons?selected=Material+Icons)
- [pexels 图片资源丰富](https://www.pexels.com/zh-cn/search/%E6%89%8B%E6%9C%BA%E5%A3%81%E7%BA%B8/)

### 自动登录
- [Android学习之保存用户登录信息](https://blog.csdn.net/u013132758/article/details/)
- [Fragment生命周期与Fragment执行hide、show后的生命周期探讨](https://blog.csdn.net/s13383754499/article/details/84782605)

### 控件
- [Android列表组件ListView使用详解之动态加载或修改列表数据](https://cloud.tencent.com/developer/article/1742232)
- [Android几种方式实现控件圆角](https://www.jianshu.com/p/ab42f2198776)
- [Best practice to implement key-value pair in android Spinner](https://stackoverflow.com/questions/35449800/best-practice-to-implement-key-value-pair-in-android-spinner)
- [Nice Spinner](https://github.com/arcadefire/nice-spinner)
- [关于Android中Button的Backgroud背景设置默认为蓝紫色，且无法修改的问题](https://my.oschina.net/u/4296470/blog/4732012)
- 输入文本框自动补全
  - ["ArrayAdapter requires the resource ID to be a TextView" XML problems](https://stackoverflow.com/questions/9280965/arrayadapter-requires-the-resource-id-to-be-a-textview-xml-problems)
  - [实例讲解Android中的AutoCompleteTextView自动补全组件](https://m.xp.cn/b.php/54409.html)
- [Android自定义View之PopupLayout（通用弹出式布局）](https://blog.csdn.net/CodingEnding/article/details/81489947)
- [安卓layout布局三等分](https://blog.csdn.net/LY_Dengle/article/details/70172918)
- [Android 退出确认弹窗(AlertDialog)](https://blog.csdn.net/qq_35988274/article/details/100513452)
- [对话框](https://developer.android.com/guide/topics/ui/dialogs)
- [图表库：MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
- [Android输入法弹出时界面被挤压的问题](https://blog.csdn.net/Sunxiaolin2016/article/details/111947770)

### 路由与跳转
- [How to move from one fragment to another fragment on click of an ImageView in Android?](https://stackoverflow.com/questions/23212162/how-to-move-from-one-fragment-to-another-fragment-on-click-of-an-imageview-in-an)
- [Fragment transactions](https://developer.android.com/guide/fragments/transactions)
- [Is there a way to check for popBackStack call in a fragment](https://stackoverflow.com/questions/52645932/is-there-a-way-to-check-for-popbackstack-call-in-a-fragment)
- [Programmatically go back to the previous fragment in the backstack](https://stackoverflow.com/questions/10863572/programmatically-go-back-to-the-previous-fragment-in-the-backstack)

### 数据存储与操作
- [保存键值对数据](https://developer.android.com/training/data-storage/shared-preferences?hl=zh-cn)
- [Android 数据存储（一）——SharedPreferences](https://www.cnblogs.com/PengLee/p/4147530.html)

### 网络
- [Add Header Parameter in Retrofit](https://stackoverflow.com/questions/42898920/add-header-parameter-in-retrofit)
- [Retotfit2默认带请求头](https://blog.csdn.net/yechaoa/article/details/103067284)
- [Retrofit 2 with only form-data](https://stackoverflow.com/questions/37814857/retrofit-2-with-only-form-data)
- [A type-safe HTTP client for Android and Java](https://square.github.io/retrofit/)
- [http请求配置](https://blog.csdn.net/qq_15204179/article/details/98663289)
- [Okhttp3 Https请求配置自签名证书](https://www.jianshu.com/p/4f738788be67)
- [Android okhttp3.0配置https的自签证书和信任所有证书](https://juejin.cn/post/6844903793096687630)
- [看完这篇文章后还不会windows下生成自签名https安全证书的话，你就打死我表弟](https://juejin.cn/post/6844904176284090381)

### 系统
- [Android权限之通知、自启动跳转](https://github.com/LoganZy/AndroidTotal/blob/master/Android%E6%9D%83%E9%99%90%E4%B9%8B%E9%80%9A%E7%9F%A5%E3%80%81%E8%87%AA%E5%90%AF%E5%8A%A8%E8%B7%B3%E8%BD%AC.md)
- [Showing a Snackbar from inside a Service](https://stackoverflow.com/questions/34863038/showing-a-snackbar-from-inside-a-service)
- [Android获得栈中最顶层的Activity](https://www.cnblogs.com/hello-studio/p/9640504.html)
- [Android获取前台进程的方法](https://www.cnblogs.com/fuyaozhishang/p/7442820.html)
- [Android后台执行定时任务](https://blog.csdn.net/qwer492915298/article/details/88533046)
- [Android后台执行定时任务](https://blog.csdn.net/weixin_40420578/article/details/103876900)
- [玩转Android10源码开发定制(十)增加获取当前运行最顶层的Activity命令 ](https://bbs.pediy.com/thread-264950.htm)
- [权限获取](https://programmer.help/blogs/5eb81619d9799.html)

### 其他
- [Android 泛型](https://www.jianshu.com/p/8d7b353ca94b)
- [Android 使用记录访问权限](https://blog.csdn.net/qq_24531461/article/details/67635016)
- [解决 Caused by: org.codehaus.groovy.control.MultipleCompilationErrorsException: startup failed:](https://www.jianshu.com/p/cd75a805ad60)

## V1.0.0版本开发规划 单机版本
### 功能清单
- [x] 任务管理模块
  - [x] 任务新增
  - [x] 任务删除
  - [x] 任务完成
  - [x] 任务查询

- [x] 手机应用日志模块
  - [x] 日志监控
  - [x] 日志保存
  - [x] 日志查询

- [x] 统计分析报表模块
  - [x] 今日日志实时统计
  - [x] 数据统计日报
  - [x] 任务统计周报
  - [x] 任务统计月报
  - [x] 任务统计年报

- [x] 其他的辅助模块
  - [x] 手机应用列表获取
  - [x] 手机应用绑定对应的标签

### 数据分析报表模块
对于每日得到的统计结果，可将其进行存储，下次获取时不用再次进行计算，因为前日的数据不会再发生变化

#### 数据统计周报
- [x] 数据总览
  - [x] 学习总时间
  - [x] 学习平均每天时间
  - [x] 运动总时间
  - [x] 运动平均每天时间
  - [x] 睡觉总时间
  - [x] 睡觉每天平均时间
  - [x] 完成任务总数

- [ ] 学习详情
  - [ ] 各APP使用时间，任务类型展示
  - [ ] 学习时间分布图表
  - [ ] 常用学习时段

- [ ] 运动详情
  - [ ] 运动时间分布图表
  - [ ] 常用运动时段

- [ ] 睡觉详情
  - [ ] 睡觉时间分布图表
  - [ ] 常用睡觉时间段

- [ ] 任务详情
  - [ ] 各个分类任务的完成数
  - [ ] 书籍类的列表展示

- [x] 学习、运动、睡觉次数柱状图
- [x] 24小时内各个时间内，学习、运动、睡觉时间柱状图