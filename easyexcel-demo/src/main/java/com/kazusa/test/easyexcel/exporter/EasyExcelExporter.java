package com.kazusa.test.easyexcel.exporter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.kazusa.test.easyexcel.anno.FileWriter;
import com.kazusa.test.easyexcel.dto.ExcelCustomExportDTO;
import com.kazusa.test.easyexcel.entity.ExcelHTableData;
import com.kazusa.test.easyexcel.entity.ExportCustomCommon;
import com.kazusa.test.easyexcel.enums.ExcelFileTypeEnum;
import com.kazusa.test.easyexcel.writer.file.ExportFileWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @Description EasyExcel导出excel
 * @Author kazusa
 * @Date 2023-05-30 17:39
 */
@Component
public class EasyExcelExporter {

    @Resource
    private ApplicationContext applicationContext;

    private final Map<ExcelFileTypeEnum, ExportFileWriter> WRITER_MAP = new HashMap<>();

    @PostConstruct
    private void init() {
        Map<String, ExportFileWriter> beans = applicationContext.getBeansOfType(ExportFileWriter.class);
        beans.forEach((name, writer) -> {
            FileWriter anno = writer.getClass().getAnnotation(FileWriter.class);
            if (anno == null) {
                throw new RuntimeException(writer.getClass().getName() + "未标注FileWriter注解");
            }
            WRITER_MAP.put(anno.fileType(), writer);
        });
    }

    public void export(OutputStream responseOs, List<ExportCustomCommon> data, ExcelCustomExportDTO dto) {
        this.checkData(data);

        ExportFileWriter writer = WRITER_MAP.get(dto.getFileType());// 获取对应的fileWriter对象
        if (writer == null) {
            throw new RuntimeException("未查找到" + dto.getFileType().getDesc() + "类型文件的writer实现");
        }

        writer.writeFile(responseOs, data, dto);
    }

    private void checkData(List<ExportCustomCommon> dataList) {
        Assert.notEmpty(dataList, () -> new RuntimeException("dataList是空"));
        dataList.forEach(data -> {
            List<? extends ExcelHTableData> listTable = data.getListTable();
            ExcelHTableData listTableSum = data.getListTableSum();

            this.checkListSumClass(listTable, listTableSum);
        });
        this.checkVTableClass(dataList);
        this.checkHTableClass(dataList);
    }

    private void checkVTableClass(List<ExportCustomCommon> dataList) {
        long classCount = dataList.stream().map(d -> d.getCommonTable().getClass()).distinct().count();
        Assert.isTrue(classCount == 1, () -> new RuntimeException("一个excel文件中的vTable只能为同一种Class"));
    }

    private void checkHTableClass(List<ExportCustomCommon> dataList) {
        long classCount = dataList.stream()
                .flatMap(d -> CollUtil.isEmpty(d.getListTable()) ? Stream.empty() : d.getListTable().stream().map(ExcelHTableData::getClass))
                .distinct().count();
        Assert.isTrue(classCount <= 1, () -> new RuntimeException("一个excel文件中的HTable只能为同一种Class"));
    }

    private void checkListSumClass(List<? extends ExcelHTableData> listTable, ExcelHTableData listTableSum) {
        Assert.isTrue(listTableSum == null
                        || listTable.get(0).getClass() == listTableSum.getClass(),
                () -> new RuntimeException("listTable和sumListData需要为同一类型！"));
    }


}
