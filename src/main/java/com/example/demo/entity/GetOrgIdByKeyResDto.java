package com.example.demo.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: OrgResDto
 * @Description:
 * @Author: Chang
 * @Date: 2021/07/01 10:20
 **/
@Data
public class GetOrgIdByKeyResDto {

    private List<GetOrgIdByKeyResDto> getRecord;

    /** 主键 */
    private Long id;

    /** 父机构id */
    private Long pid;

    /** 根机构id, 当前机构为根机构时, 则等于org_id */
    private Long rootOrgId;

    /** 是否管理机构: 0-否; 1-是 */
    private Integer manageFlag;

    /** 机构层级 */

    private Integer orgLevel;

    /** 机构代码, 根据层级自动生成 */
    private String orgCode;

    /** 机构名称 */
    private String orgTitle;

    /** 描述 */
    private String orgRemark;

    /** 状态: 0-未启用;1-已启用;9-已删除 */
    private Integer status;

    /** 负责人id */
    private Long orgManagerId;

    /** 机构类型: 0-自有部门; 1-公司; 2-分公司; 3-大区 */
    private Integer orgType;

    /** 员工号前缀 */
    private String staffcdPrefix;

    /** 员工号序列 */
    private Integer staffcdSeq;

    /** 员工号序列长度 */
    private Integer staffcdLength;

    /** 创建时间 */
    private Date createTime;

    /** 最后更新时间 */
    private Date updateTime;

    private Long createUid;

    private Long updateUid;

    /** 推送状态: 0-未推送(推送失败); 1-已推送 */
    private Integer pushFlag;

    /** 租户标识: 0-租户; 1-金柚网内部组织 */
    private Integer tenancyFlag;

    /** 部门title按层级拼接 */
    private String key;

    /**
     * 在职人数
     */
    private Integer ondutyNum;

    /**
     * 筛选入职时间后在职人数
     */
    private Integer ondutyJoinNum;
}
