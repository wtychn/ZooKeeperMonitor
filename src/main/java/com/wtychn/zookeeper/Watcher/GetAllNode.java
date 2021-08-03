package com.wtychn.zookeeper.Watcher;

import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetAllNode {

    public static volatile List<String> directory = Collections.synchronizedList(new ArrayList<>());

    public static synchronized void ls(String path, ZooKeeper zooKeeper) throws Exception {
        directory.add(path);
        List<String> list = zooKeeper.getChildren(path, null);
        //判断是否有子节点
        if (list.isEmpty()) {
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
    }

}