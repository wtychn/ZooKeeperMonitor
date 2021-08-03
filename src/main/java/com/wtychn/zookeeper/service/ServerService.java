package com.wtychn.zookeeper.service;

import com.wtychn.zookeeper.pojo.CommonResult;
import com.wtychn.zookeeper.pojo.Serverlogin;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public interface ServerService {

    CommonResult getServerList(String addresses) throws IOException, InterruptedException;

    CommonResult getServerTree(String addresses, ZooKeeper zooKeeper) throws Exception;
}
