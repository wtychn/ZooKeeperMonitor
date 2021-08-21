package com.wtychn.zookeeper.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "服务器信息")
public class ServerInfo implements Serializable {

    @TableId(type = IdType.AUTO)
    private int id;

    private String host;

    private String port;

    private String mode;

    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerInfo that = (ServerInfo) o;
        return Objects.equals(host, that.host) && Objects.equals(port, that.port) && Objects.equals(mode, that.mode) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, host, port, mode, status);
    }
}
