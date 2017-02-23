package com.ligootech.webdtu.util;

import org.apache.commons.logging.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Created by wly on 2016/1/8 15:02.
 */
public class LoggerUtil {

    /**
     * 日志文件中写入获取到的参数
     * @param className
     * @param methodName
     * @param req
     * @param logger
     */
    public static void logParameterInfo(String className, String methodName, HttpServletRequest req, Log logger){
        StringBuilder logStr = new StringBuilder("\n");
        logStr.append("class:").append(className).append("--method:").append(methodName).append(" ParameterInfo:");

        Enumeration<String> keys = req.getParameterNames();
        while(keys.hasMoreElements()) {
            String k = keys.nextElement();
            logStr.append(k).append("=").append(req.getParameter(k)).append("   ");
        }

        logger.debug(logStr);
    }
}
