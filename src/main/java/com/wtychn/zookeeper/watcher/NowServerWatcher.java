package com.wtychn.zookeeper.watcher;

import com.wtychn.zookeeper.pojo.ServerInfo;
import com.wtychn.zookeeper.utils.ZooKeeperUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@ConfigurationProperties(prefix = "watcher")
@Slf4j
@Getter
@Setter
public class NowServerWatcher {

    ThreadPoolExecutor threadPoolExecutor;

    private int corePoolSize;

    private int maximumPoolSize;

    private long keepAliveTime;

    public void startWatch() {

        threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());

        for (ServerInfo server : ZooKeeperUtil.zkList) {
            threadPoolExecutor.execute(new ServerWatcherThread(server));
            log.info("{}:{} 服务器监控线程启动", server.getHost(), server.getPort());
        }

    }

    public void stopWatch() {
        if(threadPoolExecutor == null) return;
        threadPoolExecutor.shutdownNow();
    }

}
