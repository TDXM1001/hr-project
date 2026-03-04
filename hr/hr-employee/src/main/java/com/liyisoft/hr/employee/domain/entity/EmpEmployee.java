package com.liyisoft.hr.employee.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import java.util.Date;

/**
 * 员工档案实体类
 */
@Data
@TableName("emp_employee")
public class EmpEmployee {
    /** 员工 ID */
    @TableId
    private Long id;
    
    /** 工号 */
    private String workNo;
    
    /** 姓名 */
    private String name;
    
    /** 性别（0男 1女 2未知） */
    private Integer gender;
    
    /** 手机号 */
    private String phone;
    
    /** 身份证号 */
    private String idCard;
    
    /** 部门 ID */
    private Long deptId;
    
    /** 岗位 ID */
    private Long positionId;
    
    /** 工作状态（0在职 1离职 2休假等） */
    private String workStatus;
    
    /** 入职时间 */
    private Date joinDate;
    
    /** 删除标志（0代表存在 2代表删除） */
    @TableLogic
    private Integer delFlag;
}
