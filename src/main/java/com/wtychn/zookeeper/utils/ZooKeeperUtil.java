package com.wtychn.zookeeper.utils;

import com.wtychn.zookeeper.pojo.ServerInfo;
import com.wtychn.zookeeper.watcher.NodeWatcher;
import com.wtychn.zookeeper.watcher.SessionConnectionWatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

@Component
@Slf4j
public class ZooKeeperUtil {

    public static CuratorFramework client;
    public static String nowAddresses;

    private static int baseSleepTimeMs;

    private static int maxRetries;

    private static int sessionTimeoutMs;

    private static int connectionTimeoutMs;

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

    public static void connect() {
        connect(nowAddresses);
    }

    /**
     * zk 客户端连接底层逻辑是每隔一秒切换一个 server 的连接
     */
    public static void connect(String addresses) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        client = CuratorFrameworkFactory.builder()
                .connectString(addresses)
                .sessionTimeoutMs(sessionTimeoutMs)  // 会话超时时间
                .connectionTimeoutMs(connectionTimeoutMs) // 连接超时时间
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        log.info("zookeeper 服务器连接成功！");
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

            log.info("会话监听部署完成！");
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

        log.info("节点监听部署完成！");
    }

    public static void quitConnection() {
        client.close();
        client = null;
        nowAddresses = null;
        cancelServerWatcher();
    }


    public static ServerInfo getServerInfo(ServerInfo serverInfo) {
        ServerInfo result = getServerInfo(serverInfo.getHost(), serverInfo.getPort());
        result.setId(serverInfo.getId());
        return result;
    }

    public static ServerInfo getServerInfo(String address) {
        String[] hostPort = address.split(":");
        return getServerInfo(hostPort[0], hostPort[1]);
    }

    private static ServerInfo getServerInfo(String host, String port) {
        String mode;
        String cmd = "stat";

        ServerInfo resInfo = new ServerInfo();
        resInfo.setHost(host);
        resInfo.setPort(port);

        try (
                Socket sock = new Socket(host, Integer.parseInt(port));
                OutputStream outStream = sock.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()))
        ) {
            // 通过Zookeeper的四字命令获取服务器的状态
            outStream.write(cmd.getBytes());
            outStream.flush();
            sock.shutdownOutput();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Mode: ")) {
                    mode = line.replaceAll("Mode: ", "").trim();
                    resInfo.setStatus("Connected");
                    resInfo.setMode(mode);
                }
            }
        } catch (ConnectException e) {
            log.info("[连接已经丢失]");
            resInfo.setStatus("DisConnected");
            resInfo.setMode("NULL");
        } catch (IOException e) {
            log.info("IO异常");
        }

        return resInfo;
    }

    public static void cancelServerWatcher() {

    }

}
