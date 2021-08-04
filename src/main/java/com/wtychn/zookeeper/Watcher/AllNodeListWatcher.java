package com.wtychn.zookeeper.Watcher;

import com.wtychn.zookeeper.Utils.WebSocketServer;
import com.wtychn.zookeeper.controller.ZookeeperController;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class AllNodeListWatcher {
    public static ZooKeeper zooKeeper;

    public static void watcherAllGetChild() throws Exception {
        // arg1:节点的路径
        // arg2:使用连接对象中的watcher
        GetAllNode1 nodeList = new GetAllNode1();
        nodeList.ls("/", ZookeeperController.zooKeeper);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        zooKeeper = new ZooKeeper(ZookeeperController.nowAddress, 50000, event -> {
            // 连接成功
            System.out.println("path=" + event.getPath());
            System.out.println("eventType=" + event.getType());
            System.out.println("节点数据已更新");
            System.out.println("---------------------------------------------------------");
            try {
                // 子节点变动
                if (event.getType().equals(Watcher.Event.EventType.NodeChildrenChanged)) {
                    List<String> childrenList = zooKeeper.getChildren(event.getPath(), true);
                    System.out.println("共有" + nodeList.directory.size() + "个子节点");
                    for (String s : childrenList) {
                        if (!nodeList.directory.contains(s)) {
                            if (event.getPath().equals("/")) {
                                zooKeeper.getChildren(event.getPath() + s, true);
                                zooKeeper.exists(event.getPath() + s, true);
                                System.out.println(Thread.currentThread().getId() + "线程检测到根节点的子节点变化！");
                            } else {
                                zooKeeper.getChildren(event.getPath() + "/" + s, true);
                                zooKeeper.exists(event.getPath() + "/" + s, true);
                                System.out.println(Thread.currentThread().getId() + "线程检测到" + s + "节点子节点变化！");
                            }
                        }
                    }
                    WebSocketServer.sendInfo("Rec Success!");

                    // 节点数据变动
                } else if (event.getType().equals(Watcher.Event.EventType.NodeDataChanged)) {
                    zooKeeper.exists(event.getPath(), true);
                    WebSocketServer.sendInfo("Rec Success!");
                    System.out.println(Thread.currentThread().getId() + "线程检测到节点数据变化！");

                    // 节点删除
                } else if (event.getType().equals(Watcher.Event.EventType.NodeDeleted)) {
                    WebSocketServer.sendInfo("Rec Success!");
                    System.out.println(Thread.currentThread().getId() + "线程检测到节点删除！");
                }
                nodeList.directory.clear();
                nodeList.ls("/", zooKeeper);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            countDownLatch.countDown();
        });
        countDownLatch.await();

        for (int j = 0; j < nodeList.directory.size(); j++) {
            zooKeeper.getChildren(nodeList.directory.get(j), true);
        }
        System.out.println("监控线程1部署完毕");

        Thread.sleep(Integer.MAX_VALUE);
    }
}
