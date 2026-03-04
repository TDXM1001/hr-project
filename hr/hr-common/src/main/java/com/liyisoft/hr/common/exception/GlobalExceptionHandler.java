package com.liyisoft.hr.common.exception;

import com.liyisoft.hr.common.core.domain.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * 拦截所有未知异常
   */
  @ExceptionHandler(Exception.class)
  public Result<?> handleException(Exception e) {
    log.error("系统异常: {}", e.getMessage(), e);
    return Result.error("服务器内部错误，请联系管理员");
  }

  /**
   * 拦截业务运行时异常
   */
  @ExceptionHandler(RuntimeException.class)
  public Result<?> handleRuntimeException(RuntimeException e) {
    log.error("业务运行时异常: {}", e.getMessage(), e);
    return Result.error(e.getMessage());
  }
}
