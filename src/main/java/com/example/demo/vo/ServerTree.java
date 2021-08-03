package com.example.demo.vo;

import java.util.List;

public class ServerTree {

    private String label;

    private List<ServerTree> children;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<ServerTree> getChildren() {
        return children;
    }

    public void setChildren(List<ServerTree> children) {
        this.children = children;
    }

    public ServerTree(String label, List<ServerTree> children) {
        this.label = label;
        this.children = children;
    }

    public ServerTree() {
    }
}
