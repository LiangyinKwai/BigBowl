package com.bb.video.common.util;

import com.google.common.base.Preconditions;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by LiangyinKwai on 2019-07-08.
 */
public class HttpHelper {

    /**
     * 获取请求链接
     *
     * @param request 请求类
     * @return 链接
     */
    public static String getUrl(HttpServletRequest request) {
        Preconditions.checkNotNull(request,
                "HttpHelper getUrl: request is null");
        return request.getRequestURL().toString();
    }

    /**
     * 获取请求参数
     *
     * @param request
     * @return
     */
    public static String getRequestParameter(HttpServletRequest request) {
        StringBuffer params = new StringBuffer();
        if (ObjectUtils.isEmpty(request)) {
            return "";
        }
        Map map = new HashMap();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String paramValue = "";
            String[] paramValues = request.getParameterValues(paramName);
            for (String v : paramValues) {
                paramValue += "," + v;
            }
            if (paramValue.length() != 0) {
                map.put(paramName, paramValue.substring(1));
            }
        }

        Set<Map.Entry<String, String>> set = map.entrySet();
        for (Map.Entry entry : set) {
            params.append(entry.getKey()).append("->").append(entry.getValue());
        }
        return params.toString();
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值
     * @return ip
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        System.out.println("x-forwarded-for ip: " + ip);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if( ip.indexOf(",")!=-1 ){
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
