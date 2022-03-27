package com.example.demo.tree;

import java.util.LinkedList;
import java.util.StringJoiner;

/**
 * @author ChangLF 2022-03-17
 */
public class BinarySearchTree<T extends Comparable<T>> implements Tree<T> {

    //根结点
    private BinaryNode<T> root;

    private int size;

    public BinarySearchTree() {
        root = null;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        return "BinarySearchTree{" +
                "root=" + root +
                '}';
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
     * Blog : http://blog.csdn.net/javazejian [原文地址,请尊重原创]
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

    /**
     * 非递归的先根遍历
     *
     * @return
     */
    public String preOrderTraverse() {
        StringBuffer sb = new StringBuffer();
        //构建用于存放结点的栈
        LinkedList<BinaryNode<T>> stack = new LinkedList<>();

        BinaryNode<T> p = this.root;

        while (p != null || !stack.isEmpty()) {

            if (p != null) {
                //访问该结点
                sb.append(p.data + ",");

                //将已访问过的结点入栈
                stack.push(p);

                //继续访问其左孩子，直到p为null
                p = p.left;

            } else { //若p=null 栈不为空,则说明已沿左子树访问完一条路径, 从栈中弹出栈顶结点,并访问其右孩子
                p = stack.pop();//获取已访问过的结点记录
                p = p.right;
            }

        }
        //去掉最后一个逗号
        if (sb.length() > 0) {
            return sb.toString().substring(0, sb.length() - 1);
        } else {
            return sb.toString();
        }
    }

    @Override
    public String inOrder() {
        String sb = inOrder(root);
        if (sb.length() > 0) {
            //去掉尾部","号
            sb = sb.substring(0, sb.length() - 1);
        }
        return sb;
    }

    /**
     * 中根遍历
     * Blog : http://blog.csdn.net/javazejian [原文地址,请尊重原创]
     *
     * @return
     */
    public String inOrder(BinaryNode<T> subtree) {
        StringBuffer sb = new StringBuffer();
        if (subtree != null) {//递归结束条件
            //先遍历左子树
            sb.append(inOrder(subtree.left));
            //再遍历根结点
            sb.append(subtree.data + ",");
            //最后遍历右子树
            sb.append(inOrder(subtree.right));
        }
        return sb.toString();
    }

    /**
     * 非递归的中根遍历
     * Blog : http://blog.csdn.net/javazejian [原文地址,请尊重原创]
     *
     * @return
     */
    public String inOrderTraverse() {
        StringBuffer sb = new StringBuffer();
        //构建用于存放结点的栈
        LinkedList<BinaryNode<T>> stack = new LinkedList<>();

        BinaryNode<T> p = this.root;

        while (p != null || !stack.isEmpty()) {
            while (p != null) {//把左孩子都入栈,至到左孩子为null
                stack.push(p);
                p = p.left;
            }
            //如果栈不为空,因为前面左孩子已全部入栈
            if (!stack.isEmpty()) {
                p = stack.pop();
                //访问p结点
                sb.append(p.data + ",");
                //访问p结点的右孩子
                p = p.right;
            }
        }

        if (sb.length() > 0) {
            return sb.toString().substring(0, sb.length() - 1);
        } else {
            return sb.toString();
        }
    }

    @Override
    public String postOrder() {
        String sb = postOrder(root);
        if (sb.length() > 0) {
            //去掉尾部","号
            sb = sb.substring(0, sb.length() - 1);
        }

        return sb;
    }

    /**
     * 后根遍历
     * Blog : http://blog.csdn.net/javazejian [原文地址,请尊重原创]
     *
     * @param subtree
     * @return
     */
    public String postOrder(BinaryNode<T> subtree) {
        StringBuffer sb = new StringBuffer();
        if (subtree != null) {//递归结束条件
            //先遍历左子树
            sb.append(postOrder(subtree.left));

            //再遍历右子树
            sb.append(postOrder(subtree.right));

            //最后遍历根结点
            sb.append(subtree.data + ",");
        }
        return sb.toString();
    }

    /**
     * 非递归后根遍历
     *
     * @return
     */
    public String postOrderTraverse() {
        StringBuffer sb = new StringBuffer();
        //构建用于存放结点的栈
        LinkedList<BinaryNode<T>> stack = new LinkedList<>();

        BinaryNode<T> currentNode = this.root;
        BinaryNode<T> prev = this.root;

        while (currentNode != null || !stack.isEmpty()) {
            //把左子树加入栈中,直到叶子结点为止
            while (currentNode != null) {
                stack.push(currentNode);
                currentNode = currentNode.left;
            }

            //开始访问当前结点父结点的右孩子
            if (!stack.isEmpty()) {
                //获取右孩子，先不弹出
                BinaryNode<T> temp = stack.peek().right;
                //先判断是否有右孩子或者右孩子是否已被访问过
                if (temp == null || temp == prev) {//没有右孩子||右孩子已被访问过
                    //如果没有右孩子或者右孩子已被访问,则弹出父结点并访问
                    currentNode = stack.pop();
                    //访问
                    sb.append(currentNode.data + ",");
                    //记录已访问过的结点
                    prev = currentNode;
                    //置空当前结点
                    currentNode = null;
                } else {
                    //有右孩子,则开始遍历右子树
                    currentNode = temp;
                }
            }

        }

        //去掉最后一个逗号
        if (sb.length() > 0) {
            return sb.toString().substring(0, sb.length() - 1);
        } else {
            return sb.toString();
        }
    }

    /**
     * 层次遍历
     *
     * @return
     */
    @Override
    public String levelOrder() {
        LinkedList<BinaryNode<T>> queue = new LinkedList<>();
        StringJoiner sb = new StringJoiner(",");
        BinaryNode<T> p = this.root;

        while (p != null) {
            //记录经过的结点
            sb.add(String.valueOf(p.data));

            //先按层次遍历结点,左结点一定在右结点之前访问
            if (p.left != null) {
                //孩子结点入队
                queue.add(p.left);
            }

            if (p.right != null) {
                queue.add(p.right);
            }
            //访问下一个结点
            p = queue.poll();
        }
        return sb.toString();
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
            size++;
            return p;
        }

        //比较插入结点的值，决定向左子树还是右子树搜索
        int compareResult = data.compareTo(p.data);

        if (compareResult < 0) {//左
            p.left = insert(data, p.left);
        } else if (compareResult > 0) {//右
            p.right = insert(data, p.right);
        } else {
            // 相同则覆盖
            p.data = data;
        }
        return p;
    }

    @Override
    public void remove(T data) {
        if (data == null) throw new RuntimeException("data can not be null !");
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
            size--;
        }

        return p;//返回该结点
    }

    @Override
    public T findMin() {
        if (isEmpty())
            throw new RuntimeException("BinarySearchTree is empty!");

        return findMin(root).data;
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

    @Override
    public T findMax() {
        if (isEmpty())
            throw new RuntimeException("BinarySearchTree is empty!");

        return findMax(root).data;
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
    public AVLNode<T> findNode(T data) {
//        if (data == null) throw new RuntimeException("data can not be null !");
//
//        BinaryNode<T> p = root;
//
//        //比较结点的值，决定向左子树还是右子树搜索
//        while (true) {
//            if (p == null) {
//                return null;
//            }
//            int compareResult = data.compareTo(p.data);
//
//            if (compareResult < 0) {
//                //左
//                p = p.left;
//            } else if (compareResult > 0) {
//                //右
//                p = p.right;
//            } else {
//                // 相同则返回
//                return p;
//            }
//        }
        return null;
    }

    @Override
    public boolean contains(T data) throws Exception {
        return findNode(data) != null;
    }

    @Override
    public void clear() {
        root = null;
    }
}
