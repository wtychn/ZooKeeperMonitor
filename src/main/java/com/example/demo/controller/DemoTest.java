package com.example.demo.controller;

import com.example.demo.Utils.Json;
import com.example.demo.Utils.WebSocketServer;
import com.example.demo.Utils.zookeeperUtils;
import com.example.demo.Watcher.childrenNodeWatcherThread;
import com.example.demo.Watcher.getAllNode;
import com.example.demo.Watcher.nodeDataChangedWatcherThread;
import com.example.demo.service.TestService;
import com.example.demo.vo.*;
import org.apache.catalina.connector.Response;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@RestController
public class DemoTest {
   //public static List<ServerTree1> resdata = new ArrayList<ServerTree1>();;
    public static List<String> pathList = new ArrayList<>();;
    public static boolean flag=false;
    public static ZooKeeper zooKeeper;
    public static String nowAddress;
    public static Thread getNodeChangedWatcherThread;
    public static Thread ChildrenNodeWatcherThread;

    @Resource
    private TestService testService;

    @RequestMapping("/retestadd")
    @ResponseBody
    public String index() throws IOException {
        System.out.println("test");
        WebSocketServer.sendInfo("A122.51.129.180:2182");
        return "helloword!";
    }

    @RequestMapping("/retestmin")
    @ResponseBody
    public String index1() throws IOException {
        System.out.println("test");
        WebSocketServer.sendInfo("M122.51.129.180:2182");
        return "helloword!";
    }


    @RequestMapping("/server")
    @ResponseBody
    public ResultVO serverinfo(@RequestBody Serverlogin serverlogin) throws IOException, InterruptedException {
        System.out.println(serverlogin.toString());
        //处理逻辑
        String[] addressList = testService.getAddressList(serverlogin.getAddress());
        List<ServerInfo> serverlist = new ArrayList<ServerInfo>();
        for (int i = 0; i < addressList.length; i++) {
            serverlist.add(new ServerInfo());
            serverlist.get(i).setAdress(addressList[i]);
            serverlist.get(i).setClusterState("normal");
            String[] hostPort=addressList[i].split(":");
            // 计数器对象
            CountDownLatch countDownLatch=new CountDownLatch(1);
            int I = i;
            ZooKeeper zooKeeper=new ZooKeeper(addressList[i], 5000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if(event.getState()== Watcher.Event.KeeperState.SyncConnected) {
                        serverlist.get(I).setNodeState("on service");
                        if(event.getState()== Event.KeeperState.Disconnected) {
                            serverlist.get(I).setNodeState("off line");
                        }
                        countDownLatch.countDown();
                    }
                }
            });
            // 主线程阻塞等待连接对象的创建成功
            countDownLatch.await();


            serverlist.get(i).setNodeRole(zookeeperUtils.serverStatus(hostPort[0],hostPort[1]));
          //  serverlist.get(i).setNodeState("on service");



        }


        ResultVO resultVO  = new ResultVO();
        resultVO.setCode(200);
        resultVO.setMsg("Success");
        resultVO.setData(serverlist);
        return resultVO;
    }

    @RequestMapping("/serverTree")
    @ResponseBody
    public ResultVO getServerTree(@RequestBody String  address) throws Exception {

        String realAddress=address.substring(12,address.length()-2);
        nowAddress=realAddress;
        System.out.println(realAddress);
        try {
            // 计数器对象
            CountDownLatch countDownLatch=new CountDownLatch(1);
            if (zooKeeper==null){

            zooKeeper=new ZooKeeper(realAddress, 50000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if(event.getState()== Watcher.Event.KeeperState.SyncConnected) {
                        System.out.println("连接创建成功!");
                        countDownLatch.countDown();
                    }
                }
            });
                countDownLatch.await();
            }
            // 主线程阻塞等待连接对象的创建成功

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        zookeeperUtils.directory.clear();
        getAllNode.ls("/",zooKeeper);
        //nodeWatcher.watcherGetChild("/");

        zookeeperUtils.ls("/",zooKeeper);
        System.out.println("===================================");
/*        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        for (int i = 0; i < zookeeperUtils.directory.size(); i++) {
            System.out.println(zookeeperUtils.directory.get(i));
        }
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");*/
        ServerTree1 root = transfer(zookeeperUtils.directory);
        if (ChildrenNodeWatcherThread==null){
        ChildrenNodeWatcherThread=new childrenNodeWatcherThread();
        ChildrenNodeWatcherThread.start();}
        if (getNodeChangedWatcherThread==null){
        getNodeChangedWatcherThread=new nodeDataChangedWatcherThread();
        getNodeChangedWatcherThread.start();}

        zookeeperUtils.directory.clear();
        List<ServerTree1>resdata = new ArrayList<ServerTree1>();
        resdata.add(root);
        ResultVO resultVO  = new ResultVO();
        resultVO.setCode(200);
        resultVO.setMsg("Success");
        resultVO.setData(resdata);
        return resultVO;
    }

    @RequestMapping("/query")
    @ResponseBody
            public ResultVO queryNode(@RequestBody String  address,String path) throws Exception {
        System.out.println(address);
        Json json=new Json();
        Json.Trans1(address,json);

        List<String> datalist=new ArrayList<>();
        datalist.add(json.getPath());
        datalist.add(zookeeperUtils.getNode(json.getPath(),zooKeeper));
        datalist.add("notDfined");
        datalist.add("notDfined");
        datalist.add("notDfined");

        ResultVO resultVO  = new ResultVO();
        resultVO.setData(datalist);
        resultVO.setCode(200);
        resultVO.setMsg("Success");
        return resultVO;
    }
    @RequestMapping("/add")
    @ResponseBody
    public ResultVO addNode(@RequestBody String address,String path, String Value) throws Exception {
        System.out.println(address);
        Json json=new Json();
        Json.Trans(address,json);
        System.out.println(json.getPath());
        System.out.println(json.getValue()[0]);
        zookeeperUtils.createNode(json.getValue()[0],json.getValue()[1],zooKeeper);
        //resdata.add(new ServerTree1("/a/f/k",6));

        ResultVO resultVO  = new ResultVO();
        resultVO.setCode(200);
        resultVO.setMsg("Success");
        return resultVO;
    }
    @RequestMapping("/delete")
    @ResponseBody
    public ResultVO deleteNode(@RequestBody String address,String path) throws KeeperException, InterruptedException {
        System.out.println(address);
        Json json=new Json();

        Json.Trans1(address,json);

        zookeeperUtils.deleteNode(json.getPath(),zooKeeper);

        ResultVO resultVO  = new ResultVO();
        resultVO.setCode(200);
        resultVO.setMsg("Success");
        return resultVO;
    }
    @RequestMapping("/modify")
    @ResponseBody
    public ResultVO modifyNode(@RequestBody String address,String path,String value) throws KeeperException, InterruptedException {
        System.out.println(address);
        Json json=new Json();
        Json.Trans(address,json);
        System.out.println(json.getPath());
        System.out.println(json.getValue()[0]);
        zookeeperUtils.setNode(json.getValue()[0],zooKeeper,json.getValue()[1]);
        ResultVO resultVO  = new ResultVO();
        resultVO.setCode(200);
        resultVO.setMsg("Success");
        return resultVO;

    }


    public ServerTree1 transfer(List<String> list){
        List<ServerTree1> tree=new ArrayList<ServerTree1>();
        if (list.size()==0)return null;
        ServerTree1 root=new ServerTree1("/",0);

        tree.add(root);
        for (int k = 1; k < list.size(); k++) {
            tree.add(new ServerTree1(list.get(k),k));
        }
        for (int i = 0; i < list.size(); i++) {
            String parent=list.get(i);
            for (int j = i+1; j < list.size(); j++) {
                if (list.get(j).contains(parent)){
                    if(!(list.get(j).substring(parent.length()+1).contains("/"))){
                        tree.get(i).getChildren().add(tree.get(j));
                        tree.get(j).setParentid(i);

                    }}
            }
        }
        return root;
    }
}
