package com.wtychn.zookeeper.utils;

import com.wtychn.zookeeper.watcher.NodeWatcher;
import com.wtychn.zookeeper.watcher.SessionConnectionWatcher;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class ZooKeeperUtil {

    public static CuratorFramework client;
    public static String nowAddress;

    private static int baseSleepTimeMs;

    private static int maxRetries;

    private static int sessionTimeoutMs;

    private static int connectionTimeoutMs;

    static Logger logger = LoggerFactory.getLogger(ZooKeeperUtil.class);

    public static void connect(String addresses) {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        client = CuratorFrameworkFactory.builder()
                .connectString(addresses)
                .sessionTimeoutMs(sessionTimeoutMs)  // 会话超时时间
                .connectionTimeoutMs(connectionTimeoutMs) // 连接超时时间
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        logger.info("zookeeper 服务器连接成功！");
    }

    public static void sessionConnectionWatcherRegister() {

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

            logger.info("会话监听部署完成！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void nodeWatcherRegister() {
        CuratorCache curatorCache = CuratorCache
                .builder(client, "/")
                .build();
        curatorCache.start();
        CuratorCacheListener listener = CuratorCacheListener
                .builder()
                .forTreeCache(client, new NodeWatcher())
                .build();
        curatorCache.listenable().addListener(listener);

        logger.info("节点监听部署完成！");
    }

    public static void quitConnection() {
        client.close();
        client = null;
    }

    @Value("${zookeeper.retry.baseSleepTimeMs}")
    public void setBaseSleepTimeMs(int baseSleepTimeMs) {
        ZooKeeperUtil.baseSleepTimeMs = baseSleepTimeMs;
    }

    @Value("${zookeeper.retry.maxRetries}")
    public void setMaxRetries(int maxRetries) {
        ZooKeeperUtil.maxRetries = maxRetries;
    }

    @Value("${zookeeper.connection.sessionTimeoutMs}")
    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        ZooKeeperUtil.sessionTimeoutMs = sessionTimeoutMs;
    }

    @Value("${zookeeper.connection.connectionTimeoutMs}")
    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        ZooKeeperUtil.connectionTimeoutMs = connectionTimeoutMs;
    }
}
