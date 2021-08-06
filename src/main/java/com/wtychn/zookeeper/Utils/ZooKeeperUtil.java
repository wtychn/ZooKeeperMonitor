package com.wtychn.zookeeper.Utils;

import com.wtychn.zookeeper.Watcher.NodeWatcher;
import com.wtychn.zookeeper.Watcher.SessionConnectionWatcher;
import com.wtychn.zookeeper.controller.ZookeeperController;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetAddress;

public class ZooKeeperUtil {

    public void connect(String addresses) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(50000, 3);
        ZookeeperController.client = CuratorFrameworkFactory.builder()
                .connectString(addresses)
                .sessionTimeoutMs(50000)  // 会话超时时间
                .connectionTimeoutMs(50000) // 连接超时时间
                .retryPolicy(retryPolicy)
                .build();
        ZookeeperController.client.start();
        System.out.println("zookeeper 服务器连接成功！");
    }

    public void sessionConnectionWatcherRegister() {
        CuratorFramework client = ZookeeperController.client;
        try {
            String rootPath = "/services";
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            String serviceInstance = "/prometheus" + "-" + hostAddress + "-";
            String path = rootPath + serviceInstance;

            SessionConnectionWatcher sessionConnectionWatcher = new SessionConnectionWatcher(path, "");

            client.getConnectionStateListenable()
                    .addListener(sessionConnectionWatcher);

            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(path);

            System.out.println("会话监听部署完成！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nodeWatcherRegister() {
        CuratorFramework client = ZookeeperController.client;

        CuratorCache curatorCache = CuratorCache
                .builder(client, "/")
                .build();
        curatorCache.start();
        CuratorCacheListener listener = CuratorCacheListener
                .builder()
                .forTreeCache(client, new NodeWatcher())
                .build();
        curatorCache.listenable().addListener(listener);

        System.out.println("节点监听部署完成！");
    }
}
