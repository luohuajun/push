package com.shinemo.mpush.test.spi.test;

import com.shinemo.mpush.common.spi.SPI;



@SPI("test1")
public interface TestService {

    public String sayHi(String name);

}
