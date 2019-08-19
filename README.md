## 基于Flink的关键字过滤预警系统
该项目用于监控日志数据流，管理警报规则，警报阈值和频率，配置成功实时生效。
实现基于Apache Flink，Spring Boot，Alibaba Nacos，MySQL和JQuery。

------

[TOC]

#### **Important**: 
目前只是最最基本刚刚可以跑起来的版本，还有很多设计不合理要改进的地方，开源是为了分享做项目使用Flink的一些思路，个人认为有几点较为值得参考，本人实测可用：

- 自定义的HttpSink、作为Socket服务端接收数据 :ServerSockSource

- Alibaba Nacos可以给算子绑定事件监听，实时改变算子规则(原理是长连接和java的事件机制)

- Flink集群中一个Job执行实时Map操作把结果Socket发给另一个Job使用时间窗口聚合

  #### **Important**:本项目有几个大坑:
- 使用MySQL：因为探索阶段使用Spring+JPA的组合构建后端，但引入Nacos后，MySQL显得鸡肋完全可以用Nacos替代

- 关于前端：前端JS代码极度反人类，使用jQuery从数据构建页面，是因为此项目必须作为Grafana插件设计所以前端技术受限.目前无法把Grafana一并上传，前端代码脱离Grafana可以直接运行，我己经手动修改为纯html版本配置好spring和mysql即可打开

------

## 设计思路
#### 整体流程：
![图片](https://i.loli.net/2019/08/19/oE5ByLQOkhPApzb.png)

#### 各个模块UML图：
Spring Boot RESTful API Service
![图片](https://i.loli.net/2019/08/19/iVcxB1eoYCJsvld.png)

Flink集群
![图片](https://i.loli.net/2019/08/19/jNbg4MsGIhzJlVd.png)

SpringBoot报警模块

![图片](https://i.loli.net/2019/08/19/Z8Mwfvd6xcgVozR.png)

Grafana下前端配置主界面：

![图片](https://i.loli.net/2019/08/19/yQRvPqnGgZulNbE.jpg)



-----

### 关于运行：

**Important**:因为是个人实习项目,用了很多技术栈，Fork直接跑的话不是很方便，建议仅参考思路
水平有限，还有很多设计不合理要改进的地方
记录下本项目运行所需要的配置：
运行需要在Spring Boot配置MySQL连接字符串

#### MySQL SQL
```
CREATE TABLE `jobs` (
  `jobid` int(11) NOT NULL AUTO_INCREMENT,
  `jobname` varchar(32) DEFAULT NULL,
  `rules` text NOT NULL,
  `hostname` varchar(32) DEFAULT NULL,
  `port` varchar(32) DEFAULT NULL,
  `is_zip` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`jobid`)
) ENGINE=InnoDB AUTO_INCREMENT=1005 DEFAULT CHARSET=utf8;

CREATE TABLE `rules` (
  `RuleID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Content` text,
  PRIMARY KEY (`RuleID`)
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8;;

```
#### Nacos配置
```
Data ID:FlinkJob.1001.properties
Group:BaseService
Configuration Content:
{"id":1001,"name":"缓存(Memcached)(9 panels)","isZip":"-1","keys":["Succeed","SNNN"],"hostname":"127.0.0.1","port":9000}
```
```
Data ID:Flink.AlarmRuleHashMap
Group:BaseService
Configuration Content:
{"1001Succeed":{"jobid":1001,"counts":15,"timewindow":5,"rulecontent":"Succeed","ruleid":91},"1001SNNN":{"counts":15,"jobid":1001,"rulecontent":"SNNN","ruleid":96,"timewindow":5}}
```
#### 运行参数
Flink Counter:
计数任务开启的端口 + Spring服务的URL
例子：
```
9001 http://127.0.0.1:8080
```
Flink Filter:
Nacos的URL+ 任务的ID+ SocketSink的地址+SocketSink的端口
```
127.0.0.1:8848 1001 127.0.0.1 9001
```
Spring Boot:
```
$java -jar flink.config-1.0-SNAPSHOT.jar --server.port=8080
```
-------

## 最后

喜欢的欢迎Star💗

交流技♂术可以加我的QQ：491929461

本人应届秋招，有内推的爸爸们捞我一把，谢谢惹



