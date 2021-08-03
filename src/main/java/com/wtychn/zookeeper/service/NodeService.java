package com.wtychn.zookeeper.service;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;

public interface NodeService {

    void add(String address, ZooKeeper zooKeeper) throws Exception;

    void delete(String address, ZooKeeper zooKeeper) throws InterruptedException, KeeperException;

    void update(String address, ZooKeeper zooKeeper) throws InterruptedException, KeeperException;

    List<String> select(String address, ZooKeeper zooKeeper) throws Exception;

}
