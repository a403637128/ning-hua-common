package com.ninghua.common.core.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author Derek.Fung
 * @Date 2025/7/24 11:04
 **/
@Data
@Schema(description = "分页参数")
public class Pageable {

    @Schema(description = "每页记录数")
    private Integer pageSize = 20;

    @Schema(description = "当前页")
    private Integer pageNumber = 1;
}
