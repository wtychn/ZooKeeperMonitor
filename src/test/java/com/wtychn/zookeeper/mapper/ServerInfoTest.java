package com.wtychn.zookeeper.mapper;

import com.wtychn.zookeeper.dao.ServerMapper;
import com.wtychn.zookeeper.pojo.ServerInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServerInfoTest {

    @Autowired
    private ServerMapper serverMapper;

    @Test
    public void testSelect() {
        serverMapper.clearTable();
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setHost("47.107.104.211");
        serverInfo.setPort("8001");
        serverInfo.setMode("follower");
        serverInfo.setStatus("Connected");
        serverMapper.insert(serverInfo);
        System.out.println(serverMapper.selectById(1));
    }
}