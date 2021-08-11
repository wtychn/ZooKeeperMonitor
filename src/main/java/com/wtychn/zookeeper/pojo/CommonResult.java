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

    private int stat;

    private String msg;

    private Object data;

    public void setStatus(Stat stat) {
        this.stat = stat.code;
        this.msg = stat.msg;
    }

    @AllArgsConstructor
    public enum Stat {

        SUCCESS(200, "Success"), NOT_FOUND(404, "Not found");

        private final int code;

        private final String msg;

    }

}
