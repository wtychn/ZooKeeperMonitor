package com.wtychn.zookeeper.watcher;

import com.wtychn.zookeeper.pojo.ServerInfo;
import com.wtychn.zookeeper.utils.WebSocketServer;
import com.wtychn.zookeeper.utils.ZooKeeperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableScheduling
public class NowServerWatcher {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Scheduled(cron = "0/5 * * * * ?")
    public void zkServerWatch() throws IOException, ClassNotFoundException {
        List<ServerInfo> oldList = ZooKeeperUtil.zkList == null ?
                new ArrayList<>() :
                deepCopy(ZooKeeperUtil.zkList);

        // 更新zkList(zk服务信息)
        List<ServerInfo> newList = ZooKeeperUtil.getServerInfos();

        // 对比更新前与更新后的zkList,有变化则说明服务器状态更改,发送websocket请求
        for (int i = 0; i < newList.size(); i++) {
            if (oldList.size() != newList.size() || !oldList.get(i).equals(newList.get(i))) {
                WebSocketServer.sendInfo("ServerStatChanged");
                logger.info("[服务器状态更改]");
                break;
            }
        }
    }

    private <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        return (List<T>)in.readObject();
    }
}
