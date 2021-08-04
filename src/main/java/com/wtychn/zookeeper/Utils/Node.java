package com.wtychn.zookeeper.Utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Node {

    private String address;
    private String path;
    private String[] value;

}
