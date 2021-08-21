package com.wtychn.zookeeper.service.impl;

import com.wtychn.zookeeper.pojo.ServerInfo;
import com.wtychn.zookeeper.service.ServerService;
import com.wtychn.zookeeper.utils.ZooKeeperUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServerServiceImplTest {

    @Autowired
    ServerService serverService;

    @Test
    void getAllServerList() throws InterruptedException {
        serverService.deleteAllServer();

        String[] addresses = new String[]{
                "47.107.104.211:8001",
                "47.107.104.211:8002",
                "47.107.104.211:8003"
        };

        ServerInfo s1 = ZooKeeperUtil.getServerInfo(addresses[0]);
        ServerInfo s2 = ZooKeeperUtil.getServerInfo(addresses[1]);
        ServerInfo s3 = ZooKeeperUtil.getServerInfo(addresses[2]);

        serverService.insertServer(s1);
        serverService.insertServer(s2);
        serverService.insertServer(s3);

        List<ServerInfo> serverList = (List<ServerInfo>) serverService.getAllServerList(addresses).getData();
        serverList.forEach(System.out::println);
    }

}