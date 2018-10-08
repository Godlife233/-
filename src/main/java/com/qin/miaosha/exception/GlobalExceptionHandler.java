package com.qin.miaosha.exception;

import com.qin.miaosha.common.ServerResponse;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.concurrent.*;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private void log(Exception ex, HttpServletRequest request) {
        logger.error("************************异常开始*******************************");
        logger.error("请求地址：" + request.getRequestURL());
        Enumeration enumeration = request.getParameterNames();
        logger.error("请求参数");
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement().toString();
            logger.error(name + "---" + request.getParameter(name));
        }

        StackTraceElement[] error = ex.getStackTrace();
        for (StackTraceElement stackTraceElement : error) {
            logger.error(stackTraceElement.toString());
        }
        logger.error("************************异常结束*******************************");
    }

    @ExceptionHandler(value = Exception.class)
    public ServerResponse handlerException(HttpServletRequest request,Exception e){
        log(e,request);
        if(e instanceof TimeoutException){
            return ServerResponse.createBySuccessMessage("请求超时请重试");
        }

        return ServerResponse.createByErrorMessage("系统出现异常请重试");
    }
    private final static ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.MINUTES,
            new ArrayBlockingQueue<Runnable>(1),new ThreadPoolExecutor.DiscardPolicy());



}
