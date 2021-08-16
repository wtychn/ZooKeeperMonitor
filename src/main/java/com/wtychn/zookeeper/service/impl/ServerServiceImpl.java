package com.wtychn.zookeeper.service.impl;

import com.wtychn.zookeeper.pojo.CommonResult;
import com.wtychn.zookeeper.pojo.ServerInfo;
import com.wtychn.zookeeper.pojo.ServerTree;
import com.wtychn.zookeeper.service.ServerService;
import com.wtychn.zookeeper.utils.GetAllNode;
import com.wtychn.zookeeper.utils.ZooKeeperUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServerServiceImpl implements ServerService {

    @Override
    public CommonResult getServerList(int page, int pageSize) {
        List<ServerInfo> pageInfo;
        int start = (page - 1) * pageSize;
        int end = start + pageSize;
        pageInfo = ZooKeeperUtil.zkList
                .subList(start, Math.min(ZooKeeperUtil.zkList.size(), end));

        CommonResult commonResult = new CommonResult();
        commonResult.setStatus(CommonResult.Stat.SUCCESS);
        commonResult.setData(pageInfo);

        return commonResult;
    }

    @Override
    public CommonResult getServerList() {

        List<ServerInfo> serverInfos = ZooKeeperUtil.getServerInfos();

        CommonResult commonResult = new CommonResult();
        commonResult.setStatus(CommonResult.Stat.SUCCESS);
        commonResult.setData(serverInfos);

        return commonResult;
    }

    @Override
    public CommonResult getServerTree(String address) throws Exception {

        if (ZooKeeperUtil.client == null) {
            ZooKeeperUtil.connect(address);
            ZooKeeperUtil.sessionConnectionWatcherRegister();
            ZooKeeperUtil.nodeWatcherRegister();
        }

        ServerTree root = transfer(new GetAllNode().ls("/"));

        CommonResult commonResult = new CommonResult();
        commonResult.setStatus(CommonResult.Stat.SUCCESS);
        commonResult.setData(root);
        return commonResult;
    }

    @Override
    public CommonResult quitServer() {
        ZooKeeperUtil.quitConnection();

        CommonResult commonResult = new CommonResult();
        commonResult.setStatus(CommonResult.Stat.SUCCESS);

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
