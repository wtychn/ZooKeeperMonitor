package com.wtychn.zookeeper.service;

import com.wtychn.zookeeper.Utils.Node;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;

public interface NodeService {

    void add(Node node, ZooKeeper zooKeeper) throws Exception;

    void delete(String path, ZooKeeper zooKeeper) throws InterruptedException, KeeperException;

    void update(Node node, ZooKeeper zooKeeper) throws InterruptedException, KeeperException;

    List<String> select(String path, ZooKeeper zooKeeper) throws Exception;

}
