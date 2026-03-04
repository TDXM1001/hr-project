package com.liyisoft.hr.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

/**
 * 部门实体类
 */
@Data
@TableName("sys_dept")
public class SysDept {
    /** 部门 ID */
    @TableId
    private Long id;
    
    /** 父部门 ID */
    private Long parentId;
    
    /** 祖级列表 */
    private String ancestors;
    
    /** 部门名称 */
    private String deptName;
    
    /** 显示顺序 */
    private Integer orderNum;
    
    /** 负责人 */
    private String leader;
    
    /** 部门状态（0正常 1停用） */
    private Integer status;
    
    /** 删除标志（0代表存在 2代表删除） */
    @TableLogic
    private Integer delFlag;
}
