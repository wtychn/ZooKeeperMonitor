package com.example.demo.Watcher;

import com.example.demo.Utils.WebSocketServer;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class nodeDataChangedWatcher {
    public static ZooKeeper zooKeeper;
    public static AtomicInteger i=new AtomicInteger(0);
    public static void watcherData() throws KeeperException, InterruptedException, IOException {
        // arg1:节点的路径
        // arg2:使用连接对象中的watcher
        zooKeeper=new ZooKeeper("122.51.129.180:2181", 50000, new Watcher() {
            @lombok.SneakyThrows
            @Override
            public void process(WatchedEvent event) {
                System.out.println("连接对象的参数!");
                // 连接成功
                System.out.println("path=" + event.getPath());
                System.out.println("eventType=" + event.getType());
                System.out.println("节点数据已更新");
                if (event.getType()== Event.EventType.NodeDataChanged){
                    zooKeeper.exists(event.getPath(),true);
                    WebSocketServer.sendInfo("Rec Success!");}

            }
        });
        i.set(0);
        for (int j = 0; j < getAllNode.directory.size()-1; j++) {
            i.set(j);
            zooKeeper.exists(getAllNode.directory.get(i.get()),true);

        }


        Thread.sleep(Integer.MAX_VALUE);
        System.out.println("结束");
    }
}
