package com.kazusa.test.easyexcel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 导出文件类型枚举
 * @Author kazusa
 * @Date 2024-02-19 16:58
 */
@AllArgsConstructor
@Getter
public enum ExcelFileTypeEnum {

    ZIP(".zip", "压缩包"),
    EXCEL(".xlsx", "单/多sheet的excel");

    private final String suffix;

    private final String desc;
}
