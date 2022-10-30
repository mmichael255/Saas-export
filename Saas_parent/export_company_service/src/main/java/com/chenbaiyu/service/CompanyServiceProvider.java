package com.chenbaiyu.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class CompanyServiceProvider {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext cxl = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        cxl.start();
        System.in.read();
    }
}
