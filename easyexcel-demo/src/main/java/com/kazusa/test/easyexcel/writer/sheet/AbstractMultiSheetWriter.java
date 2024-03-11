package com.kazusa.test.easyexcel.writer.sheet;

import com.alibaba.excel.ExcelWriter;
import com.kazusa.test.easyexcel.dto.ExcelCustomExportDTO;
import com.kazusa.test.easyexcel.entity.ExportCustomCommon;

import java.util.List;

/**
 * @Description 多个sheet writer抽象类
 * @Author kazusa
 * @Date 2024-02-29 14:49
 */
public abstract class AbstractMultiSheetWriter implements ExportSheetWriter {

    @Override
    public void writeSheet(ExcelWriter writer, List<ExportCustomCommon> dataList, ExcelCustomExportDTO dto) {
        this.writeMultiSheet(writer, dataList, dto);
    }

    protected abstract void writeMultiSheet(ExcelWriter writer, List<ExportCustomCommon> dataList, ExcelCustomExportDTO dto);
}
