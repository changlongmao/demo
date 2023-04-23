package com.example.demo.testMain.classloader;

import com.example.demo.entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ChangLF 2023-03-20
 */
public class MyClassLoader extends ClassLoader {

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
            InputStream is = getClass().getResourceAsStream(fileName);
            if (is == null) {
                return super.loadClass(name);
            }
            byte[] b = new byte[is.available()];
            is.read(b);
            return defineClass(name, b, 0, b.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(name);
        }
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        throw new ClassNotFoundException(name);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Class<? extends TestClassAddress> aClass = new TestClassAddress().getClass();
        System.out.println(aClass);
        Class<? extends TestClassAddress> aClass2 = TestClassAddress.class;
        System.out.println(aClass2);


        new ReentrantLock();
        MyClassLoader myClassLoader = new MyClassLoader();
        Class<?> classAddress = myClassLoader.loadClass("com.example.demo.testMain.classloader.TestClassAddress");
        System.out.println(classAddress);

    }
}
