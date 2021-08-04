package com.wtychn.zookeeper.service.impl;

import com.wtychn.zookeeper.Utils.ZookeeperUtils;
import com.wtychn.zookeeper.Watcher.ChildrenNodeWatcherThread;
import com.wtychn.zookeeper.Watcher.NodeDataChangedWatcherThread;
import com.wtychn.zookeeper.Watcher.GetAllNode;
import com.wtychn.zookeeper.controller.ZookeeperController;
import com.wtychn.zookeeper.pojo.CommonResult;
import com.wtychn.zookeeper.pojo.ServerInfo;
import com.wtychn.zookeeper.pojo.ServerTree;
import com.wtychn.zookeeper.service.ServerService;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
public class ServerServiceImpl implements ServerService {

    public static Thread getNodeChangedWatcherThread;
    public static Thread childrenNodeWatcherThread;

    @Override
    public CommonResult getServerList(String addresses) throws IOException, InterruptedException {
        String[] addressList = addresses.split(",");
        List<ServerInfo> serverlist = new ArrayList<>();
        for (int i = 0; i < addressList.length; i++) {
            serverlist.add(new ServerInfo());
            serverlist.get(i).setAdress(addressList[i]);
            serverlist.get(i).setClusterState("normal");
            String[] hostPort = addressList[i].split(":");
            // 计数器对象
            CountDownLatch countDownLatch = new CountDownLatch(1);
            int curIdx = i;
            new ZooKeeper(addressList[curIdx], 5000, event -> {
                if (event.getState().equals(Watcher.Event.KeeperState.SyncConnected)) {
                    serverlist.get(curIdx).setNodeState("on service");
                    if (event.getState().equals(Watcher.Event.KeeperState.Disconnected)) {
                        serverlist.get(curIdx).setNodeState("off line");
                    }
                    countDownLatch.countDown();
                }
            });
            // 主线程阻塞等待连接对象的创建成功
            countDownLatch.await();

            serverlist.get(i).setNodeRole(ZookeeperUtils.serverStatus(hostPort[0], hostPort[1]));
        }

        CommonResult commonResult = new CommonResult();
        commonResult.setCode(200);
        commonResult.setMsg("Success");
        commonResult.setData(serverlist);

        return commonResult;
    }

    @Override
    public CommonResult getServerTree(String address) throws Exception {
        try {
            // 计数器对象
            CountDownLatch countDownLatch = new CountDownLatch(1);
            if (ZookeeperController.zooKeeper == null) {

                ZookeeperController.zooKeeper = new ZooKeeper(address, 50000, event -> {
                    if (event.getState().equals(Watcher.Event.KeeperState.SyncConnected)) {
                        System.out.println("连接创建成功!");
                        countDownLatch.countDown();
                    }
                });
                // 主线程阻塞等待连接对象的创建成功
                countDownLatch.await();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ZookeeperUtils.directory.clear();

        assert ZookeeperController.zooKeeper != null;
        GetAllNode.ls("/", ZookeeperController.zooKeeper);
        ZookeeperUtils.ls("/", ZookeeperController.zooKeeper);
        System.out.println("===================================");
        ServerTree root = transfer(ZookeeperUtils.directory);

        if (childrenNodeWatcherThread == null) {
            childrenNodeWatcherThread = new ChildrenNodeWatcherThread();
            childrenNodeWatcherThread.start();
        }
        if (getNodeChangedWatcherThread == null) {
            getNodeChangedWatcherThread = new NodeDataChangedWatcherThread();
            getNodeChangedWatcherThread.start();
        }

        ZookeeperUtils.directory.clear();
        List<ServerTree> resData = new ArrayList<>();
        resData.add(root);
        CommonResult commonResult = new CommonResult();
        commonResult.setCode(200);
        commonResult.setMsg("Success");
        commonResult.setData(resData);
        return commonResult;
    }

    // 节点字符串列表 -> 列表树
    public ServerTree transfer(List<String> list) {
        List<ServerTree> tree = new ArrayList<>();
        if (list.size() == 0) return null;
        ServerTree root = new ServerTree("/", 0);

        tree.add(root);
        for (int k = 1; k < list.size(); k++) {
            tree.add(new ServerTree(list.get(k), k));
        }
        for (int i = 0; i < list.size(); i++) {
            String parent = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(j).contains(parent) &&
                        !(list.get(j).substring(parent.length() + 1).contains("/"))) {
                    tree.get(i).getChildren().add(tree.get(j));
                    tree.get(j).setParentId(i);
                }
            }
        }
        return root;
    }

}
