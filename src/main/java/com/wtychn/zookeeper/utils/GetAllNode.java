package com.wtychn.zookeeper.utils;

import java.util.ArrayList;
import java.util.List;

public class GetAllNode {
    List<String> directory = new ArrayList<>();

    public List<String> ls(String path) throws Exception {
        listChildren(path);
        return directory;
    }

    private void listChildren(String path) throws Exception {
        directory.add(path);
        List<String> list = ZooKeeperUtil.client
                .getChildren()
                .forPath(path);
        //判断是否有子节点
        if (list.isEmpty()) {
            return ;
        }
        for (String s : list) {
            //判断是否为根目录
            if (path.equals("/")) {
                listChildren(path + s);
            } else {
                listChildren(path + "/" + s);
            }
        }
    }
}