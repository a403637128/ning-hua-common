package com.ninghua.common.core.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Derek.Fung
 * @Date 2025/7/23 14:15
 **/
@lombok.Data
@Schema(description = "字典项")
@NoArgsConstructor
public class DictItemResult {

    @Schema(description = "字典项ID")
    private Long id;

    @Schema(description = "字典项文本")
    private String itemText;

    @Schema(description = "字典项值")
    private String itemValue;

    @Schema(description = "字典项描述")
    private String description;

    public DictItemResult(String desc, String name) {
        this.description = desc;
        this.itemText = desc;
        this.itemValue = name;
    }
}
