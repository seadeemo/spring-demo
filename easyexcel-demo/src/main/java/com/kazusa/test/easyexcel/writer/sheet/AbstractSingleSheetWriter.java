package com.kazusa.test.easyexcel.writer.sheet;

import com.alibaba.excel.ExcelWriter;
import com.kazusa.test.easyexcel.dto.ExcelCustomExportDTO;
import com.kazusa.test.easyexcel.entity.ExportCustomCommon;

import java.util.List;

/**
 * @Description 单个sheet writer抽象类
 * @Author kazusa
 * @Date 2024-02-29 14:45
 */
public abstract class AbstractSingleSheetWriter implements ExportSheetWriter {

    @Override
    public void writeSheet(ExcelWriter writer, List<ExportCustomCommon> dataList, ExcelCustomExportDTO dto) {
        this.writeSingleSheet(writer, dataList.get(0), dto);
    }

    protected abstract void writeSingleSheet(ExcelWriter writer, ExportCustomCommon data, ExcelCustomExportDTO dto);
}
