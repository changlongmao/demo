package com.example.demo.tree;

import java.io.Serializable;
import java.util.Objects;

/**
 * 二叉树节点
 * @author ChangLF 2022-03-17
 */
public class BinaryNode<T extends Comparable<T>> implements Serializable {

    private static final long serialVersionUID = -6477238039299912313L;

    public BinaryNode<T> left;//左结点

    public BinaryNode<T> right;//右结点

    public T data;

    public static void main(String[] args) {
        BinaryNode<Integer> root = new BinaryNode<>(1);
        BinaryNode<Integer> a = new BinaryNode<>(2);
        BinaryNode<Integer> b = new BinaryNode<>(3);
        BinaryNode<Integer> c = new BinaryNode<>(4);
        root.right = a;
        a.right = b;
        b.right = c;
        System.out.println(root);
        BinaryNode<Integer> e = a.right;
        a.right = e.left;
        e.left = a;
        root.right = e;
        System.out.println(root);
    }


    public BinaryNode(T data, BinaryNode<T> left, BinaryNode<T> right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    public BinaryNode(T data) {
        this(data, null, null);
    }

    /**
     * 判断是否为叶子结点
     *
     * @return
     */
    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }

    @Override
    public String toString() {
        return "{" +
                "\"data\": " + data +
                (left == null ? "" : ",\"left\": " + left) +
                (right == null ? "" : ",\"right\": " + right) +
                '}';
    }
}
