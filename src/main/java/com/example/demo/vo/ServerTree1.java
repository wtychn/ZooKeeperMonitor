package com.example.demo.vo;

import java.util.ArrayList;
import java.util.List;

public class ServerTree1 {


    private String label;
    private List<ServerTree1> children = new ArrayList<>();
    private int id;
    private int parentid;


    public ServerTree1(String label,int id) {
        this.label = label;
        this.id=id;

    }

    public ServerTree1() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<ServerTree1> getChildren() {
        return children;
    }

    public void setChildren(List<ServerTree1> children) {
        this.children = children;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }
}
