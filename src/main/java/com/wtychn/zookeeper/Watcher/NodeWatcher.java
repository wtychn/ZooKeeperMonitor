package com.wtychn.zookeeper.Watcher;

import com.wtychn.zookeeper.Utils.WebSocketServer;
import com.wtychn.zookeeper.controller.ZookeeperController;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

public class NodeWatcher implements Runnable{

    @Override
    public void run() {

        CuratorCache curatorCache = CuratorCache
                .builder(ZookeeperController.client, "/")
                .build();
        curatorCache.start();
        CuratorCacheListener listener = CuratorCacheListener
                .builder()
                .forTreeCache(ZookeeperController.client, (client, event) -> {
                    if (event.getType() == TreeCacheEvent.Type.NODE_ADDED) {
                        System.out.println("监听到节点增加！:" + event.getData().getPath());
                    } else if (event.getType() == TreeCacheEvent.Type.NODE_REMOVED) {
                        System.out.println("监听到节点删除！:" + event.getData().getPath());
                    } else if (event.getType() == TreeCacheEvent.Type.NODE_UPDATED) {
                        System.out.println("监听到节点更新！:" +
                                event.getData().getPath() + "数据更新为" +
                                new String(event.getData().getData()));
                    }
                    // 向前端发送消息，使前端发送请求更新节点表
                    WebSocketServer.sendInfo("Rec Success!");
                })
                .build();
        curatorCache.listenable().addListener(listener);

        System.out.println("节点监听部署完成！");
    }
}
