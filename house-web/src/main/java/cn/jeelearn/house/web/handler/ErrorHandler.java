package cn.jeelearn.house.web.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 错误处理器
 * @Auther: lyd
 * @Date: 2018/12/11
 * @Version:v1.0
 */
@ControllerAdvice
public class ErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    /**
     * 发生错误时进行一些操作
     *      如打印url
     * @auther: lyd
     * @date: 2018/12/11
     */
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public String error500(HttpServletRequest request, Exception e){
        logger.error(e.getMessage(), e);
        logger.error(request.getRequestURL() + " encounter 500");
        return "error/500";
    }

}

