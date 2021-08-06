package com.example.demo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 豆豆兼职用户表
 * <p>jy_doudou_user
 *
 * <p><p><strong>该类型由Allison 1875生成，请勿人为修改</strong>
 *
 * @author changlf 2021-06-23
 */
@Data
public class JyDoudouUserEntity {

    /**
     * <p>id
     * <p>不能为null
     */
    private Long id;

    /**
     * 用户姓名
     * <p>client_name
     * <p>长度：20
     * <p>默认：''
     */
    private String clientName;

    /**
     * 联系电话
     * <p>mobile
     * <p>长度：20
     * <p>默认：''
     */
    private String mobile;

    /**
     * 所属部门
     * <p>belong_department
     * <p>长度：50
     * <p>默认：''
     */
    private String belongDepartment;

    /**
     * 所属部门id
     * <p>belong_department_id
     * <p>默认：'0'
     */
    private Long belongDepartmentId;

    /**
     * 推荐人员工ID（麒麟）
     * <p>create_staff_id
     * <p>默认：'0'
     */
    private Long createStaffId;

    /**
     * 推荐人员工姓名（麒麟）
     * <p>create_staff_name
     * <p>长度：20
     * <p>默认：''
     */
    private String createStaffName;

    /**
     * 推荐人员工工号（麒麟）
     * <p>create_staff_no
     * <p>长度：20
     * <p>默认：''
     */
    private String createStaffNo;

    /**
     * 豆豆注册时间
     * <p>doudou_create_time
     */
    private Date doudouCreateTime;

    /**
     * 豆豆唯一标识id
     * <p>doudou_unionid
     * <p>默认：'0'
     */
    private Long doudouUnionid;

    /**
     * 提成金额
     * <p>commission_amount
     * <p>默认：'0.00'
     */
    private BigDecimal commissionAmount;

    /**
     * 0待结算，1已结算
     * <p>check_result
     * <p>默认：'0'
     */
    private Byte checkResult;

    /**
     * 结算月份
     * <p>settlement_month
     * <p>长度：20
     * <p>默认：''
     */
    private String settlementMonth;

    /**
     * 创建时间
     * <p>create_time
     * <p>默认：CURRENT_TIMESTAMP
     */
    private Date createTime;

    /**
     * 更新时间
     * <p>update_time
     * <p>默认：CURRENT_TIMESTAMP
     */
    private Date updateTime;
}
