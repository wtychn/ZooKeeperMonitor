package com.wtychn.zookeeper.service.impl;

import com.wtychn.zookeeper.Utils.Node;
import com.wtychn.zookeeper.Utils.ZookeeperUtils;
import com.wtychn.zookeeper.service.NodeService;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class NodeServiceImpl implements NodeService {

    @Override
    public void add(Node node, ZooKeeper zooKeeper) throws Exception {
        System.out.println(node);
        System.out.println(node.getPath());
        System.out.println(node.getValue()[0]);
        ZookeeperUtils.createNode(node.getPath(), node.getValue()[0], zooKeeper);
    }

    @Override
    public void delete(String path, ZooKeeper zooKeeper) throws InterruptedException, KeeperException {
        System.out.println("DELETE: " + path);
        ZookeeperUtils.deleteNode(path, zooKeeper);
    }

    @Override
    public void update(String path, String[] value, ZooKeeper zooKeeper) throws InterruptedException, KeeperException {
        System.out.println(path);
        System.out.println(Arrays.toString(value));
        ZookeeperUtils.setNode(path, zooKeeper, value[0]);
    }

    @Override
    public List<String> select(String path, ZooKeeper zooKeeper) throws Exception {
        System.out.println(path);

        List<String> datalist = new ArrayList<>();
        datalist.add(path);
        datalist.add(ZookeeperUtils.getNode(path, zooKeeper));
        datalist.add("notDfined");
        datalist.add("notDfined");
        datalist.add("notDfined");

        return datalist;
    }
}
