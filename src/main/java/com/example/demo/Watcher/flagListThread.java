package com.example.demo.Watcher;

import com.example.demo.Utils.WebSocketServer;
import com.example.demo.Utils.zookeeperUtils;
import lombok.SneakyThrows;

public class flagListThread extends Thread {
    @SneakyThrows
    @Override
    public void run() {
        while (true){
            if (zookeeperUtils.FalgList.get()>0){
                zookeeperUtils.FalgList.set(0);
                WebSocketServer.sendInfo("Rec Success!");
            }
            Thread.sleep(5000);
        }
    }
}
