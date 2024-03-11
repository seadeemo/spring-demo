package com.kazusa.test.easyexcel.writer.file.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.kazusa.test.easyexcel.anno.FileWriter;
import com.kazusa.test.easyexcel.dto.ExcelCustomExportDTO;
import com.kazusa.test.easyexcel.entity.ExcelHTableData;
import com.kazusa.test.easyexcel.entity.ExportCustomCommon;
import com.kazusa.test.easyexcel.enums.ExcelFileTypeEnum;
import com.kazusa.test.easyexcel.writer.file.ExportFileWriter;
import com.kazusa.test.easyexcel.writer.sheet.impl.MultiSheetWriter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

/**
 * @Description excel文件的file writer
 * @Author kazusa
 * @Date 2024-02-23 14:24
 */
@Component
@FileWriter(fileType = ExcelFileTypeEnum.EXCEL)
public class ExcelFileWriter implements ExportFileWriter {

    @Resource
    private MultiSheetWriter multiSheetWriter;

    @Override
    public void writeFile(OutputStream responseOs, List<ExportCustomCommon> dataList, ExcelCustomExportDTO dto) {
        Class<? extends ExcelHTableData> hTableClass = this.getHTableClass(dataList);
        ExcelWriter writer = EasyExcel.write(responseOs, hTableClass)
                .needHead(Boolean.FALSE)// 不需要header
                .excelType(ExcelTypeEnum.XLSX)// 使用xlsx
                .autoCloseStream(Boolean.FALSE)// 不自动关闭流
                .build();
        multiSheetWriter.writeSheet(writer, dataList, dto);
        writer.finish();
    }

    public void writeSingleSheetFile(OutputStream responseOs, ExportCustomCommon data, ExcelCustomExportDTO dto) {
        this.writeFile(responseOs, Collections.singletonList(data), dto);
    }

    private Class<? extends ExcelHTableData> getHTableClass(List<ExportCustomCommon> data) {
        return data.stream().flatMap(d -> d.getListTable().stream().map(ExcelHTableData::getClass))
                .findAny().orElse(null);
    }
}
