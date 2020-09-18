# MagicMirror
MagicMirror is an Android App that uses PSGAN's core technology to apply makeup to photos provided by users.


## 项目背景

###     网络上存在着各种各样的美妆教程、化妆品宣传广告，这确实为消费者提供了大量的信息，但也让消费者面临更多的问题：哪一种妆容更适合自己、肤色不够好会不会影响这个化妆品的效果等。试妆魔镜可以根据示例图片在素颜图的基础上生成化妆后的照片。用户可以根据算法生成的照片判断这种某一种妆容是否适合自己。相比于现在的美颜软件，我们的用户可以选择任意一种想要模仿的美妆，示例图的化妆风格不受限制，生成的化妆照片看起来也更真实。

![image](https://github.com/Super262/MagicMirror/blob/master/screenshots/1.png)
App上妆界面如图 1 所示。

```
图 1 上妆界面和效果
```
## 个人分工

    设计App的主要功能

    App项目基础框架设计和开发

    App网络请求框架设计和开发

    App相机组件设计和开发


## 主要工作

1. 设计App的主要功能
    1) 需求分析
       我们想为用户提供一个体验、分享不同上妆效果的平台。用户可以对图片进行上妆。用户可以与其他用户共享自己的妆容图。
    2) 功能设计
       程序的主要功能是登录注册功能、示例榜、上妆界面、个人信息、上传示例。
       用户通过手机号注册新账号并登录，如图 2 所示。用户可以在示例榜看到其他用户上传的所有美妆图片，如图 3 所示。用户可以在上妆界面上妆，支持调节妆容的浓淡和局部上妆，如图 3 所示。用户可以在个人信息页面查看示例收藏、上妆历史，可以上传示例，如图 4 所示。用户可以在个人信息页面更改用户设置，如图 4 所示。
       
![image](https://github.com/Super262/MagicMirror/blob/master/screenshots/2.png)
```
图 2 注册和登录
```

![image](https://github.com/Super262/MagicMirror/blob/master/screenshots/3.png)
```
图 3 示例榜 上妆界面
```

![image](https://github.com/Super262/MagicMirror/blob/master/screenshots/4.png)
```
图 4 个人信息页面 示例收藏 美妆历史 上传示例 账号设置
```
2. App项目基础框架设计和开发
    为了提高代码的易读性，提高项目整体的健壮性，我将项目分成了 5 个模块，如图5 所示。
![image](https://github.com/Super262/MagicMirror/blob/master/screenshots/5.png)
```
图 5 项目模块分解
```
```
1) 注解Module
名称：annotations，类型：Java Library；
自定义的注解。
```
```
2) 代码生成器Module
名称：compiler，类型：Java Library；
根据自定义的注解生成相应的代码。
```
```
3) 核心Module
名称：core，类型：Android Library；
App最核心的、通用的功能。
```
```
4) 业务Module
名称：ec，类型： Android Library；
App上妆业务的特有功能。
```
```
5) 具体项目Module
```

```
名称：app，类型：Android Application；
调用上妆业务，完成相关处理。
```
### 在完成项目架构设计和模块分解后，我开始搭建项目基础框架。这包括开发一个全局配置器和集成字体图标库。

    在设计全局配置器（类Configurator）时，我依照“单例模式”下的“懒汉模式”，确保全局配置一致，并且不会为维持一致性造成过大的空间占用。我使用Java下的私有构造方法和私有内部类实现这一目标，如图 6 所示。
![image](https://github.com/Super262/MagicMirror/blob/master/screenshots/6.png)
```
图 6 私有构造方法和私有内部类
```
    我选择使用字体图标来代替传统的图像图标，这是因为字体图标基于SVG，在分辨率变化时图标不会失真。我集成了android-iconify，可以使用Font Awesome作为图标源。字体图标和文字可以同在一个文本框，如图 7 所示。
![image](https://github.com/Super262/MagicMirror/blob/master/screenshots/6.1.png)
```
图 7 字体图标和文字同在一个文本框
```
    完成项目基础框架后，我设计并开发了App的界面架构。我设计了一个单Activity、多Fragment的通用UI框架，界面切换十分流畅。为此我设计了 6 个抽象类作为所有页面的父类，它们分别是ProxyActivity（所有Activity的父类），BaseDelegate（所有页面的父类），BaseBottomDelegate（具有底部导航栏的首页的父类），BottomItemDelegate（由底部导航栏的按钮驱动的页面的父类），PermissionCheckerDelegate（动态权限申请页面的父类，适配Android 6.0+），QiluDelegate（常规页面的父类）。这些抽象类包含了一些通用功能，如动态权限申请、加载动画、底部导航菜单栏，如图 8 所示。
![image](https://github.com/Super262/MagicMirror/blob/master/screenshots/7.png)
```
图 8 动态权限申请 加载动画 底部导航栏
```

3. App网络请求框架设计和开发
    为了提高后续开发的效率，我设计并开发了一个高效易用的网络请求框架。开发者可以轻松地使用这个框架发送并处理RESTful 请求。框架本身会自动处理多线程和异步的相关问题。例如，开发者可以使用这个框架上传一个或多个文件，如图 9 所示。
![image](https://github.com/Super262/MagicMirror/blob/master/screenshots/8.png)
```
图 9 开发者使用框架上传文件
```
```
    这个网络请求框架基于Retrofit2构建。框架设计遵循“建造者模式”，即将一个复杂的对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示。框架为开发者提供了简洁、高效的函数调用接口，具有相当完善的回调（Interface）。框架本身的项目结构和回调接口如图 10 所示，callback包下是所有的回调。
```
![image](https://github.com/Super262/MagicMirror/blob/master/screenshots/9.png)
```
图 10 源代码结构
```

4. App相机组件设计和开发
    App的核心功能是对照片进行上妆，因此，我设计了功能齐全、高效可靠的图片处理界面，具有图片裁剪、旋转、压缩功能，如图 11 所示。
![image](https://github.com/Super262/MagicMirror/blob/master/screenshots/10.png)
```
图 11 图片处理界面
```
```
在用户拍摄照片前，App会进行动态权限申请（适配Android 6.0+），如图 12 所示。
```
![image](https://github.com/Super262/MagicMirror/blob/master/screenshots/11.png)
```
图 12 拍照前的动态权限申请
```
```
App支持立即拍摄照片或从本地选择照片，如图 13 所示。
```
![image](https://github.com/Super262/MagicMirror/blob/master/screenshots/12.png)
```
图 13 立即拍照或选择本地照片
```

## 实训心得

    在本次实训中，我第一次体验长时间的团队合作编程，这也是我第一次频繁地使用Git工具来实现代码同步。实训刚开始时，我们并未立即使用Git工具，因为我们不是很熟练。实训开始 2 周后，我们已经可以熟练使用Git工具和GitHub平台，我们大部分的commit操作也是在这时完成的。通过学习并熟练使用Git来实现代码同步，我渐渐适应了团队分工合作完成项目的工作模式，这对我以后的学习和工作颇有益处。设计App的底层框架是一件费心费时的事情，相比于我用于编码的时间，我大部分的时间都花在了构思上。在这个过程中，我一步步地解决复杂的问题，一点点地设计顶层的抽象类，再逐步到下层的具体功能，不断地修改，最终形成了一个简单易用的框架。我第一次深刻体会到巧用Java语言的继承所带来的便利性，也对Java语言的编程规则有了更深刻的理解。我也意识到“类多代码少”的重要性。
