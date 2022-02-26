# Self Growth 自我生成手机应用使用情况统计Android客户端
***

**自我生长：成为更好的自己**

## 简介
用户获取手机当前的正在使用的应用，将其上报到服务端

## 功能简介
客户端主要功能定位是：

- [x] 收集手机当前应用使用情况
- [ ] 登录集成
    - [ ] 手机号码一键登录
    - [ ] 网页端扫描登录
- [ ] 今日当前截止手机应用使用情况
- [x] 任务列表

核心功能主要是能收集当前手机正在使用的APP情况，上报到服务端

更多的管理和展示，目前规划到Web端

## 手机应用情况本地存储统计策略
### 活动
频率：每十秒记录一次当前的顶层手机应用Activity

放入文件中，文件中约定的格式如下：

```text
时间戳 时间日期（yyyy-mm-dd HH:MM:SS格式） activity名称
```

每天都有一个实时的用于记录更新的文件：date_current.log

- date:获取当前时间日期，到天，如 2021_02_21,注意，22点后按照第二天算，也就是不是0点到第二天，而是10点到第二天，恩，早点睡觉比较好
- current：用于实时更新的标识
- 一天最多有：10 * 60 * 24 = 14400条数据
- 22点后第一次记录时，复制当前文件到离线文档记录，清空当前文件所用内容
- 22点后自动进行一次当天数据统计，将情况进行汇总，记录下总的时间和各个记录，详情的结构请参考类：{@link DashboardStatistics}
- 每次记录，进行一次实时统计，并将统计结果保存，在应用重启后可以进行加载（如果数据不对，需要提供按钮，进行数据重新统计）

离线活动记录文档：date_save.log 最多一周的，每天22点进行一次清理，将老的日志清理调

当天统计的就读current.log,历史统计查看就读当前的

### 任务

## 参考链接
### 资源
- [Awesome Android](https://github.com/JStumpp/awesome-android)
- [Android DOCUMENTATION](https://developer.android.com/reference)
- [字节开源的，Apache2 使用起来放心点：IconPark](https://github.com/bytedance/iconpark)
- [iconparkBETA](https://iconpark.oceanengine.com/official)
- [有哪些无版权、免费、高清图片素材网站？](https://www.zhihu.com/question/318961106/answer/767074512)
- [官方Vector资源:res文件夹右键也可以添加资源](https://fonts.google.com/icons?selected=Material+Icons)

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

- [ ] 统计分析报表模块
  - [x] 今日日志实时统计
  - [x] 数据统计日报
  - [ ] 任务统计周报
  - [ ] 任务统计月报
  - [ ] 任务统计年报

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

- [ ] 学习、运动、睡觉次数柱状图
- [ ] 24小时内各个时间内，学习、运动、睡觉时间柱状图