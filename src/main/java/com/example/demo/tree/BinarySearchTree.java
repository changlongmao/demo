package com.example.demo.tree;

/**
 * @author ChangLF 2022-03-17
 */
public class BinarySearchTree<T extends Comparable<T>> implements Tree<T> {

    //根结点
    protected BinaryNode<T> root;

    public BinarySearchTree() {
        root = null;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return size(root);
    }

    /**
     * 递归实现：定义根节点root后，再用subtree实现递归
     *
     * @param subtree
     * @return
     */
    private int size(BinaryNode<T> subtree) {
        if (subtree == null) {
            return 0;
        } else {
            //对比汉诺塔:H(n)=H(n-1) + 1 + H(n-1)
            return size(subtree.left) + 1 + size(subtree.right);
        }
    }

    @Override
    public int height() {
        return height(root);
    }

    /**
     * 递归实现
     *
     * @param subtree
     * @return
     */
    private int height(BinaryNode<T> subtree) {
        if (subtree == null) {
            return 0;
        } else {
            int l = height(subtree.left);
            int r = height(subtree.right);
            return (l > r) ? (l + 1) : (r + 1);//返回并加上当前层
        }
    }

    @Override
    public String preOrder() {
        String sb = preOrder(root);
        if (sb.length() > 0) {
            //去掉尾部","号
            sb = sb.substring(0, sb.length() - 1);
        }

        return sb;
    }

    /**
     * 先根遍历
     *
     * @param subtree
     * @return
     */
    private String preOrder(BinaryNode<T> subtree) {
        StringBuffer sb = new StringBuffer();
        if (subtree != null) {//递归结束条件
            //先访问根结点
            sb.append(subtree.data + ",");
            //遍历左子树
            sb.append(preOrder(subtree.left));
            //遍历右子树
            sb.append(preOrder(subtree.right));
        }
        return sb.toString();
    }

    @Override
    public String inOrder() {
        return null;
    }

    @Override
    public String postOrder() {
        return null;
    }

    @Override
    public String levelOrder() {
        return null;
    }

    @Override
    public void insert(T data) {
        if (data == null) throw new RuntimeException("data can not be null !");
        //插入操作
        root = insert(data, root);
    }

    /**
     * 插入操作,递归实现
     *
     * @param data
     * @param p
     * @return
     */
    private BinaryNode<T> insert(T data, BinaryNode<T> p) {
        if (p == null) {
            p = new BinaryNode<>(data, null, null);
            return p;
        }

        //比较插入结点的值，决定向左子树还是右子树搜索
        int compareResult = data.compareTo(p.data);

        if (compareResult < 0) {//左
            p.left = insert(data, p.left);
        } else if (compareResult > 0) {//右
            p.right = insert(data, p.right);
        } else {
            //已有元素就没必要重复插入了
        }
        return p;
    }

    @Override
    public void remove(T data) {
        if (data == null) throw new RuntimeException("data can\'Comparable be null !");
        //删除结点
        root = remove(data, root);
    }

    /**
     * 分3种情况
     * 1.删除叶子结点(也就是没有孩子结点)
     * 2.删除拥有一个孩子结点的结点(可能是左孩子也可能是右孩子)
     * 3.删除拥有两个孩子结点的结点
     *
     * @param data
     * @param p
     * @return
     */
    private BinaryNode<T> remove(T data, BinaryNode<T> p) {
        //没有找到要删除的元素,递归结束
        if (p == null) {
            return null;
        }
        int compareResult = data.compareTo(p.data);
        if (compareResult < 0) {//左边查找删除结点
            p.left = remove(data, p.left);
        } else if (compareResult > 0) {
            p.right = remove(data, p.right);
        } else if (p.left != null && p.right != null) {//已找到结点并判断是否有两个子结点(情况3)
            //中继替换，找到右子树中最小的元素并替换需要删除的元素值
            p.data = findMin(p.right).data;
            //移除用于替换的结点
            p.right = remove(p.data, p.right);
        } else {
            //拥有一个孩子结点的结点和叶子结点的情况
            p = (p.left != null) ? p.left : p.right;
        }

        return p;//返回该结点
    }

    @Override
    public T findMin() {
        if (isEmpty())
            throw new RuntimeException("BinarySearchTree is empty!");

        return findMin(root).data;
    }

    @Override
    public T findMax() {
        if (isEmpty())
            throw new RuntimeException("BinarySearchTree is empty!");

        return findMax(root).data;
    }

    /**
     * 查找最小值结点
     *
     * @param p
     * @return
     */
    private BinaryNode<T> findMin(BinaryNode<T> p) {
        if (p == null)//结束条件
            return null;
        else if (p.left == null)//如果没有左结点,那么t就是最小的
            return p;
        return findMin(p.left);
    }

    /**
     * 查找最大值结点
     *
     * @param p
     * @return
     */
    private BinaryNode<T> findMax(BinaryNode<T> p) {
        if (p == null)//结束条件
            return null;
        else if (p.right == null)
            return p;
        return findMax(p.right);
    }

    @Override
    public BinaryNode<T> findNode(T data) {
        return null;
    }

    @Override
    public boolean contains(T data) throws Exception {
        return false;
    }

    @Override
    public void clear() {

    }
}
