package com.kazusa.test.easyexcel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description 水平方向的Table抽象类
 * @Author kazusa
 * @Date 2024-02-19 16:41
 */
@Setter
@Getter
public abstract class ExcelHTableData extends ExcelData {

    /**
     * 序号
     */
    @ExcelProperty(value = "序号", order = Integer.MIN_VALUE)
    private String no;

}
