package com.example.demo.Serverwatcher;

import com.example.demo.Utils.WebSocketServer;
import com.example.demo.Utils.zookeeperUtils;
import com.example.demo.controller.DemoTest;
import lombok.SneakyThrows;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class NowServerWatcher {
    public static ZooKeeper zooKeeper;
    public static Boolean v=true;
    public static void nowServer() throws KeeperException, InterruptedException, IOException {
        // arg1:节点的路径
        // arg2:使用连接对象中的watcher
        CountDownLatch countDownLatch=new CountDownLatch(1);
        zooKeeper=new ZooKeeper(DemoTest.nowAddress, Integer.MAX_VALUE, new Watcher() {
            @SneakyThrows
            @Override
            public void process(WatchedEvent event) {
                System.out.println("连接对象的参数!");
                // 连接成功
                if (event.getState()== Event.KeeperState.Disconnected){

                    System.out.println("服务器下线");
                    v=false;
                    String[] temp=DemoTest.nowAddress.split(":");
                    if (zookeeperUtils.serverStatus(temp[0],temp[1])=="leader"){
                        System.out.println("集群leader节点已失去连接，可能进入选举状态");
                    }
                    // zooKeeper=new ZooKeeper("122.51.129.180:2181", Integer.MAX_VALUE,this::process);

                }
                if (event.getState()== Event.KeeperState.SyncConnected){
                    if (v==true){

                        System.out.println("服务器上线");
                    }
                    else {
                        WebSocketServer.sendInfo("Reconnected");
                        System.out.println("服务器重连");
                        v=true;}
                }

                countDownLatch.countDown();
                try {
                    zooKeeper.exists("/",true);

                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        });
        countDownLatch.await();



        zooKeeper.exists("/",true);


        Thread.sleep(Integer.MAX_VALUE);
        System.out.println("结束");
    }
}
