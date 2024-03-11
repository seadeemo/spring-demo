package com.kazusa.test.easyexcel.writer.file;

import com.kazusa.test.easyexcel.dto.ExcelCustomExportDTO;
import com.kazusa.test.easyexcel.entity.ExportCustomCommon;

import java.io.OutputStream;
import java.util.List;

/**
 * @Description file writer接口
 * @Author kazusa
 * @Date 2024-02-23 14:21
 */
public interface ExportFileWriter {

    void writeFile(OutputStream responseOs, List<ExportCustomCommon> dataList, ExcelCustomExportDTO dto);

}
