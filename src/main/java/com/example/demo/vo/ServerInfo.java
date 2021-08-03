package com.example.demo.vo;

public class ServerInfo {

    private String adress;

    private String nodeRole;

    private String clusterState;

    private String nodeState;

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getNodeRole() {
        return nodeRole;
    }

    public void setNodeRole(String nodeRole) {
        this.nodeRole = nodeRole;
    }

    public String getClusterState() {
        return clusterState;
    }

    public void setClusterState(String clusterState) {
        this.clusterState = clusterState;
    }

    public String getNodeState() {
        return nodeState;
    }

    public void setNodeState(String nodeState) {
        this.nodeState = nodeState;
    }

    public ServerInfo() {
    }

    public ServerInfo(String adress, String nodeRole, String clusterState, String nodeState) {
        this.adress = adress;
        this.nodeRole = nodeRole;
        this.clusterState = clusterState;
        this.nodeState = nodeState;
    }
}
