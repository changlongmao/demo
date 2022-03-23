package com.example.demo.tree;

/**
 * @author ChangLF 2022-03-23
 */
public class TestTree {


    public static void main(String[] args) throws Exception {

        BinarySearchTree<String> cbtree = new BinarySearchTree<>();

        cbtree.insert("D");
        cbtree.insert("C");
        cbtree.insert("E");
        cbtree.insert("A");
        cbtree.insert("B");
        cbtree.insert("F");
        cbtree.insert("G");

        cbtree.remove("D");
        System.out.println("先根遍历:" + cbtree.preOrder());
        System.out.println("非递归先根遍历:" + cbtree.preOrderTraverse());
        System.out.println("中根遍历:" + cbtree.inOrder());
        System.out.println("非递归中根遍历:" + cbtree.inOrderTraverse());
        System.out.println("后根遍历:" + cbtree.postOrder());
        System.out.println("非递归后根遍历:" + cbtree.postOrderTraverse());
        System.out.println("二叉树层次遍历:" + cbtree.levelOrder());
        System.out.println("查找最大结点(根据搜索二叉树):" + cbtree.findMax());
        System.out.println("查找最小结点(根据搜索二叉树):" + cbtree.findMin());
        System.out.println("二叉树结点数量：" + cbtree.size());
        System.out.println("二叉树高度：" + cbtree.height());
        System.out.println("获取二叉树中结点:" + cbtree.findNode("A"));
        System.out.println("判断二叉树中是否存在结点:" + cbtree.contains("A"));
        System.out.println("获取二叉树根结点:" + cbtree);
        cbtree.clear();
        System.out.println("获取二叉树根结点" +cbtree);

    }
}
