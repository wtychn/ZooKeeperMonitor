package com.wtychn.zookeeper.Utils;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ZookeeperUtils {
    public static List<String> directory = new ArrayList<>();

    public static void setNode(String path, ZooKeeper zooKeeper, String value) throws KeeperException, InterruptedException {
        zooKeeper.setData(path, value.getBytes(), -1);
        System.out.println("修改节点成功");
    }

    public static void deleteNode(String path, ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        zooKeeper.delete(path, -1);
        System.out.println("删除成功");
    }

    public static String getNode(String path, ZooKeeper zooKeeper) throws Exception {
        // arg1:节点的路径
        // arg3:读取节点属性的对象
        Stat stat = new Stat();
        byte[] bys = zooKeeper.getData(path, false, stat);
        // 打印数据
        System.out.println(new String(bys));
        // 版本信息
        System.out.println(stat.getVersion());
        return new String((bys));
    }

    public static void createNode(String path, String data, ZooKeeper zooKeeper) throws Exception {
        // arg1:节点的路径
        // arg2:节点的数据
        // arg3:权限列表  world:anyone:cdrwa
        // arg4:节点类型  持久化节点
        zooKeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("新增节点成功");
    }

    public static void ls(String path, ZooKeeper zooKeeper) throws Exception {
        directory.add(path);
        List<String> list = zooKeeper.getChildren(path, null);
        //判断是否有子节点
        if(!list.isEmpty()) {
            for (String s : list) {
                //判断是否为根目录
                if (path.equals("/")) {
                    ls(path + s, zooKeeper);
                } else {
                    ls(path + "/" + s, zooKeeper);
                }
            }
        }
    }

    public static String serverStatus(String host, String port) throws IOException {
        String cmd = "stat";

        Socket sock = new Socket(host, Integer.parseInt(port));
        BufferedReader reader = null;
        try {
            OutputStream outstream = sock.getOutputStream();
            // 通过Zookeeper的四字命令获取服务器的状态
            outstream.write(cmd.getBytes());
            outstream.flush();
            sock.shutdownOutput();

            reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Mode: ")) {
                    return (line.replaceAll("Mode: ", "").trim());
                }
            }
        } finally {
            sock.close();
            if (reader != null) {
                reader.close();
            }
        }
        return null;
    }

}
