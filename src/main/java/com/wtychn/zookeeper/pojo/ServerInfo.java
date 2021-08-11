package com.wtychn.zookeeper.pojo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "服务器信息")
public class ServerInfo {

    private String address;

    private String nodeRole;

    private String clusterState;

    private String nodeState;

}
