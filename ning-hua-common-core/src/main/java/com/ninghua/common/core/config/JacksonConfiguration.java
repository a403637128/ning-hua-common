package com.ninghua.common.core.config;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ninghua.common.core.jackson.FormatJavaTimeModule;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @Author Derek.Fung
 * @Date 2025/6/6 14:19
 **/
@AutoConfiguration
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class JacksonConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            // 设置地区为中国，保证序列化/反序列化与地区相关的格式符合中文环境
            builder.locale(Locale.CHINA);
            // 设置时区为系统默认时区，确保时间的序列化和反序列化使用正确的时区
            builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            // 设置日期时间的默认格式为 yyyy-MM-dd HH:mm:ss
            builder.simpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
            // 将 Long 类型字段序列化为字符串，防止前端接收精度丢失问题
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            // 添加自定义的时间模块以支持 Java 8 的新时间API（如 LocalDateTime、LocalDate 等）
            // 并应用统一的格式化规则（如 yyyy-MM-dd HH:mm:ss）
            builder.modules(new FormatJavaTimeModule());
        };
    }

    /**
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // 注册自定义序列化器，将 Long 类型序列化为字符串
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
        mapper.registerModule(module);

        return mapper;
    }
    **/
}
