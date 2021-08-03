package com.wtychn.zookeeper.Utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Json {
    private String address;
    private String path;
    private String[] value;

    public static void Trans(String jsonStr, Json json) {
        StringBuilder cache = new StringBuilder();
        String[] value = new String[5];
        int i = 0;
        //遍历至第一个引号
        while (i < jsonStr.length() && jsonStr.charAt(i) != ':') {
            i++;
        }
        i = i + 2;  //此时 jsonStr.charAt(i)='1';
        while (i < jsonStr.length() && jsonStr.charAt(i) != '\"') {
            cache.append(jsonStr.charAt(i));
            i++;
        }   //此时 address=122.51.129.180:2181
        json.setAddress(cache.toString());  //address

        cache = new StringBuilder();
        while (i < jsonStr.length() && jsonStr.charAt(i) != ':') {
            i++;
        }   //遍历至path后的引号
        i = i + 2;  //jsonStr.charAt(i)='z';
        while (i < jsonStr.length() && jsonStr.charAt(i) != '\"') {
            cache.append(jsonStr.charAt(i));
            i++;
        }   //此时path=/zookeeper
        json.setPath(cache.toString()); //path


        while (i < jsonStr.length() && jsonStr.charAt(i) != '[') {
            i++;
        }   //遍历至value后的'['
        i = i + 2;  //此时json.charAt(i)='"'
        for (int j = 0; j < 5; j++) {
            cache = new StringBuilder();
            while (jsonStr.charAt(i) != '\"') {
                cache.append(jsonStr.charAt(i));
                i++;
            }
            value[j] = cache.toString();
            i += 3; //跳过  ","
        }
        json.setValue(value);
    }

}
