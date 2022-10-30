package com.chenbaiyu.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class CargoServiceProvider {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        classPathXmlApplicationContext.start();
        System.in.read();
    }
}
