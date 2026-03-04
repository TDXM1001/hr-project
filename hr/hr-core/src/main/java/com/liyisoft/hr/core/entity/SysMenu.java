package com.liyisoft.hr.core.entity;

import java.util.List;

/**
 * 系统菜单实体类
 * 用于承载前端所需渲染的动态菜单数据结构。
 */
public class SysMenu {
    /** 菜单ID */
    private Long id;
    /** 菜单名称 */
    private String name;
    /** 路由路径 */
    private String path;
    /** 菜单图标 */
    private String icon;
    /** 子菜单列表 */
    private List<SysMenu> children;

    /**
     * 构造函数
     * @param id   菜单ID
     * @param name 菜单名称
     * @param path 路由路径
     * @param icon 菜单图标
     */
    public SysMenu(Long id, String name, String path, String icon) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.icon = icon;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public List<SysMenu> getChildren() { return children; }
    public void setChildren(List<SysMenu> children) { this.children = children; }
}
