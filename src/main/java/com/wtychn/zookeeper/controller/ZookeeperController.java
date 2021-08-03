package com.wtychn.zookeeper.controller;

import com.wtychn.zookeeper.Utils.Json;
import com.wtychn.zookeeper.Utils.WebSocketServer;
import com.wtychn.zookeeper.Utils.ZookeeperUtils;
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

    @GetMapping("/serverTree/{addresses}")
    @ApiOperation(value = "获取 zk 节点树")
    public CommonResult getServerTree(@PathVariable("addresses") String addresses) throws Exception {

        String realAddress = addresses.substring(12, addresses.length() - 2);
        nowAddress = realAddress;
        System.out.println(realAddress);

        return serverService.getServerTree(addresses, zooKeeper);
    }

    @GetMapping("/node/{address}")
    @ApiOperation(value = "查询节点")
    public CommonResult queryNode(@PathVariable("address") String address) throws Exception {
        CommonResult commonResult = new CommonResult();
        commonResult.setData(nodeService.select(address, zooKeeper));
        commonResult.setCode(200);
        commonResult.setMsg("Success");
        return commonResult;
    }

    @PostMapping("/node")
    @ApiOperation(value = "增加节点")
    public CommonResult addNode(String address) throws Exception {
        nodeService.add(address, zooKeeper);

        CommonResult commonResult = new CommonResult();
        commonResult.setCode(200);
        commonResult.setMsg("Success");
        return commonResult;
    }

    @DeleteMapping("/node/{address}")
    @ApiOperation(value = "删除节点")
    public CommonResult deleteNode(@PathVariable("address") String address) throws KeeperException, InterruptedException {
        nodeService.delete(address, zooKeeper);

        CommonResult commonResult = new CommonResult();
        commonResult.setCode(200);
        commonResult.setMsg("Success");
        return commonResult;
    }

    @PutMapping("/node/{address}")
    @ApiOperation(value = "改动节点")
    public CommonResult modifyNode(@PathVariable("address")  String address) throws KeeperException, InterruptedException {
        nodeService.update(address, zooKeeper);

        CommonResult commonResult = new CommonResult();
        commonResult.setCode(200);
        commonResult.setMsg("Success");
        return commonResult;
    }

}
