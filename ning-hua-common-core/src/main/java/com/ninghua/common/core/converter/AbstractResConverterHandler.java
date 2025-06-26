package com.ninghua.common.core.converter;

import com.ninghua.common.core.request.NingHuaHeader;
import com.ninghua.common.core.util.AuthPayload;
import com.ninghua.common.core.util.RequestUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Derek.Fung
 * @Date 2025/5/28 11:49
 **/
public abstract class AbstractResConverterHandler {

    @Resource
    private ServletContext servletContext;

    @Resource
    private HttpServletRequest request;

    /**
     * 使用指定转换器转换
     * @param src 原始对象
     * @param converterClass 转换器类
     * @param extension 附加参数
     * @param <E> 原始对对象类型
     * @param <R> Result类型
     * @param <C> 转换器类型
     * @return
     */
    public <E, R, C extends ResourceConverter<E, R>>R convertBy(E src, Class<C> converterClass, Map<String, Object> extension) {
        ResourceConverter<E, R> converter = this.getResourceByClass(converterClass);
        return this.deal(src, converter, extension);
    }

    /**
     * 使用指定转换器转换
     * @param src 原始对象
     * @param builderClass 转换器类
     * @param <E> 原始对对象类型
     * @param <R> Result类型
     * @param <C> 转换器类型
     * @return
     */
    public <E, R, C extends ResourceConverter<E, R>>R convertBy(E src, Class<C> builderClass) {
        return this.convertBy(src, builderClass, Collections.emptyMap());
    }

    /**
     * 使用指定转换器转换
     * @param src 原始对象
     * @param builderClass 转换器类
     * @param <E> 原始对对象类型
     * @param <R> Result类型
     * @param <C> 转换器类型
     * @return
     */
    public <E, R, C extends ResourceConverter<E, R>> List<R> convertListBy(List<E> src, Class<C> builderClass, Map<String, Object> extension) {
        ResourceConverter<E, R> converter = this.getResourceByClass(builderClass);
        return this.deal(src, converter, extension);
    }

    /**
     * 使用指定转换器转换
     * @param src 原始对象
     * @param builderClass 转换器类
     * @param <E> 原始对对象类型
     * @param <R> Result类型
     * @param <C> 转换器类型
     * @return
     */
    public <E, R, C extends ResourceConverter<E, R>>List<R> convertListBy(List<E> src, Class<C> builderClass) {
        return convertListBy(src, builderClass, Collections.emptyMap());
    }

    private <E, R>R deal(E src, ResourceConverter<E, R> builder, Map<String, Object> extension) {
        AuthPayload auth = this.getAuth();
        NingHuaHeader appHeader = RequestUtils.getNingHuaHeaderByRequest(this.request);
        Map<String, Object> mergeMap = new HashMap<>(8);
        mergeMap.putAll(extension);
        mergeMap.putAll(this.getDefaultExtension());

        ConverterContext converterContext = new ConverterContext();
        converterContext.setPayload(auth);
        converterContext.setExtras(mergeMap);
        converterContext.setAppHeader(appHeader);
        converterContext.setConverter(this);

        return builder.build(src, converterContext);
    }

    private <E, R>List<R> deal(List<E> src, ResourceConverter<E, R> converter, Map<String, Object> extension) {
        AuthPayload auth = this.getAuth();
        NingHuaHeader appHeader = RequestUtils.getNingHuaHeaderByRequest(this.request);
        Map<String, Object> mergeMap = new HashMap<>(8);
        mergeMap.putAll(extension);
        mergeMap.putAll(this.getDefaultExtension());

        ConverterContext converterContext = new ConverterContext();
        converterContext.setPayload(auth);
        converterContext.setExtras(mergeMap);
        converterContext.setAppHeader(appHeader);
        converterContext.setConverter(this);

        return converter.build(src, converterContext);
    }

    /**
     * 获取当前token用户信息，该值对应Resource.convert的auth
     * @return
     */
    protected abstract AuthPayload getAuth();

    /**
     * 添加通用扩展信息
     * @return
     */
    protected Map<String, Object> getDefaultExtension() {
        return Collections.emptyMap();
    }

    private ResourceConverter getResourceByClass(Class converterClass) {
        try {
            ResourceConverter converter = (ResourceConverter) converterClass.newInstance();
            SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(converter, this.servletContext);
            return converter;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(converterClass.getSimpleName() + " 转换器初始化异常");
        }
    }

}
