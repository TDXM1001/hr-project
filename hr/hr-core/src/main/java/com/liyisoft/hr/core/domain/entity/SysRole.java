package com.liyisoft.hr.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

/**
 * 角色实体类
 */
@Data
@TableName("sys_role")
public class SysRole {
    /** 角色 ID */
    @TableId
    private Long id;
    
    /** 角色名称 */
    private String roleName;
    
    /** 角色权限字符串 */
    private String roleKey;
    
    /** 显示顺序 */
    private Integer roleSort;
    
    /** 角色状态（0正常 1停用） */
    private Integer status;
    
    /** 删除标志（0代表存在 2代表删除） */
    @TableLogic
    private Integer delFlag;
}
