package com.wtychn.zookeeper.watcher;

import com.wtychn.zookeeper.utils.WebSocketServer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeWatcher implements TreeCacheListener {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
        if (treeCacheEvent.getType() == TreeCacheEvent.Type.NODE_ADDED) {
            logger.info("监听到节点增加！:" + treeCacheEvent.getData().getPath());
        } else if (treeCacheEvent.getType() == TreeCacheEvent.Type.NODE_REMOVED) {
            logger.info("监听到节点删除！:" + treeCacheEvent.getData().getPath());
        } else if (treeCacheEvent.getType() == TreeCacheEvent.Type.NODE_UPDATED) {
            logger.info("监听到节点更新！:" +
                    treeCacheEvent.getData().getPath() + "数据更新为：" +
                    new String(treeCacheEvent.getData().getData()));
        }
        // 向前端发送消息，使前端发送请求更新节点表
        WebSocketServer.sendInfo("Rec Success!");
    }
}
