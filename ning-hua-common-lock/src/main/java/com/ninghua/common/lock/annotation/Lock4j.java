package com.ninghua.common.lock.annotation;

import com.ninghua.common.lock.LockFailureStrategy;
import com.ninghua.common.lock.LockKeyBuilder;
import com.ninghua.common.lock.core.LockExecutor;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * @Author Derek.Fung
 * @Date 2025/6/24 11:59
 **/
@Repeatable(Lock4j.List.class)
@Target(value = {ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Lock4j {

    /**
     * 应用条件表达式，当执行结果为{@code true}或{@code 'true'}时，才会执行锁操作
     *
     * @return 名称
     */
    String condition() default "";

    /**
     * 用于多个方法锁同一把锁 可以理解为锁资源名称 为空则会使用 包名+类名+方法名
     *
     * @return 名称
     */
    String name() default "";

    /**
     * @return lock 执行器
     */
    Class<? extends LockExecutor> executor() default LockExecutor.class;

    /**
     * support SPEL expresion 锁的key = name + keys
     *
     * @return KEY
     */
    String[] keys() default "";

    /**
     * @return 过期时间 单位：毫秒
     * <pre>
     *     过期时间一定是要长于业务的执行时间. 未设置则为默认时间30秒 默认值：{@link Lock4jProperties#expire}
     * </pre>
     */
    long expire() default -1;

    /**
     * @return 获取锁超时时间 单位：毫秒
     * <pre>
     *     结合业务,建议该时间不宜设置过长,特别在并发高的情况下. 未设置则为默认时间3秒 默认值：{@link Lock4jProperties#acquireTimeout}
     * </pre>
     */
    long acquireTimeout() default -1;

    /**
     * 业务方法执行完后（方法内抛异常也算执行完）自动释放锁，如果为false，锁将不会自动释放直至到达过期时间才释放 {@link com.baomidou.lock.annotation.Lock4j#expire()}
     *
     * @return 是否自动释放锁
     */
    boolean autoRelease() default true;

    /**
     * 失败策略
     *
     * @return LockFailureStrategy
     */
    Class<? extends LockFailureStrategy> failStrategy() default LockFailureStrategy.class;

    /**
     * key生成器策略
     *
     * @return LockKeyBuilder
     */
    Class<? extends LockKeyBuilder> keyBuilderStrategy() default LockKeyBuilder.class;

    /**
     * 获取顺序，值越小越先执行
     *
     * @return 顺序值
     */
    int order() default Ordered.LOWEST_PRECEDENCE;

    @Target(value = {ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(value = RetentionPolicy.RUNTIME)
    @Inherited
    @Documented
    @interface List {
        Lock4j[] value();
    }
}
