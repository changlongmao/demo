package com.example.demo.tree;

import java.io.Serializable;

/**
 * 二叉树节点
 * @author ChangLF 2022-03-17
 */
public class BinaryNode<T extends Comparable<T>> implements Serializable {

    private static final long serialVersionUID = -6477238039299912313L;

    public BinaryNode<T> left;//左结点

    public BinaryNode<T> right;//右结点

    public T data;

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
}
