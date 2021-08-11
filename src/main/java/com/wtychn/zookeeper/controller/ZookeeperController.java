package com.wtychn.zookeeper.controller;

import com.wtychn.zookeeper.pojo.CommonResult;
import com.wtychn.zookeeper.pojo.Node;
import com.wtychn.zookeeper.service.NodeService;
import com.wtychn.zookeeper.service.ServerService;
import com.wtychn.zookeeper.utils.ZooKeeperUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@Api(value = "zookeeper 监控")
public class ZookeeperController {

    @Autowired
    private ServerService serverService;
    @Autowired
    private NodeService nodeService;

    Logger logger = LoggerFactory.getLogger(getClass());

//    @RequestMapping("/retestadd")
//    public String index() throws IOException {
//        System.out.println("test");
//        WebSocketServer.sendInfo("A122.51.129.180:2182");
//        return "helloword!";
//    }
//
//    @RequestMapping("/retestmin")
//    public String index1() throws IOException {
//        System.out.println("test");
//        WebSocketServer.sendInfo("M122.51.129.180:2182");
//        return "helloword!";
//    }

    @GetMapping("/server/{addresses}")
    @ApiOperation(value = "获取 zk 服务器信息")
    public CommonResult serverinfo(@PathVariable("addresses") String addresses) throws IOException, InterruptedException {
        System.out.println(addresses);
        return serverService.getServerList(addresses);
    }

    @GetMapping("/nodetree/{addresses}")
    @ApiOperation(value = "获取 zk 节点树")
    public CommonResult getServerTree(@PathVariable("addresses") String addresses) throws Exception {

        ZooKeeperUtil.nowAddress = addresses;
        logger.info("访问地址：{}", addresses);

        return serverService.getServerTree(addresses);
    }

    @DeleteMapping("/quit")
    @ApiOperation(value = "断开当前连接")
    public CommonResult quitConnection() {

        return serverService.quitServer();
    }

    @GetMapping("node")
    @ApiOperation(value = "查询节点")
    public CommonResult queryNode(@RequestParam(value = "path") String path) throws Exception {
        return nodeService.select(path);
    }

    @PostMapping("node")
    @ApiOperation(value = "增加节点")
    public CommonResult addNode(Node node) throws Exception {
        return nodeService.add(node);
    }

    @DeleteMapping("node")
    @ApiOperation(value = "删除节点")
    public CommonResult deleteNode(@RequestParam(value = "path") String path) throws Exception {
        return nodeService.delete(path);
    }

    @PutMapping("node")
    @ApiOperation(value = "改动节点")
    public CommonResult modifyNode(HttpServletRequest request) throws Exception {
        return nodeService.update(request.getParameter("path"), request.getParameter("value"));
    }

}
