package com.wtychn.zookeeper.Watcher;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.CreateMode;

import java.nio.charset.StandardCharsets;

public class SessionConnectionWatcher implements ConnectionStateListener {

    private String path;
    private String data;

    public SessionConnectionWatcher(String path, String data) {
        this.path = path;
        this.data = data;
    }

    @Override
    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
        if (connectionState == ConnectionState.LOST) {
            System.out.println("zk session超时");
            while (true) {
                try {
                    if (curatorFramework.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
                        curatorFramework.create()
                                .creatingParentsIfNeeded()
                                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                                .forPath(path, data.getBytes(StandardCharsets.UTF_8));
                        System.out.println("重连zk成功");
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
