package com.wtychn.zookeeper.service;

import com.wtychn.zookeeper.pojo.CommonResult;
import com.wtychn.zookeeper.pojo.ServerInfo;

import java.io.IOException;

public interface ServerService {

    CommonResult getServerList(int page, int pageSize);

    CommonResult getAllServerList(String[] addresses) throws InterruptedException;

    CommonResult quitServer();

    void insertServer(ServerInfo serverInfo);

    void updateServer(ServerInfo serverInfo);

    void deleteAllServer();

    void watchServer(int id) throws IOException, InterruptedException;

}
