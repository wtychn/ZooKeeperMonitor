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
    @Value(value = "${zookeeper.address}")
    static String address;

    public static void watcherAllGetChild() throws Exception {
        // arg1:节点的路径
        // arg2:使用连接对象中的watcher
        GetAllNode1 a = new GetAllNode1();
        a.ls("/", ZookeeperController.zooKeeper);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        zooKeeper = new ZooKeeper(address, 50000, event -> {
            // 连接成功
            System.out.println("path=" + event.getPath());
            System.out.println("eventType=" + event.getType());
            System.out.println("节点数据已更新");
            System.out.println("---------------------------------------------------------");
            try {
                // 子节点变动
                if (event.getType().equals(Watcher.Event.EventType.NodeChildrenChanged)) {
                    List<String> childrenList = zooKeeper.getChildren(event.getPath(), true);
                    System.out.println(a.directory.size());
                    for (String s : childrenList) {
                        if (!a.directory.contains(s)) {
                            if (event.getPath().equals("/")) {
                                zooKeeper.getChildren(event.getPath() + s, true);
                                zooKeeper.exists(event.getPath() + s, true);
                                System.out.println("**********看我发送了在孩子孩子跟" + Thread.currentThread().getId());
                            } else {
                                zooKeeper.getChildren(event.getPath() + "/" + s, true);
                                zooKeeper.exists(event.getPath() + "/" + s, true);
                                System.out.println("**********看我发送了在孩子孩子" + Thread.currentThread().getId());
                            }
                        }
                    }
                    WebSocketServer.sendInfo("Rec Success!");

                    // 节点数据变动
                } else if (event.getType().equals(Watcher.Event.EventType.NodeDataChanged)) {
                    zooKeeper.exists(event.getPath(), true);
                    WebSocketServer.sendInfo("Rec Success!");
                    System.out.println("**********看我发送了在孩子数据" + Thread.currentThread().getId());

                    // 节点删除
                } else if (event.getType().equals(Watcher.Event.EventType.NodeDeleted)) {
                    WebSocketServer.sendInfo("Rec Success!");
                    System.out.println("**********看我发送了在孩子删除" + Thread.currentThread().getId());
                }
                a.directory.clear();
                a.ls("/", zooKeeper);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            countDownLatch.countDown();
        });
        countDownLatch.await();

        for (int j = 0; j < a.directory.size(); j++) {
            zooKeeper.getChildren(a.directory.get(j), true);
        }
        System.out.println("监控线程1部署完毕");

        Thread.sleep(Integer.MAX_VALUE);
    }
}
