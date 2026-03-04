package com.liyisoft.hr.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

/**
 * 字典类型实体类
 */
@Data
@TableName("sys_dict_type")
public class SysDictType {
    /** 字典主键 */
    @TableId
    private Long id;
    /** 字典名称 */
    private String dictName;
    /** 字典类型 */
    private String dictType;
    /** 状态 (0正常 1停用) */
    private Integer status;
    /** 备注 */
    private String remark;
    /** 删除标志 (0代表存在 2代表删除) */
    @TableLogic
    private Integer delFlag;
}
