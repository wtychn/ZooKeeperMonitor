package com.wtychn.zookeeper.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wtychn.zookeeper.pojo.ServerInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ServerMapper extends BaseMapper<ServerInfo> {

    //清空表
    @Update("truncate table server_info")
    void clearTable();

}
