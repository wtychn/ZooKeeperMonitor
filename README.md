# Zookeeper 监控项目文档

## 1. 需求及完成度

利用API实现一个zookeeper内容浏览器(客户端方式或浏览器方式均可)

- [x] 能够连接到zookeeper集群并可视化展示节点数据；
- [x] 能够通过界面修改zookeeper节点数据；
- [x] 能够通过事件监听方式及时刷新节点信息（不允简单许轮询实现）；
- [ ] 能够主动发现zookeeper集群的动态变化（重新选举、增加或者减少节点，节点故障等）；

## 2. 接口及参数

| 接口名      | HTTP 方法 | 参数                      | 参数传递方式 | 状态       | 备注                    |
| ----------- | --------- | ------------------------- | ------------ | ---------- | ----------------------- |
| /serverTree | get       | String addresses          | URL 拼接     | 完成       | 获取 zookeeper 节点信息 |
| /quit       | delete    |                           |              | 前端待完善 | 断开当前集群连接        |
| /node       | get       | String path               | URL 拼接     | 完成       | 查询节点信息            |
| /node       | post      | Node node                 | 表单数据     | 完成       | 新增节点                |
| /node       | delete    | String path               | URL 拼接     | 完成       | 删除节点                |
| /node       | put       | String path, String value | 表单数据     | 完成       | 更新节点                |

