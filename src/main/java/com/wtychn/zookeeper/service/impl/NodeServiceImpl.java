package com.wtychn.zookeeper.service.impl;

import com.wtychn.zookeeper.Utils.Json;
import com.wtychn.zookeeper.Utils.ZookeeperUtils;
import com.wtychn.zookeeper.service.NodeService;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NodeServiceImpl implements NodeService {

    @Override
    public void add(String address, ZooKeeper zooKeeper) throws Exception {
        System.out.println(address);
        Json json = new Json();
        Json.Trans(address, json);
        System.out.println(json.getPath());
        System.out.println(json.getValue()[0]);
        ZookeeperUtils.createNode(json.getValue()[0], json.getValue()[1], zooKeeper);
    }

    @Override
    public void delete(String address, ZooKeeper zooKeeper) throws InterruptedException, KeeperException {
        System.out.println(address);
        Json json = new Json();
        Json.Trans(address, json);
        ZookeeperUtils.deleteNode(json.getPath(), zooKeeper);
    }

    @Override
    public void update(String address, ZooKeeper zooKeeper) throws InterruptedException, KeeperException {
        System.out.println(address);
        Json json = new Json();
        Json.Trans(address, json);
        System.out.println(json.getPath());
        System.out.println(json.getValue()[0]);
        ZookeeperUtils.setNode(json.getValue()[0], zooKeeper, json.getValue()[1]);
    }

    @Override
    public List<String> select(String address, ZooKeeper zooKeeper) throws Exception {
        System.out.println(address);
        Json json = new Json();
        Json.Trans(address, json);

        List<String> datalist = new ArrayList<>();
        datalist.add(json.getPath());
        datalist.add(ZookeeperUtils.getNode(json.getPath(), zooKeeper));
        datalist.add("notDfined");
        datalist.add("notDfined");
        datalist.add("notDfined");

        return datalist;
    }
}
