package com.ninghua.common.core.result;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Derek.Fung
 * @Date 2025/7/23 14:15
 **/
@lombok.Data
@Schema(description = "通用字典返回")
public class DictResult {

    @Schema(description = "字典项ID")
    private Long id;

    @Schema(description = "字典项名称")
    private String name;

    @Schema(description = "字典项编码")
    private String code;

    @Schema(description = "字典项描述")
    private String description;

    @Schema(description = "字典项子项")
    private List<DictItemResult> items = new ArrayList<>();
}
