package com.wtychn.zookeeper.watcher;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class SessionConnectionWatcher implements ConnectionStateListener {

    private final String path;
    private final String data;

    Logger logger = LoggerFactory.getLogger(getClass());

    public SessionConnectionWatcher(String path, String data) {
        this.path = path;
        this.data = data;
    }

    @Override
    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
        if (connectionState == ConnectionState.LOST) {
            logger.info("zk session超时");
            while (true) {
                try {
                    if (curatorFramework.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
                        curatorFramework.create()
                                .creatingParentsIfNeeded()
                                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                                .forPath(path, data.getBytes(StandardCharsets.UTF_8));
                        logger.info("重连zk成功");
                        break;
                    }
                } catch (InterruptedException e) {
                    break;
                } catch (Exception ignored) {
                }
            }
        }
    }
}
