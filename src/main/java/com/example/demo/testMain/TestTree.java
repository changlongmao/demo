package com.example.demo.testMain;


import com.example.demo.entity.User;

import javax.swing.tree.TreeNode;
import java.math.BigInteger;
import java.util.TreeMap;

/**
 * @author ChangLF 2022-02-27
 */
public class TestTree {

    public static void main(String[] args) {
        // 初始化节点
        TreeMap<Integer, Object> treeMap = new TreeMap<>();
        treeMap.put(20, new Object());
        treeMap.put(10, new Object());
        treeMap.put(30, new Object());
        treeMap.put(15, new Object());
        treeMap.put(5, new Object());
        treeMap.put(35, new Object());
        treeMap.put(25, new Object());
        treeMap.put(8, new Object());
        System.out.println(treeMap);
        treeMap.remove(15);
        System.out.println(treeMap);


    }
}
