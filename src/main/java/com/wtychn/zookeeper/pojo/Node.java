package com.wtychn.zookeeper.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "znode 节点信息")
public class Node {

    private String address;
    private String path;
    private String value;
    private String showPath;

    public Node(String address, String path, String value, String showPath) {
        this.address = address;
        this.path = path;
        this.value = value;
        this.showPath = path.substring(path.lastIndexOf("/"));
    }
}
