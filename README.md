# AndroidOnlinePackage
一个以网页为入口可以切换分支，gradle编译，自动签名，自动对齐，一键pull代码，一键下载的Android打包编译工具

以Tomcat作为Web服务器，使用Gradle，JDK，Git等工具实现的自动打包，签名工具，提供了Linux和Windows两个版本的源代码，相关用法和设计思路请参考我的博客：http://blog.csdn.net/lvshaorong/article/details/51385537

![demo](https://github.com/AlexZhuo/AndroidOnlinePackage/blob/master/demo1.jpg)



使用前请先将代码库克隆到本地，并修改相关源代码指向本地的Android项目，并为每个分支单独建立一个Android项目目录，并在源码中修改其分支号。

并指定签名文件的目录，密码，key alais等等参数

修改git远程项目地址，并将用户名，密码拼在url中，方便pull代码

在demo中通过选择Live和Staging环境可以切换相关的java文件，当然你还可以根据前端的选择动态替换项目中的各种文件，包括普通文件或者数据库等等
