package com.ninghua.common.core.converter;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Derek.Fung
 * @Date 2025/5/28 13:53
 **/
public interface ResourceConverter<T, R> {

    default R build(T obj, ConverterContext context) {
        return null;
    }

    default List<R> build(List<T> obj, ConverterContext context) {
        return CollectionUtils.isEmpty(obj) ? Collections.emptyList() : obj.stream().map((e) -> this.build(e, context)).collect(Collectors.toList());
    }

}
