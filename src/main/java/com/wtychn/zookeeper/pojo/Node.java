package com.wtychn.zookeeper.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
