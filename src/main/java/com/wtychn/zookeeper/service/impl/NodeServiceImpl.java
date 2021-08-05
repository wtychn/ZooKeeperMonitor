package com.wtychn.zookeeper.service.impl;

import com.wtychn.zookeeper.pojo.Node;
import com.wtychn.zookeeper.controller.ZookeeperController;
import com.wtychn.zookeeper.service.NodeService;
import org.springframework.stereotype.Service;

@Service
public class NodeServiceImpl implements NodeService {

    @Override
    public void add(Node node) throws Exception {
        System.out.println("新增节点：" + node.getPath());

        ZookeeperController.client
                .create()
                .creatingParentsIfNeeded()
                .forPath(node.getPath(), node.getValue().getBytes());
    }

    @Override
    public String delete(String path) throws Exception {
        System.out.println("删除节点: " + path);
        if(ZookeeperController.client.checkExists().forPath(path) == null)
            return "节点不存在！";
        ZookeeperController.client
                .delete()
                .deletingChildrenIfNeeded()
                .forPath(path);
        return "success";
    }

    @Override
    public String update(String path, String value) throws Exception {
        System.out.println("更新节点 " + path + " 值为：" + value);
        if(ZookeeperController.client.checkExists().forPath(path) == null)
            return "节点不存在！";
        ZookeeperController.client
                .setData()
                .forPath(path, value.getBytes());
        return "success";
    }

    @Override
    public String select(String path) throws Exception {
        System.out.println("查询节点:" + path);

        if(ZookeeperController.client.checkExists().forPath(path) == null)
            return "节点不存在！";

        byte[] bytes = ZookeeperController.client.getData().forPath(path);

        return new String(bytes);
    }
}
