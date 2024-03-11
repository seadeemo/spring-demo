package com.kazusa.test.easyexcel.anno;

import com.kazusa.test.easyexcel.enums.ExcelFileTypeEnum;

import java.lang.annotation.*;

/**
 * @Description ExportFileWriter标注的注解, 用来区分导出文件的类型
 * @Author kazusa
 * @Date 2024-02-23 14:29
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileWriter {

    /**
     * @return 导出文件类型
     */
    ExcelFileTypeEnum fileType();

}
