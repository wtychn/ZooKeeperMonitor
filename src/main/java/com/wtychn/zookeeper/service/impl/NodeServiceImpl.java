package com.wtychn.zookeeper.service.impl;

import com.wtychn.zookeeper.pojo.CommonResult;
import com.wtychn.zookeeper.pojo.Node;
import com.wtychn.zookeeper.pojo.ServerTree;
import com.wtychn.zookeeper.service.NodeService;
import com.wtychn.zookeeper.utils.GetAllNode;
import com.wtychn.zookeeper.utils.ZooKeeperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NodeServiceImpl implements NodeService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public CommonResult getNodeTree() throws Exception {

        if (ZooKeeperUtil.client == null) {
            while (ZooKeeperUtil.nowAddresses == null) {
            }

            ZooKeeperUtil.connect(ZooKeeperUtil.nowAddresses);
            ZooKeeperUtil.sessionConnectionWatcherRegister();
            ZooKeeperUtil.nodeWatcherRegister();
        }

        ServerTree root = transfer(new GetAllNode().ls("/"));

        CommonResult commonResult = new CommonResult();
        commonResult.setStatus(CommonResult.Stat.SUCCESS);
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

    @Override
    public CommonResult add(Node node) {
        logger.info("新增节点：" + node.getPath());
        CommonResult commonResult = new CommonResult();

        try {
            ZooKeeperUtil.client
                    .create()
                    .creatingParentsIfNeeded()
                    .forPath(node.getPath(), node.getValue().getBytes());

            commonResult.setStatus(CommonResult.Stat.SUCCESS);
        } catch (Exception e) {
            commonResult.setStatus(CommonResult.Stat.UNAUTHORIZED);
            e.printStackTrace();
        }

        return commonResult;
    }

    @Override
    public CommonResult delete(String path) throws Exception {
        logger.info("删除节点: " + path);
        CommonResult commonResult = new CommonResult();

        if (ZooKeeperUtil.client.checkExists().forPath(path) == null) {
            commonResult.setStatus(CommonResult.Stat.NOT_FOUND);
        }

        try {
            ZooKeeperUtil.client
                    .delete()
                    .deletingChildrenIfNeeded()
                    .forPath(path);

            commonResult.setStatus(CommonResult.Stat.SUCCESS);
        } catch (Exception e) {
            commonResult.setStatus(CommonResult.Stat.UNAUTHORIZED);
            e.printStackTrace();
        }

        return commonResult;
    }

    @Override
    public CommonResult update(String path, String value) throws Exception {
        logger.info("更新节点 " + path + " 值为：" + value);
        CommonResult commonResult = new CommonResult();

        if (ZooKeeperUtil.client.checkExists().forPath(path) == null) {
            commonResult.setStatus(CommonResult.Stat.NOT_FOUND);
        }

        try {
            ZooKeeperUtil.client
                    .setData()
                    .forPath(path, value.getBytes());

            commonResult.setStatus(CommonResult.Stat.SUCCESS);
        } catch (Exception e) {
            commonResult.setStatus(CommonResult.Stat.UNAUTHORIZED);
            e.printStackTrace();
        }

        return commonResult;
    }

    @Override
    public CommonResult select(String path) throws Exception {
        logger.info("查询节点:" + path);
        CommonResult commonResult = new CommonResult();

        if (ZooKeeperUtil.client.checkExists().forPath(path) == null) {
            commonResult.setStatus(CommonResult.Stat.NOT_FOUND);
        }

        byte[] bytes = ZooKeeperUtil.client.getData().forPath(path);

        commonResult.setStatus(CommonResult.Stat.SUCCESS);
        commonResult.setData(new String(bytes));

        return commonResult;
    }
}
