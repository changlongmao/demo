package com.example.demo.testMain;

import com.example.demo.tree.AVLNode;
import com.example.demo.tree.AVLSearchTree;
import com.example.demo.tree.Tree;
import org.apache.poi.ss.formula.functions.T;

import java.util.*;
import java.util.concurrent.Executors;

/**
 * @author ChangLF 2023-03-16
 */
public class TestTree2 {

    /*
     问题：/*
     操作给定的二叉树，将其变换为源二叉树的镜像。
        二叉树的镜像定义：
        源二叉树
        8
        | |
        6 10
        | | | |
        5 7 9 11
        镜像二叉树
        8
        | |
        10 6
        | | | |
        11 9 7 5
    */
    public static void main(String[] args) {
        new HashMap<>();
        new ArrayList<>();

        Executors.newSingleThreadExecutor();
    }

    public class TreeNode<T extends Comparable> {

        public TreeNode<T> parent;//上结点
        public TreeNode<T> left;//左结点

        public TreeNode<T> right;//右结点

        public T data;

        public TreeNode(T data) {
            this(null,null,data);
        }

        public TreeNode(TreeNode<T> left, TreeNode<T> right, T data) {
            this(left,right,data,0);
        }

        public TreeNode(TreeNode<T> left, TreeNode<T> right, T data, int height) {
            this.left=left;
            this.right=right;
            this.data=data;
        }

    }

}
