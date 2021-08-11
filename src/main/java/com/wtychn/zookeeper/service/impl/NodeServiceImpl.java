package com.wtychn.zookeeper.service.impl;

import com.wtychn.zookeeper.pojo.CommonResult;
import com.wtychn.zookeeper.pojo.Node;
import com.wtychn.zookeeper.service.NodeService;
import com.wtychn.zookeeper.utils.ZooKeeperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NodeServiceImpl implements NodeService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public CommonResult add(Node node) throws Exception {
        logger.info("新增节点：" + node.getPath());
        CommonResult commonResult = new CommonResult();

        ZooKeeperUtil.client
                .create()
                .creatingParentsIfNeeded()
                .forPath(node.getPath(), node.getValue().getBytes());

        commonResult.setStatus(CommonResult.Stat.SUCCESS);
        return commonResult;
    }

    @Override
    public CommonResult delete(String path) throws Exception {
        logger.info("删除节点: " + path);
        CommonResult commonResult = new CommonResult();

        if (ZooKeeperUtil.client.checkExists().forPath(path) == null) {
            commonResult.setStatus(CommonResult.Stat.NOT_FOUND);
        }

        ZooKeeperUtil.client
                .delete()
                .deletingChildrenIfNeeded()
                .forPath(path);
        commonResult.setStatus(CommonResult.Stat.SUCCESS);

        return commonResult;
    }

    @Override
    public CommonResult update(String path, String value) throws Exception {
        logger.info("更新节点 " + path + " 值为：" + value);
        CommonResult commonResult = new CommonResult();

        if (ZooKeeperUtil.client.checkExists().forPath(path) == null) {
            commonResult.setStatus(CommonResult.Stat.NOT_FOUND);
        }

        ZooKeeperUtil.client
                .setData()
                .forPath(path, value.getBytes());
        commonResult.setStatus(CommonResult.Stat.SUCCESS);

        return commonResult;
    }

    @Override
    public CommonResult select(String path) throws Exception {
        logger.info("查询节点:" + path);
        CommonResult commonResult = new CommonResult();

        if (ZooKeeperUtil.client.checkExists().forPath(path) == null) {
            commonResult.setStatus(CommonResult.Stat.NOT_FOUND);
        }

        byte[] bytes = ZooKeeperUtil.client.getData().forPath(path);

        commonResult.setStatus(CommonResult.Stat.SUCCESS);
        commonResult.setData(new String(bytes));

        return commonResult;
    }
}
