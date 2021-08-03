# Zookeeper 监控项目文档

## 接口及参数

| 接口名           | HTTP 方法      | 参数                                 | 状态 | 备注                                |
| ---------------- | -------------- | ------------------------------------ | ---- | ----------------------------------- |
| /retestadd       | post           |                                      | 弃用 | A122.51.129.180:2182 websocket 测试 |
| /retestmin       | post           |                                      | 弃用 | M122.51.129.180:2182 websocket 测试 |
| /server          | post -> get    | ServerLogin 对象 -> String addresses | 修改 | 获取 zookeeper 服务器信息           |
| /serverTree      | post -> get    | String addresses                     | 修改 | 获取 zookeeper 节点信息             |
| /query -> /node  | post -> get    | String addresses                     | 修改 | 查询节点信息                        |
| /add -> /node    | post -> get    | String addresses                     | 修改 | 新增节点                            |
| /delete -> /node | post -> delete | String addresses                     | 修改 | 删除节点                            |
| /modify -> /node | post -> put    | String addresses                     | 修改 | 更新节点                            |

