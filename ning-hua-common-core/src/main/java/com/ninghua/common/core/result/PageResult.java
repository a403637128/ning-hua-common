package com.ninghua.common.core.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Derek.Fung
 * @Date 2025/7/24 11:00
 **/
@NoArgsConstructor
@Data
@Schema(description = "分页结果")
public class PageResult<T> {

    @Schema(description = "当前页")
    private Integer current;

    @Schema(description = "总页数")
    private Integer pages;

    @Schema(description = "总记录数")
    private Long total;

    @Schema(description = "每页记录数")
    private Integer size;

    @Schema(description = "记录")
    private List<T> records = new ArrayList<>();

    @Schema(description = "扩展信息")
    private String extra;

    public PageResult(Integer current, Integer pages, Long total, Integer size) {
        this.current = current;
        this.pages = pages;
        this.total = total;
        this.size = size;
    }
}
