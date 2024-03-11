package com.kazusa.test.easyexcel.dto;

import com.kazusa.test.easyexcel.enums.ExcelBillTypeEnum;
import com.kazusa.test.easyexcel.enums.ExcelFileTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * @Description excel导出请求DTO
 * @Author kazusa
 * @Date 2023-05-30 9:58
 */
@Data
public class ExcelCustomExportDTO {

    /**
     * 单据编号
     */
    private List<String> snList;

    /**
     * 单据类型
     */
    private ExcelBillTypeEnum billType;

    /**
     * 导出文件类型
     */
    private ExcelFileTypeEnum fileType;

    /**
     * 竖直方向表格需要导出的属性
     */
    private List<String> verticalTableFields;

    /**
     * 水平方向表格需要导出的属性
     */
    private List<String> horizontalTableFields;

}
