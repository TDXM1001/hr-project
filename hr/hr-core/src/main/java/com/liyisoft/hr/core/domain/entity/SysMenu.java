package com.liyisoft.hr.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

/**
 * 系统菜单实体类
 */
@Data
@TableName("sys_menu")
public class SysMenu {
    /** 菜单ID */
    @TableId
    private Long id;
    /** 菜单名称 */
    private String menuName;
    /** 父菜单ID */
    private Long parentId;
    /** 显示顺序 */
    private Integer orderNum;
    /** 路由地址 */
    private String path;
    /** 组件路径 */
    private String component;
    /** 路由参数 */
    private String queryParam;
    /** 是否为外链 (0否 1是) */
    private Integer isFrame;
    /** 是否缓存 (0否 1是) */
    private Integer isCache;
    /** 菜单类型 (M目录 C菜单 F按钮) */
    private Integer menuType;
    /** 菜单状态 (0隐藏 1显示) */
    private String visible;
    /** 状态 (0正常 1停用) */
    private Integer status;
    /** 权限标识 */
    private String perms;
    /** 菜单图标 */
    private String icon;
    /** 删除标志 (0代表存在 2代表删除) */
    @TableLogic
    private Integer delFlag;
}
