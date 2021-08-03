package com.wtychn.zookeeper.Watcher;

import lombok.SneakyThrows;

public class ChildrenNodeWatcherThread extends Thread {
    @SneakyThrows
    @Override
    public void run() {
        AllNodeListWatcher.watcherAllGetChild();
    }
}
