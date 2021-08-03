package com.wtychn.zookeeper.pojo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "返回结果")
public class CommonResult {

    private int code;

    private String msg;

    private Object data;

}
