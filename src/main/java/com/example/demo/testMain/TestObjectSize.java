package com.example.demo.testMain;

import com.example.demo.entity.TestObject;
import com.example.demo.entity.User;
import org.openjdk.jol.info.ClassLayout;

import java.util.Arrays;

/**
 * https://www.cnblogs.com/rickiyang/p/14206724.html
 * 测试对象占用内存大小
 * <p>
 * 在 Hotspot VM 中，对象在内存中的存储布局分为 3 块区域：
 * <p>
 * 对象头（Header）
 * 实例数据（Instance Data）
 * 对齐填充（Padding）
 * <p>
 * 对象头又包括三部分：MarkWord、元数据指针、数组长度。
 * MarkWord：用于存储对象运行时的数据，好比 HashCode、锁状态标志、GC分代年龄等。这部分在 64 位操作系统下占 8 字节，32 位操作系统下占 4 字节。
 * 指针：对象指向它的类元数据的指针，虚拟机通过这个指针来确定这个对象是哪一个类的实例。在开启指针压缩的状况下占 4 字节，未开启状况下占 8 字节。
 * 数组长度：这部分只有是数组对象才有，若是是非数组对象就没这部分。这部分占 4 字节。
 * <p>
 * 引用类型在 64 位系统上占用 8 个字节，JDK 1.6 开始 64 bit JVM 正式支持了 -XX:+UseCompressedOops (64位系统默认开启) ，这个参数可以压缩指针
 * 对象头信息：64 位系统下，原生对象头大小为 16 字节，压缩后为 12 字节；
 * 对象的引用类型：64 位系统下，引用类型本身大小为 8 字节，压缩后为 4 字节；
 * 对象数组类型：64 位平台下，数组类型本身大小为 24 字节，压缩后 16 字节。
 * <p>
 * Java 基础对象在内存中占用的空间如下：、
 * 类型	占用空间(byte)
 * boolean	1
 * byte	1
 * short	2
 * char	2
 * int	4
 * float	4
 * long	8
 * double	8
 *
 * 关于对齐填充，Java 对象的大小默认是按照 8字节对齐，也就是说 Java 对象的大小必须是 8字节的倍数。若是算到最后不够 8 字节的话，那么就会进行对齐填充。
 *
 * @author ChangLF 2022-02-20
 */
public class TestObjectSize {

    public static void main(String[] args) {
        System.out.println(System.getProperties());
        System.out.println(Arrays.toString(args));
        ClassLayout classLayout = ClassLayout.parseInstance(new TestObject());
        System.out.println(classLayout.toPrintable());
//        ClassLayout classLayout = ClassLayout.parseInstance(new User());
//        System.out.println(classLayout.toPrintable());
//        ClassLayout classLayout = ClassLayout.parseInstance(new long[3]);
//        System.out.println(classLayout.toPrintable());
    }
}
