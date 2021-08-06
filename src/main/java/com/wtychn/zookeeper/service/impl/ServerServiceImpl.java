package com.wtychn.zookeeper.service.impl;

import com.wtychn.zookeeper.Utils.GetAllNode;
import com.wtychn.zookeeper.Utils.ZooKeeperUtil;
import com.wtychn.zookeeper.controller.ZookeeperController;
import com.wtychn.zookeeper.pojo.CommonResult;
import com.wtychn.zookeeper.pojo.ServerInfo;
import com.wtychn.zookeeper.pojo.ServerTree;
import com.wtychn.zookeeper.service.ServerService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServerServiceImpl implements ServerService {

    @Override
    public CommonResult getServerList(String addresses) throws IOException, InterruptedException {
//        String[] addressList = addresses.split(",");
        List<ServerInfo> serverlist = new ArrayList<>();
//        for (int i = 0; i < addressList.length; i++) {
//            serverlist.add(new ServerInfo());
//            serverlist.get(i).setAdress(addressList[i]);
//            serverlist.get(i).setClusterState("normal");
//            String[] hostPort = addressList[i].split(":");
//            // 计数器对象
//            CountDownLatch countDownLatch = new CountDownLatch(1);
//            int curIdx = i;
//            new ZooKeeper(addressList[curIdx], 5000, event -> {
//                if (event.getState().equals(Watcher.Event.KeeperState.SyncConnected)) {
//                    serverlist.get(curIdx).setNodeState("on service");
//                    if (event.getState().equals(Watcher.Event.KeeperState.Disconnected)) {
//                        serverlist.get(curIdx).setNodeState("off line");
//                    }
//                    countDownLatch.countDown();
//                }
//            });
//            // 主线程阻塞等待连接对象的创建成功
//            countDownLatch.await();
//
//            serverlist.get(i).setNodeRole(ZookeeperUtils.serverStatus(hostPort[0], hostPort[1]));
//        }

        CommonResult commonResult = new CommonResult();
        commonResult.setCode(200);
        commonResult.setMsg("Success");
        commonResult.setData(serverlist);

        return commonResult;
    }

    @Override
    public CommonResult getServerTree(String address) throws Exception {
        ZooKeeperUtil zooKeeperUtil = new ZooKeeperUtil();

        if (ZookeeperController.client == null) {
            zooKeeperUtil.connect(address);
            zooKeeperUtil.sessionConnectionWatcherRegister();
            zooKeeperUtil.nodeWatcherRegister();
        }

        ServerTree root = transfer(new GetAllNode().ls("/"));

        CommonResult commonResult = new CommonResult();
        commonResult.setCode(200);
        commonResult.setMsg("Success");
        commonResult.setData(root);
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
