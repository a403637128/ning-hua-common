package com.ninghua.common.database.plugins;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ParameterUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * @Author Derek.Fung
 * @Date 2025/4/30 16:55
 **/
@Getter
@Setter
@NoArgsConstructor
public class NingHuaPaginationInnerInterceptor extends PaginationInnerInterceptor {
    /**
     * 数据库类型
     */
    private DbType dbType;

    /**
     * 方言实现类
     */
    private IDialect dialect;


    public NingHuaPaginationInnerInterceptor(DbType dbType) {
        this.dbType = dbType;
    }

    public NingHuaPaginationInnerInterceptor(IDialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds,
                            ResultHandler resultHandler, BoundSql boundSql) {
        IPage<?> page = ParameterUtils.findPage(parameter).orElse(null);
        // size 小于 0 直接设置为 0 , 即不查询任何数据
        if (null != page && page.getSize() < 0) {
            page.setSize(0);
        }
        super.beforeQuery(executor, ms, page, rowBounds, resultHandler, boundSql);
    }
}
