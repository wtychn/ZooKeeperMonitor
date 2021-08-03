package com.example.demo.service.impl;

import com.example.demo.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Override
    public void test1() {
        System.out.println("test");
    }

    @Override
    public String[] getAddressList(String address) {
        //要做异常处理
        return address.split(",");
    }
}
