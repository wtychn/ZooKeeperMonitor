package com.wtychn.zookeeper.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wtychn.zookeeper.dao.ServerMapper;
import com.wtychn.zookeeper.pojo.CommonResult;
import com.wtychn.zookeeper.pojo.ServerInfo;
import com.wtychn.zookeeper.service.ServerService;
import com.wtychn.zookeeper.utils.WebSocketServer;
import com.wtychn.zookeeper.utils.ZooKeeperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class ServerServiceImpl implements ServerService {

    @Autowired
    ServerMapper serverMapper;

    @Override
    public CommonResult getServerList(int page, int pageSize) {
        IPage<ServerInfo> serverInfoPage = serverMapper.selectPage(new Page<>(page, pageSize), new QueryWrapper<>());

        CommonResult commonResult = new CommonResult();
        commonResult.setStatus(CommonResult.Stat.SUCCESS);
        commonResult.setData(serverInfoPage.getRecords());

        return commonResult;
    }

    @Override
    public CommonResult getAllServerList(String[] addresses) {

        List<ServerInfo> serverInfos = serverMapper.selectList(null);

        CommonResult commonResult = new CommonResult();
        commonResult.setStatus(CommonResult.Stat.SUCCESS);
        commonResult.setData(serverInfos);

        return commonResult;
    }

    @Override
    public CommonResult quitServer() {
        ZooKeeperUtil.quitConnection();

        CommonResult commonResult = new CommonResult();
        commonResult.setStatus(CommonResult.Stat.SUCCESS);

        return commonResult;
    }

    @Override
    public void insertServer(ServerInfo serverInfo) {
        serverMapper.insert(serverInfo);
    }

    @Override
    public void updateServer(ServerInfo serverInfo) {
        serverMapper.updateById(serverInfo);
    }

    @Override
    public void deleteAllServer() {
        serverMapper.clearTable();
    }

    @Async("serverWatcherExecutor")
    @Override
    public void watchServer(int id) throws IOException, InterruptedException {
        while (true) {
            ServerInfo oldInfo = serverMapper.selectById(id);
            ServerInfo newInfo = ZooKeeperUtil.getServerInfo(oldInfo);
            if (!newInfo.equals(oldInfo)) {
                updateServer(newInfo);
                WebSocketServer.sendInfo("ServerStatChanged");
                log.info("[服务器状态更改]");
            }
            Thread.sleep(2000);
        }
    }
}
