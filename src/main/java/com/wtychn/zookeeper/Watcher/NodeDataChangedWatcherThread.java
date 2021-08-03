package com.wtychn.zookeeper.Watcher;

import lombok.SneakyThrows;

public class NodeDataChangedWatcherThread extends Thread {
    @SneakyThrows
    @Override
    public void run() {
        NodeDataChangedWatcher.watcherData();
    }
}
