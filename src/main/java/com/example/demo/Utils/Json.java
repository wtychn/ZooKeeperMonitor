package com.example.demo.Utils;
import java.util.Arrays;

public class Json {
    private String address;
    private String path;
    private String[] value;

    @Override
    public String toString() {
        return "Json{" +
                "address='" + address + '\'' +
                ", path='" + path + '\'' +
                ", value=" + Arrays.toString(value) +
                '}';
    }

    public Json(String address, String path, String[] value) {
        this.address = address;
        this.path = path;
        this.value = value;
    }
    public Json() {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String[] getValue() {
        return value;
    }

    public void setValue(String[] value) {
        this.value = value;
    }
    public static void Trans(String Json,Json json){
        StringBuffer cache=new StringBuffer();
        //StringBuffer path=new StringBuffer();
        String[] value=new String[5];
        int i=0;

        while(Json.charAt(i)!=':'){i++;}//遍历至第一个引号
        i=i+2;//此时Json.charAt(i)='1';
        while(Json.charAt(i)!='\"'){
            cache.append(Json.charAt(i));
            i++;
        }//此时address=122.51.129.180:2181
        json.setAddress(cache.toString());//address


        cache=new StringBuffer();
        while(Json.charAt(i)!=':'){i++;}//遍历至path后的引号
        i=i+2;//Json.charAt(i)='z';
        while(Json.charAt(i)!='\"'){
            cache.append(Json.charAt(i));
            i++;
        }//此时path=/zookeeper
        json.setPath(cache.toString());//path


        while(Json.charAt(i)!='['){i++;}//遍历至value后的'['
        i=i+2;//此时json.charAt(i)='"'
        for(int j=0;j<5;j++){
            cache=new StringBuffer();
            while(Json.charAt(i)!='\"'){
                cache.append(Json.charAt(i));
                i++;
            }
            value[j]=cache.toString();
            i+=3;//跳过  ","
        }
        json.setValue(value);

    }
    public static void Trans1(String Json, Json json){
        StringBuffer cache=new StringBuffer();
        //StringBuffer path=new StringBuffer();
        String[] value=new String[5];
        int i=0;

        while(Json.charAt(i)!=':'){i++;}//遍历至第一个引号
        i=i+2;//此时Json.charAt(i)='1';
        while(Json.charAt(i)!='\"'){
            cache.append(Json.charAt(i));
            i++;
        }//此时address=122.51.129.180:2181
        json.setAddress(cache.toString());//address


        cache=new StringBuffer();
        while(Json.charAt(i)!=':'){i++;}//遍历至path后的引号
        i=i+2;//Json.charAt(i)='z';
        while(Json.charAt(i)!='\"'){
            cache.append(Json.charAt(i));
            i++;
        }//此时path=/zookeeper
        json.setPath(cache.toString());//path




    }
}
