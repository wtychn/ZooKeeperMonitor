package com.example.demo.Watcher;

import lombok.SneakyThrows;

public class nodeDataChangedWatcherThread extends Thread {
    @SneakyThrows
    @Override
    public void run() {
        nodeDataChangedWatcher.watcherData();
    }
}
