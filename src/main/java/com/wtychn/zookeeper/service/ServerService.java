package com.wtychn.zookeeper.service;

import com.wtychn.zookeeper.pojo.CommonResult;

import java.io.IOException;

public interface ServerService {

    CommonResult getServerList(int page, int pageSize);

    CommonResult getServerList() throws IOException, InterruptedException;

    CommonResult getServerTree(String address) throws Exception;

    CommonResult quitServer();
}
