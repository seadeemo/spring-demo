package com.kazusa.test.easyexcel.demo.web.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.kazusa.test.easyexcel.entity.ExcelHTableData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @Description
 * @Author kazusa
 * @Date 2023-12-04 13:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExcelTestHTable extends ExcelHTableData {

    @ExcelProperty(
            value = "属性1-String",
            order = 0
    )
    private String field1;

    @ExcelProperty(
            value = "属性2-BigDecimal",
            order = 4
    )
    private BigDecimal field2;

    @ExcelProperty(
            value = "属性3-Integer",
            order = 6
    )
    private Integer field3;

    @ExcelProperty(
            value = "属性4-Boolean",
            order = 9
    )
    private Boolean field4;

    @ExcelProperty(
            value = "属性5-String",
            order = 0
    )
    private String field5;

    @ExcelProperty(
            value = "属性6-String",
            order = 20
    )
    private String field6;

    @ExcelProperty(
            value = "属性7-String",
            order = 11
    )
    private String field7;

    @ExcelProperty(
            value = "属性8-String",
            order = 12
    )
    private String field8;

    @ExcelProperty(
            value = "属性9-String",
            order = 13
    )
    private String field9;

}
