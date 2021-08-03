package com.example.demo.Watcher;

import lombok.SneakyThrows;

public class childrenNodeWatcherThread extends Thread {
    @SneakyThrows
    @Override
    public void run() {
        allNodeListWatcher.watcherAllGetChild();
    }
}
