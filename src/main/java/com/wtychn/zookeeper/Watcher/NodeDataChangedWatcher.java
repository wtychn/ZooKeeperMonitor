package com.wtychn.zookeeper.Watcher;

import com.wtychn.zookeeper.Utils.WebSocketServer;
import lombok.SneakyThrows;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class NodeDataChangedWatcher {
    public static ZooKeeper zooKeeper;
    public static AtomicInteger i = new AtomicInteger(0);
    @Value(value = "${zookeeper.address}")
    static String address;

    public static void watcherData() throws KeeperException, InterruptedException, IOException {
        // arg1:节点的路径
        // arg2:使用连接对象中的watcher
        zooKeeper = new ZooKeeper(address, 50000, event -> {
            System.out.println("连接对象的参数!");
            // 连接成功
            System.out.println("path=" + event.getPath());
            System.out.println("eventType=" + event.getType());
            System.out.println("节点数据已更新");
            if (event.getType().equals(Watcher.Event.EventType.NodeDataChanged)) {
                try {
                    zooKeeper.exists(event.getPath(), true);
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    WebSocketServer.sendInfo("Rec Success!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        i.set(0);
        for (int j = 0; j < GetAllNode.directory.size() - 1; j++) {
            i.set(j);
            zooKeeper.exists(GetAllNode.directory.get(i.get()), true);
        }

        Thread.sleep(Integer.MAX_VALUE);
        System.out.println("结束");
    }
}
