package com.kazusa.test.easyexcel.demo.simple.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description 基本的例子
 * @Author kazusa
 * @Date 2024-03-08 17:04
 */
@Data
@AllArgsConstructor
public class SimpleDomain {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("年龄")
    private Integer age;

    @ExcelProperty("银行余额")
    private BigDecimal bankBalance;

    @ExcelProperty("描述")
    private String desc;

    @ExcelProperty("不导出的属性")
    private String exclude;

}
