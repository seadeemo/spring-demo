package com.kazusa.test.easyexcel.writer.sheet;

import com.alibaba.excel.ExcelWriter;
import com.kazusa.test.easyexcel.dto.ExcelCustomExportDTO;
import com.kazusa.test.easyexcel.entity.ExportCustomCommon;

import java.util.List;

/**
 * @Description sheet writer接口
 * @Author kazusa
 * @Date 2024-02-23 14:21
 */
public interface ExportSheetWriter {

    void writeSheet(ExcelWriter writer, List<ExportCustomCommon> dataList, ExcelCustomExportDTO dto);

}
