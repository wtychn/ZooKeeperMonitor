package com.wtychn.zookeeper.service.impl;

import com.wtychn.zookeeper.pojo.CommonResult;
import com.wtychn.zookeeper.pojo.Node;
import com.wtychn.zookeeper.controller.ZookeeperController;
import com.wtychn.zookeeper.service.NodeService;
import org.springframework.stereotype.Service;

@Service
public class NodeServiceImpl implements NodeService {

    @Override
    public CommonResult add(Node node) throws Exception {
        System.out.println("新增节点：" + node.getPath());
        CommonResult commonResult = new CommonResult();

        ZookeeperController.client
                .create()
                .creatingParentsIfNeeded()
                .forPath(node.getPath(), node.getValue().getBytes());

        commonResult.setCode(200);
        commonResult.setMsg("Success");
        return commonResult;
    }

    @Override
    public CommonResult delete(String path) throws Exception {
        System.out.println("删除节点: " + path);
        CommonResult commonResult = new CommonResult();

        if (ZookeeperController.client.checkExists().forPath(path) == null) {
            commonResult.setCode(404);
            commonResult.setMsg("节点不存在！");
        }

        ZookeeperController.client
                .delete()
                .deletingChildrenIfNeeded()
                .forPath(path);
        commonResult.setCode(200);
        commonResult.setMsg("Success");

        return commonResult;
    }

    @Override
    public CommonResult update(String path, String value) throws Exception {
        System.out.println("更新节点 " + path + " 值为：" + value);
        CommonResult commonResult = new CommonResult();

        if (ZookeeperController.client.checkExists().forPath(path) == null) {
            commonResult.setCode(404);
            commonResult.setMsg("节点不存在！");
        }

        ZookeeperController.client
                .setData()
                .forPath(path, value.getBytes());
        commonResult.setCode(200);
        commonResult.setMsg("Success");

        return commonResult;
    }

    @Override
    public CommonResult select(String path) throws Exception {
        System.out.println("查询节点:" + path);
        CommonResult commonResult = new CommonResult();

        if (ZookeeperController.client.checkExists().forPath(path) == null) {
            commonResult.setCode(404);
            commonResult.setMsg("节点不存在！");
        }

        byte[] bytes = ZookeeperController.client.getData().forPath(path);

        commonResult.setCode(200);
        commonResult.setMsg("Success");
        commonResult.setData(new String(bytes));

        return commonResult;
    }
}
