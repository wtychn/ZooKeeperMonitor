# Zookeeper 监控项目文档

## 1. 需求及完成度

利用API实现一个zookeeper内容浏览器(客户端方式或浏览器方式均可)

- [x] 能够连接到zookeeper集群并可视化展示节点数据；
- [x] 能够通过界面修改zookeeper节点数据；
- [x] 能够通过事件监听方式及时刷新节点信息（不允简单许轮询实现）；
- [x] 能够主动发现zookeeper集群的动态变化（重新选举、增加或者减少节点，节点故障等）；

## 2. 接口及参数

| 接口名      | HTTP 方法 | 参数                      | 参数传递方式 | 状态 | 备注                    |
| ----------- | --------- | ------------------------- | ------------ | ---- | ----------------------- |
| /servers    | get       |                           |              | 完成 | 获取 zk 服务器节点信息  |
| /serverTree | get       | String addresses          | URL 拼接     | 完成 | 获取 zookeeper 节点信息 |
| /quit       | delete    |                           |              | 完成 | 断开当前集群连接        |
| /node       | get       | String path               | URL 拼接     | 完成 | 查询节点信息            |
| /node       | post      | Node node                 | 表单数据     | 完成 | 新增节点                |
| /node       | delete    | String path               | URL 拼接     | 完成 | 删除节点                |
| /node       | put       | String path, String value | 表单数据     | 完成 | 更新节点                |

## 3. 实现方法

项目后端部分采用 SpringBoot 框架进行搭建，zk 客户端使用 Curator 框架实现；

项目前端部分采用基于 Vue 的 Element-UI 框架实现。

### 3.1 项目目录

```java
├─src
   ├─main
   │  ├─java
   │  │  └─com.wtychn.zookeeper //主要代码
   │  │     ├─config
   │  │     ├─controller 
   │  │     ├─pojo
   │  │     ├─service
   │  │     │  └─impl
   │  │     ├─utils
   │  │     └─watcher
   │  └─resources //主要存放 SpringBoot 配置文件      
   └─test //测试文件

```

本项目就是常规的 SpringBoot 项目结构。

### 3.2 znode CRUD

znode 增删改查的实现通过 Curator 客户端实现，封装在 `NodeServiceImpl`类中，在`ZookeeperController`中通过 Restful 风格接口与前端进行交互。

### 3.3 znode 监听（NodeWatcher）

znode 监听采用 Curator 的 TreeCache 监听器实现，能够监听整个节点树的节点增删改变化。具体实现方式：

1. 在前端输入 zk 集群地址，后端根据地址创建 Curator 客户端并与 zk 集群建立连接，同时在根节点上设置监听器监听所有 znode 的变化；
2. 监听到节点变化时，后端程序通过 webSocket 向前端发送信息，使前端调用 /serverTree 接口更新 znode 数据。

### 3.4 zk 集群变化监听（NowServerWatcher）

zk 集群节点通过 SpringBoot 定时任务和 zk 自身的 stat 工具实现。具体流程是：

1. 后端每隔 5 秒对集群的节点进行访问，更新每个节点的状态，包括节点角色，连接状态；
2. 发现节点状态发生变化时通过 webSocket 向前端发送消息，使前端进行提示并调用 /servers 接口更新节点状态。

### 3.5 客户端连接监听（SessionConnectionWatcher）

客户端与 zk 集群的连接通过 Curator 的 ConnectionStateListener 实现。监听包含两方面：

1. 监听部署时会在 zk 集群中加入一个临时节点，节点值设为客户端地址；临时节点在客户端断开连接时会自动删除，这样能够在集群中查看正在连接的客户端；
2. 当监听器监听到连接断开时会自动重连并创建临时节点。

