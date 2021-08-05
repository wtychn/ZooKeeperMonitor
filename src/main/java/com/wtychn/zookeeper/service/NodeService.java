package com.wtychn.zookeeper.service;

import com.wtychn.zookeeper.pojo.Node;

public interface NodeService {

    void add(Node node) throws Exception;

    String delete(String path) throws Exception;

    String update(String path, String value) throws Exception;

    String select(String path) throws Exception;

}
