package com.springboot.demo;

public class test {
    @interface testone{}

    @testone
    class test1{}

    @testone
    class test2{}

    @testone
    class test3{}

    public static void main(String[] args) {
        outName(testone.class);
    }
    static void outName(Class<?> clazz) {
        System.out.println(clazz.getName());
    }
}
