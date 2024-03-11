package com.kazusa.test.easyexcel.writer.sheet.impl;

import com.alibaba.excel.ExcelWriter;
import com.kazusa.test.easyexcel.dto.ExcelCustomExportDTO;
import com.kazusa.test.easyexcel.entity.ExportCustomCommon;
import com.kazusa.test.easyexcel.writer.sheet.AbstractMultiSheetWriter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 多个sheet writer实现
 * @Author kazusa
 * @Date 2024-02-29 14:50
 */
@Component
public class MultiSheetWriter extends AbstractMultiSheetWriter {

    @Resource
    private SingleSheetWriter singleSheetWriter;

    @Override
    protected void writeMultiSheet(ExcelWriter writer, List<ExportCustomCommon> dataList, ExcelCustomExportDTO dto) {
        dataList.forEach(data -> singleSheetWriter.writeSingleSheet(writer, data, dto));
    }
}
