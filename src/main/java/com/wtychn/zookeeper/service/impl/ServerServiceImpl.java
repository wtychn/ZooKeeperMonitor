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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ServerServiceImpl implements ServerService {

    @Autowired
    ServerMapper serverMapper;

    Set<Integer> idSet = new HashSet<>();
    boolean threadSwitch = true;

    @Override
    public CommonResult getServerList(int page, int pageSize) {
        IPage<ServerInfo> serverInfoPage = new Page<>();
        // 防止数据未写入时查到空数据，自旋等待
        while (serverInfoPage.getRecords().size() == 0) {
            serverInfoPage = serverMapper.selectPage(
                    new Page<>(page, pageSize),
                    new QueryWrapper<>());
        }

        CommonResult commonResult = new CommonResult();
        commonResult.setStatus(CommonResult.Stat.SUCCESS);
        commonResult.setData(serverInfoPage.getRecords());

        return commonResult;
    }

    @Override
    public CommonResult getAllServerList(String[] addresses) {
        threadSwitch = true;
        List<ServerInfo> serverInfos = serverMapper.selectList(null);

        CommonResult commonResult = new CommonResult();
        commonResult.setStatus(CommonResult.Stat.SUCCESS);
        commonResult.setData(serverInfos);

        return commonResult;
    }

    @Override
    public CommonResult quitServer() {
        ZooKeeperUtil.quitConnection();

        idSet.clear();
        threadSwitch = false;

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
        // 重复 id 直接返回
        if (idSet.contains(id)) {
            return;
        } else {
            idSet.add(id);
        }

        log.info("{} 号监控线程启动", id);

        while (threadSwitch) {
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
