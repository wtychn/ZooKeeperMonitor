package com.example.demo.Watcher;

import com.example.demo.Utils.WebSocketServer;
import com.example.demo.controller.DemoTest;
import lombok.SneakyThrows;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class allNodeListWatcher {
    public static ZooKeeper zooKeeper;
    public static void watcherAllGetChild() throws Exception {
        // arg1:节点的路径
        // arg2:使用连接对象中的watcher
        getAllNode1 a=new getAllNode1();
        a.ls("/", DemoTest.zooKeeper);
        CountDownLatch countDownLatch=new CountDownLatch(1);
        zooKeeper=new ZooKeeper("122.51.129.180:2181", 50000, new Watcher() {
            @lombok.SneakyThrows
            @Override
            public void process(WatchedEvent event) {
                // 连接成功
                System.out.println("path=" + event.getPath());
                System.out.println("eventType=" + event.getType());
                System.out.println("节点数据已更新");
                System.out.println("---------------------------------------------------------");
                //zooKeeper.getChildren(event.getPath(),true);
                try {

                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
                        List<String> childrenList = zooKeeper.getChildren(event.getPath(), true);
                        System.out.println(a.directory.size());
                        for (int k = 0; k < childrenList.size(); k++) {
                            if (!a.directory.contains(childrenList.get(k))) {
                                if (event.getPath().equals("/")) {
                                    zooKeeper.getChildren(event.getPath() + childrenList.get(k), true);
                                    zooKeeper.exists(event.getPath() + childrenList.get(k), true);
                                    System.out.println("**********看我发送了在孩子孩子跟"+Thread.currentThread().getId());
                                } else {
                                    zooKeeper.getChildren(event.getPath() + "/" + childrenList.get(k), true);
                                    zooKeeper.exists(event.getPath() +"/"+ childrenList.get(k), true);
                                    System.out.println("**********看我发送了在孩子孩子"+Thread.currentThread().getId());
                                }


                            }



                        }
                        WebSocketServer.sendInfo("Rec Success!");
                    }
                    else if (event.getType()== Event.EventType.NodeDataChanged){
                        zooKeeper.exists(event.getPath(),true);
                        WebSocketServer.sendInfo("Rec Success!");
                        System.out.println("**********看我发送了在孩子数据"+Thread.currentThread().getId());

                    }
                    else if (event.getType()== Event.EventType.NodeDeleted){
                        WebSocketServer.sendInfo("Rec Success!");
                        System.out.println("**********看我发送了在孩子删除"+Thread.currentThread().getId());

                    }



                    a.directory.clear();
                    a.ls("/",zooKeeper);
                }

                catch (Exception ex){ex.printStackTrace();}
                countDownLatch.countDown();



            }
        });
        countDownLatch.await();
        for (int j = 0; j < a.directory.size(); j++) {
            zooKeeper.getChildren(a.directory.get(j),true);

        }
        System.out.println("监控线程1部署完毕");


        Thread.sleep(Integer.MAX_VALUE);
    }
}
