package com.wtychn.zookeeper.Watcher;

import com.wtychn.zookeeper.Utils.WebSocketServer;
import com.wtychn.zookeeper.controller.ZookeeperController;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class NowServerWatcher {
    public static ZooKeeper zooKeeper;
    public static Boolean v = true;

//    public static void nowServer() throws KeeperException, InterruptedException, IOException {
//        // arg1:节点的路径
//        // arg2:使用连接对象中的watcher
//        CountDownLatch countDownLatch = new CountDownLatch(1);
//        zooKeeper = new ZooKeeper(ZookeeperController.nowAddress, Integer.MAX_VALUE, event -> {
//            System.out.println("连接对象的参数!");
//            // 连接成功
//            if (event.getState() == Watcher.Event.KeeperState.Disconnected) {
//
//                System.out.println("服务器下线");
//                v = false;
//                String[] temp = ZookeeperController.nowAddress.split(":");
//                try {
//                    if (Objects.equals(ZookeeperUtils.serverStatus(temp[0], temp[1]), "leader")) {
//                        System.out.println("集群leader节点已失去连接，可能进入选举状态");
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                // zooKeeper=new ZooKeeper("122.51.129.180:2181", Integer.MAX_VALUE,this::process);
//
//            }
//            if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
//                if (v) {
//                    System.out.println("服务器上线");
//                } else {
//                    try {
//                        WebSocketServer.sendInfo("Reconnected");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("服务器重连");
//                    v = true;
//                }
//            }
//
//            countDownLatch.countDown();
//            try {
//                zooKeeper.exists("/", true);
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//
//        });
//        countDownLatch.await();
//
//        zooKeeper.exists("/", true);
//
//        Thread.sleep(Integer.MAX_VALUE);
//        System.out.println("结束");
//    }
}
