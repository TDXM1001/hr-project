package com.liyisoft.hr.core.controller;

import com.liyisoft.hr.common.core.domain.Result;
import com.liyisoft.hr.core.entity.SysMenu;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 菜单控制层，用于暴露菜单加载接口，返回菜单树形结构数据给前端。
 */
@RestController
@RequestMapping("/api/system/menu")
public class SysMenuController {

    /**
     * 获取当前系统用户的菜单列表（树形结构）
     * 
     * @return 包含当前用户菜单项的通用返回结果包装对象
     */
    @GetMapping("/user-menus")
    public Result<List<SysMenu>> getUserMenus() {
        // 模拟提供一套基础菜单，当前结构包含首页面板以及组织结构的折叠菜单
        SysMenu dashboard = new SysMenu(1L, "首页面板", "/dashboard", "House");
        SysMenu employeeGroup = new SysMenu(2L, "员工组织", "/employee", "User");
        
        SysMenu myProfile = new SysMenu(3L, "组织架构", "/employee/org", "Connection");
        SysMenu employeeList = new SysMenu(4L, "档案管理", "/employee/list", "List");
        
        // 编排层级结构
        employeeGroup.setChildren(Arrays.asList(myProfile, employeeList));
        
        return Result.success(Arrays.asList(dashboard, employeeGroup));
    }
}
