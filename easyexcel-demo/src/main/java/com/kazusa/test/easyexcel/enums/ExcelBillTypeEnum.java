package com.kazusa.test.easyexcel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description excel导出单据类型枚举
 * @Author kazusa
 * @Date 2023-06-01 16:21
 */
@Getter
@SuppressWarnings("all")
@AllArgsConstructor
public enum ExcelBillTypeEnum {

    DEMO_TEST("测试单据", "demo_test"),
    ;

    private String desc;

    private String lowercase;
}
