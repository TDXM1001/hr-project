package com.liyisoft.hr.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * 自定义 MyBatis-Plus 元数据处理器，用于自动填充审计字段
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

  /**
   * 插入时的填充策略
   */
  @Override
  public void insertFill(MetaObject metaObject) {
    this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
    this.strictInsertFill(metaObject, "createBy", String.class, "system"); // 后续接入 Security 上下文替换为实际用户
    this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
    this.strictInsertFill(metaObject, "updateBy", String.class, "system");
  }

  /**
   * 更新时的填充策略
   */
  @Override
  public void updateFill(MetaObject metaObject) {
    this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
    this.strictUpdateFill(metaObject, "updateBy", String.class, "system"); // 后续接入 Security 上下文替换为实际用户
  }
}
