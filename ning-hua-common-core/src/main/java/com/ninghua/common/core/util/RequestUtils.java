package com.ninghua.common.core.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.ninghua.common.core.constants.CommonConstants;
import com.ninghua.common.core.request.NingHuaHeader;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Author Derek.Fung
 * @Date 2025/5/16 14:08
 **/
public class RequestUtils {

    /**
     * 获取用户真实IP
     */
    public static String getUserIp(HttpServletRequest request) {
        String ip = request.getHeader(CommonConstants.USER_IP);
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = RequestUtils.getIpAddress(request);
        }
        return ip;
    }

    private static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            return ip.split(",")[0];
        } else {
            return ip;
        }
    }

    /**
     * 获取微服务的外网访问地址
     */
    public static String getComponentUrl(HttpServletRequest request) {
        String gatewayHost = request.getHeader("x-forwarded-host");
        String component = request.getHeader("x-forwarded-prefix");
        String proto = request.getHeader("x-forwarded-proto");
        return UriComponentsBuilder.fromUriString(StrUtil.format("{}://{}", proto, gatewayHost)).path(component).toUriString();
    }


    /**
     * 获取指定头信息
     */
    public static NingHuaHeader getNingHuaHeaderByRequest(HttpServletRequest request) {
        Enumeration<String> enumeration = request.getHeaderNames();
        Map<String, String> headerMap = new HashMap<>();
        while(enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            headerMap.put(name, request.getHeader(name));
        }
        return parseNingHuaHeaderHeaderFromMap(headerMap);
    }

    /**
     * 获取指定头信息
     */
    public static NingHuaHeader getNingHuaHeaderByRequest(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        Map<String, String> headerMap = new HashMap<>();
        headers.forEach((name, values) -> {
            headerMap.put(name, values.get(0));
        });
        return parseNingHuaHeaderHeaderFromMap(headerMap);
    }

    /**
     * 解析AppHeader
     * @param headerMap
     * @return
     */
    public static NingHuaHeader parseNingHuaHeaderHeaderFromMap(Map<String, String> headerMap) {
        JSONObject headers = new JSONObject();
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            if (StringUtils.startsWithIgnoreCase(name, NingHuaHeader.PREFIX)) {
                headers.set(StrUtil.removePrefixIgnoreCase(name.toLowerCase(), NingHuaHeader.PREFIX), value);
            }
        }
        ObjectMapper om = new ObjectMapper();
        om.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);
        return JSONUtil.toBean(headers.toString(), NingHuaHeader.class, true);
        /**
        try {

            return om.readValue(headers.toString(), NingHuaHeader.class);
        } catch (IOException e) {
            throw new RuntimeException("NingHuaHeader提取异常:" + e.getMessage());
        }
         **/
    }

    /**
     * 获取网关的RequestId，必须经过网关的请求才有
     */
    public static String getGatewayRequestId(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(CommonConstants.REQUEST_ID))
                .orElse(request.getHeader("request-id")) ;
    }

    public static NingHuaHeader parseNingHuaHeaderFromJson(JSONObject json) {
        JSONObject headers = new JSONObject();
        for (Map.Entry<String, Object> entry : json.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue().toString();
            if (StringUtils.startsWithIgnoreCase(name, NingHuaHeader.PREFIX)) {
                headers.set(StrUtil.removePrefixIgnoreCase(name.toLowerCase(), NingHuaHeader.PREFIX), value);
            }
        }
        ObjectMapper om = new ObjectMapper();
        om.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);
        try {
            return om.readValue(headers.toString(), NingHuaHeader.class);
        } catch (IOException e) {
            throw new RuntimeException("GuimiHeader提取异常:" + e.getMessage());
        }
    }

    /**
     * 获取完整的请求路径如 GET /path?id=1&c=4
     */
    public static String getRequestString(HttpServletRequest request) {
        String method = request.getMethod();
        String requestUrl = request.getRequestURI();

        StringBuilder output = new StringBuilder();
        String queryStr = request.getQueryString();
        output.append(method).append(" ").append(requestUrl);

        if (queryStr != null) {
            output.append("?").append(queryStr);
        }
        return output.toString();
    }

    public static String getHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return JSONUtil.toJsonStr(headers);
    }

    public static void dealIfFeignRequest(HttpServletRequest request, HttpServletResponse response) {
        if (isFeignRequest(request)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }

    /***
     * 是否来自Feign请求
     */
    public static boolean isFeignRequest(HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        return URLUtil.getPath(requestUrl).startsWith("/client/");
    }

    public static String getRequestJsonStr(HttpServletRequest request) throws IOException {
        String method = request.getMethod();
        if (method.equals("GET")) {
            if (StrUtil.isEmpty(request.getQueryString())) {
                return null;
            }
            return new String(request.getQueryString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8).replaceAll("%22", "\"");
        } else {
            return getRequestPostStr(request);
        }
    }

    public static String getRequestPostStr(HttpServletRequest request) throws IOException {
        return IoUtil.read(request.getInputStream(), StandardCharsets.UTF_8);
    }
}
