package com.example.demo.testMain;

import com.example.demo.entity.TestObject;
import com.example.demo.entity.User;
import org.openjdk.jol.info.ClassLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChangLF 2022-02-24
 */
public class TestOutMemoryError {

    public static void main(String[] args) {
        int initialCapacity = 101440;
//        List<TestObject> testObjectList = new ArrayList<>(initialCapacity);
//        for (int i = 0; i < 1000000; i++) {
//            TestObject testObject = new TestObject();
//            testObjectList.add(testObject);
//        }
//        Integer integer = new Integer(1);
        TestObject[] testObjectList = new TestObject[initialCapacity];
//        for (int i = 0; i < testObjectList.length; i++) {
////            TestObject testObject = new TestObject(new Integer(1),new Long(2),new Boolean(false));
////            TestObject testObject = new TestObject(1,2L,false);
////            TestObject testObject = new TestObject(integer);
////            TestObject testObject = new TestObject(new Integer(1));
//            TestObject testObject = new TestObject();
//            testObjectList[i] = testObject;
//        }
//        testObjectList[1] = new TestObject();

        ClassLayout classLayout = ClassLayout.parseInstance(testObjectList);
        System.out.println(classLayout.toPrintable());
        int arrSize = initialCapacity * 4;
        System.out.println("arrSize最大是:" +arrSize);
        System.out.println("除数组外多余空间80000：" + (10*1024*1024 - arrSize));
        System.out.println(10*1024*1024);
        System.out.println((10*1024*1024-80000)/4);
        System.out.println((10*1024*1024-80000)/24);
        System.out.println(testObjectList.length);
    }
}
