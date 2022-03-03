package com.example.demo.studyArith;

/**
 * 2. 两数相加
 * https://leetcode-cn.com/problems/add-two-numbers/
 *
 * @author ChangLF 2022-03-03
 */
public class TwoDay {

    public static void main(String[] args) {
        ListNode listNode = addTwoNumbers(new ListNode(9, new ListNode(8)), new ListNode(7, new ListNode(6, new ListNode(9, new ListNode(8, new ListNode(2))))));
        System.out.println(listNode.toString());
    }

    /**
     * 给你两个非空 的链表，表示两个非负的整数。它们每位数字都是按照逆序的方式存储的，并且每个节点只能存储一位数字。
     * <p>
     * 请你将两个数相加，并以相同形式返回一个表示和的链表。
     * <p>
     * 你可以假设除了数字 0 之外，这两个数都不会以 0开头。
     *
     * @param l1 l1 = [2,4,3]
     * @param l2 l2 = [5,6,4]
     * @return [7, 0, 8]
     * 342 + 465 = 807.
     * 每个链表中的节点数在范围 [1, 100] 内
     * 0 <= Node.val <= 9
     * 题目数据保证列表表示的数字不含前导零
     */
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        if (l1.val == 0 && l1.next == null) {
            return l2;
        }
        if (l2.val == 0 && l2.next == null) {
            return l1;
        }
        ListNode result = new ListNode(0);
        ListNode e = l1;
        ListNode f = l2;
        ListNode g = result;

        while (true) {
            int val = f.val + g.val + e.val;
            if (e.next == null) {
                e.val = 0;
            } else if (f.next == null) {
                f.val = 0;
            }
            if (val > 9) {
                g.val = val - 10;
                g.next = new ListNode(1);
            } else {
                g.val = val;
                if (e.next != null || f.next != null) {
                    g.next = new ListNode(0);
                }
            }
            if (e.next == null && f.next == null) {
                break;
            }
            g = g.next;
            if (e.next != null) {
                e = e.next;
            }
            if (f.next != null) {
                f = f.next;
            }
        }
        return result;
    }

    static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

        @Override
        public String toString() {
            return "ListNode{" +
                    "val=" + val +
                    ", next=" + next +
                    '}';
        }
    }
}
