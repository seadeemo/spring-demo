package com.kazusa.test.easyexcel.writer.sheet.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.kazusa.test.easyexcel.dto.ExcelCustomExportDTO;
import com.kazusa.test.easyexcel.entity.ExportCustomCommon;
import com.kazusa.test.easyexcel.handler.AutoResizeHandler;
import com.kazusa.test.easyexcel.writer.sheet.AbstractSingleSheetWriter;
import com.kazusa.test.easyexcel.writer.table.ExportTableWriter;
import org.springframework.stereotype.Component;

/**
 * @Description 单个sheet writer实现
 * @Author kazusa
 * @Date 2024-02-29 14:48
 */
@Component
public class SingleSheetWriter extends AbstractSingleSheetWriter {

    @Override
    protected void writeSingleSheet(ExcelWriter writer, ExportCustomCommon data, ExcelCustomExportDTO dto) {
        WriteSheet sheet = EasyExcel.writerSheet()
                .sheetName(data.getName())
                .registerWriteHandler(new AutoResizeHandler())// 每个sheet使用一个自动列宽处理器
                .build();

        ExportTableWriter tableWriter = new ExportTableWriter(writer, data, sheet, dto.getVerticalTableFields(), dto.getHorizontalTableFields());
        tableWriter.writeTable();
    }
}
