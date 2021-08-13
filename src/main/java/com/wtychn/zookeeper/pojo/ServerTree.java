package com.wtychn.zookeeper.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value = "服务器树节点")
public class ServerTree {

    private String label;
    private List<ServerTree> children = new ArrayList<>();
    private int id;
    private int parentId;
    private String path;

    public ServerTree(String path, int id) {
        this.path = path;
        this.id = id;
        label = path.substring(path.lastIndexOf("/"));
    }

}
