package com.example.demo.Watcher;

import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class getAllNode1 {
    public  volatile List<String> directory= Collections.synchronizedList(new ArrayList<>());
    public  synchronized void ls(String path, ZooKeeper zooKeeper) throws Exception {
        //System.out.println(path);
        directory.add(path);
        List<String> list = zooKeeper.getChildren(path, null);
        //判断是否有子节点
        if (list.isEmpty() || list == null) {
            return;
        }
        for (String s : list) {
            //判断是否为根目录
            if (path.equals("/")) {
                ls(path + s, zooKeeper);
            } else {
                ls(path + "/" + s, zooKeeper);
            }
        }
        return;
    }

}