package com.kazusa.test.easyexcel.demo.simple.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.merge.OnceAbsoluteMergeStrategy;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.kazusa.test.easyexcel.demo.simple.domain.SimpleDomain;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Description 基本的例子
 * @Author kazusa
 * @Date 2024-03-08 17:07
 */
public class SimpleTestService {

    private static final String FILE_LOCATION = "D:/test.xlsx";

    public void export() {
        // 构建一个自动列宽handler
        WriteHandler autoResize = new LongestMatchColumnWidthStyleStrategy();

        // 构建writer
        ExcelWriter writer = EasyExcel.write(FILE_LOCATION, SimpleDomain.class)
                .autoCloseStream(Boolean.FALSE)
                .excelType(ExcelTypeEnum.XLSX)
                .needHead(Boolean.TRUE)
                .build();

        // 构建sheet,每一个sheet使用一个handler
        WriteSheet sheet = EasyExcel.writerSheet("test-sheet-name")
                .registerWriteHandler(autoResize)
                .needHead(Boolean.FALSE)
                .build();

        // 构建标题
        List<List<String>> title = Collections.singletonList(Collections.singletonList("标题1"));
        WriteTable titleTable = EasyExcel.writerTable(0)
                .needHead(Boolean.FALSE)
                .registerWriteHandler(new OnceAbsoluteMergeStrategy(0, 0, 0, 7))// 合并单元格
                .build();
        // 写入标题
        writer.write(title, sheet, titleTable);

        // 构建两个表的数据：一个竖直方向排列，一个水平方向排列
        String blank = "     ";
        List<List<String>> vTableData = Arrays.asList(
                Arrays.asList("编号:", "4536251", blank, "时间:", "2024-01-02", blank, "操作人:", "ADMIN"),
                Arrays.asList("操作时间:", "2024-01-01"),
                Arrays.asList("描述信息:", "描述描述描述描述描述描述描述描述"),
                Collections.singletonList("") // 添加一个空行
        );

        List<SimpleDomain> hTableData = Arrays.asList(
                new SimpleDomain(1L, "张三", 24, new BigDecimal("22.22"), "张三的描述很短", "不导出"),
                new SimpleDomain(2L, "李四", 30, new BigDecimal("22222.22"), "李四的描述很短", "不导出"),
                new SimpleDomain(3L, "王五", 56, new BigDecimal("22333.22"), "王五的描述很短", "不导出"),
                new SimpleDomain(4L, "赵六", 16, new BigDecimal("22.2244"), "赵六的描述很短", "不导出"),
                new SimpleDomain(5L, "钱七", 23, BigDecimal.ZERO, "钱七的描述很短才怪钱七的描述很短才怪钱七的描述很短才怪钱七的描述很短才怪钱七的描述很短才怪钱七的描述很短才怪", "不导出")
        );

        // 写入两个表：一个竖直方向排列，一个水平方向排列
        WriteTable vTable = EasyExcel.writerTable(1).build();
        writer.write(vTableData, sheet, vTable);

        WriteTable hTable = EasyExcel.writerTable(2)// 需要指定tableNo,head才能正常导出
                .includeColumnFiledNames(Arrays.asList("id", "name", "age", "bankBalance", "desc"))// 选择某些属性导出
//                .excludeColumnFiledNames()// 也可以排除某些属性
                .needHead(Boolean.TRUE)
                .build();
        writer.write(hTableData, sheet, hTable);

        writer.finish();
    }

    public static void main(String[] args) {
        new SimpleTestService().export();
    }

}
