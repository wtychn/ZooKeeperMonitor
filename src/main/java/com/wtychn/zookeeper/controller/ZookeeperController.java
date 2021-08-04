package com.wtychn.zookeeper.controller;

import com.wtychn.zookeeper.Utils.Node;
import com.wtychn.zookeeper.Utils.WebSocketServer;
import com.wtychn.zookeeper.pojo.CommonResult;
import com.wtychn.zookeeper.service.NodeService;
import com.wtychn.zookeeper.service.ServerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Api(value = "zookeeper 监控")
public class ZookeeperController {

    public static ZooKeeper zooKeeper;
    public static String nowAddress;

    @Autowired
    private ServerService serverService;
    @Autowired
    private NodeService nodeService;

    @RequestMapping("/retestadd")
    public String index() throws IOException {
        System.out.println("test");
        WebSocketServer.sendInfo("A122.51.129.180:2182");
        return "helloword!";
    }

    @RequestMapping("/retestmin")
    public String index1() throws IOException {
        System.out.println("test");
        WebSocketServer.sendInfo("M122.51.129.180:2182");
        return "helloword!";
    }

    @GetMapping("/server/{addresses}")
    @ApiOperation(value = "获取 zk 服务器信息")
    public CommonResult serverinfo(@PathVariable("addresses") String addresses) throws IOException, InterruptedException {
        System.out.println(addresses);
        return serverService.getServerList(addresses);
    }

    @GetMapping("/nodetree/{address}")
    @ApiOperation(value = "获取 zk 节点树")
    public CommonResult getServerTree(@PathVariable("address") String address) throws Exception {

        nowAddress = address;
        System.out.println(address);

        return serverService.getServerTree(address);
    }

    @GetMapping("/node/{path}")
    @ApiOperation(value = "查询节点")
    public CommonResult queryNode(@PathVariable("path") String path) throws Exception {
        CommonResult commonResult = new CommonResult();
        commonResult.setData(nodeService.select(path, zooKeeper));
        commonResult.setCode(200);
        commonResult.setMsg("Success");
        return commonResult;
    }

    @PostMapping("/node")
    @ApiOperation(value = "增加节点")
    public CommonResult addNode(Node node) throws Exception {
        nodeService.add(node, zooKeeper);

        CommonResult commonResult = new CommonResult();
        commonResult.setCode(200);
        commonResult.setMsg("Success");
        return commonResult;
    }

    @DeleteMapping("/node/{path}")
    @ApiOperation(value = "删除节点")
    public CommonResult deleteNode(@PathVariable("path") String path) throws KeeperException, InterruptedException {
        nodeService.delete(path, zooKeeper);

        CommonResult commonResult = new CommonResult();
        commonResult.setCode(200);
        commonResult.setMsg("Success");
        return commonResult;
    }

    @PutMapping("/node")
    @ApiOperation(value = "改动节点")
    public CommonResult modifyNode(@RequestBody Node node) throws KeeperException, InterruptedException {
        nodeService.update(node, zooKeeper);

        CommonResult commonResult = new CommonResult();
        commonResult.setCode(200);
        commonResult.setMsg("Success");
        return commonResult;
    }

}
