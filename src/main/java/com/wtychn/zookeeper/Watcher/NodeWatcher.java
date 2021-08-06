package com.wtychn.zookeeper.Watcher;

import com.wtychn.zookeeper.Utils.WebSocketServer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

public class NodeWatcher implements TreeCacheListener {

    @Override
    public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
        if (treeCacheEvent.getType() == TreeCacheEvent.Type.NODE_ADDED) {
            System.out.println("监听到节点增加！:" + treeCacheEvent.getData().getPath());
        } else if (treeCacheEvent.getType() == TreeCacheEvent.Type.NODE_REMOVED) {
            System.out.println("监听到节点删除！:" + treeCacheEvent.getData().getPath());
        } else if (treeCacheEvent.getType() == TreeCacheEvent.Type.NODE_UPDATED) {
            System.out.println("监听到节点更新！:" +
                    treeCacheEvent.getData().getPath() + "数据更新为：" +
                    new String(treeCacheEvent.getData().getData()));
        }
        // 向前端发送消息，使前端发送请求更新节点表
        WebSocketServer.sendInfo("Rec Success!");
    }
}
