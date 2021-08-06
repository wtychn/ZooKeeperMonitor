package com.wtychn.zookeeper.service;

import com.wtychn.zookeeper.pojo.CommonResult;
import com.wtychn.zookeeper.pojo.Node;

public interface NodeService {

    CommonResult add(Node node) throws Exception;

    CommonResult delete(String path) throws Exception;

    CommonResult update(String path, String value) throws Exception;

    CommonResult select(String path) throws Exception;

}
