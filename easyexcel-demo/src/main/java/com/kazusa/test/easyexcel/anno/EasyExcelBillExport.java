package com.kazusa.test.easyexcel.anno;

import com.kazusa.test.easyexcel.enums.ExcelBillTypeEnum;

import java.lang.annotation.*;

/**
 * @Description 单据表单导出注解
 * @Author kazusa
 * @Date 2023-06-01 16:20
 */
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EasyExcelBillExport {

    /**
     * @return 导出的类型
     */
    ExcelBillTypeEnum type() default ExcelBillTypeEnum.DEMO_TEST;

}
