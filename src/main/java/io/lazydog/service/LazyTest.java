package io.lazydog.service;

import io.lazydog.Spring.LazySpringAppContext;

public class LazyTest {

    public static void main(String[] args) {
        LazySpringAppContext appContext = new LazySpringAppContext(AppConfig.class);
        UserService userService = (UserService) appContext.getBean("userService");

    }

}
