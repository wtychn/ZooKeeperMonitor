package com.wtychn.zookeeper.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "服务器地址和 session")
public class ServerLogin {

    private String address;

    private String session;

}
