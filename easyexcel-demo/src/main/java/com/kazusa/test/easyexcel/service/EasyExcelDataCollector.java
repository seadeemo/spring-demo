package com.kazusa.test.easyexcel.service;

import com.kazusa.test.easyexcel.dto.ExcelCustomExportDTO;
import com.kazusa.test.easyexcel.entity.ExportCustomCommon;

import java.io.IOException;
import java.util.List;

/**
 * @Description 业务service获取数据接口
 * @Author kazusa
 * @Date 2023-07-14 9:28
 */
public interface EasyExcelDataCollector {

    List<ExportCustomCommon> getCustomExcelData(ExcelCustomExportDTO exportDTO) throws IOException;
}
