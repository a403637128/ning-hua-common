package com.ninghua.common.core.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author Derek.Fung
 * @Date 2025/7/30 14:02
 **/
@Data
@Schema(description = "分享结果")
public class ShareResult {

    @Schema(description = "分享页面地址")
    private String sharePageUrl;

    @Schema(description = "分享图片")
    private String shareImage;

    @Schema(description = "微信小程序路径")
    private String wxMaPath;

    @Schema(description = "微信小程序二维码")
    private String shareQrCode;

    @Schema(description = "分享标题")
    private String shareTitle;
}

