package com.kazusa.test.easyexcel.writer.table;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterTableBuilder;
import com.alibaba.excel.write.merge.OnceAbsoluteMergeStrategy;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.kazusa.test.easyexcel.entity.ExcelHTableData;
import com.kazusa.test.easyexcel.entity.ExcelVTableData;
import com.kazusa.test.easyexcel.entity.ExportCustomCommon;
import com.kazusa.test.easyexcel.util.FieldUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.kazusa.test.easyexcel.consts.ExcelCellStyleConst.*;

/**
 * @Description table writer实现
 * @Author kazusa
 * @Date 2024-02-29 14:53
 */
public class ExportTableWriter {

    private final int MAX_COL;

    private static final int DEFAULT_MAX_COL = 8;// 默认最大列数,刚好是3组key-value-blank的长度

    private final ExportCustomCommon data;

    private final List<String> verticalTableFields;

    private final List<String> horizontalTableFields;

    private final WriteSheet sheet;

    private final ExcelWriter writer;

    public ExportTableWriter(ExcelWriter writer, ExportCustomCommon data, WriteSheet sheet,
                             List<String> verticalTableFields, List<String> horizontalTableFields) {
        this.writer = writer;
        this.data = data;
        this.verticalTableFields = verticalTableFields;
        this.horizontalTableFields = horizontalTableFields;
        this.sheet = sheet;
        this.MAX_COL = this.getMaxCol();
    }

    public void writeTable() {
        writeHeader();
        writeVTable();
        writeHTable();
        writeFooter();
    }

    /**
     * 写入header
     */
    private void writeHeader() {
        List<String> headerTable = data.getHeaderTable();

        if (CollUtil.isEmpty(headerTable)) {
            return;
        }

        ExcelWriterTableBuilder tableBuilder = EasyExcel.writerTable(1)
                .registerWriteHandler(new HorizontalCellStyleStrategy(null, HEADER_STYLE))// 统一样式
                .needHead(Boolean.FALSE);// 不需要header

        for (int i = 0; i < headerTable.size(); i++) {// 合并单元格
            tableBuilder.registerWriteHandler(new OnceAbsoluteMergeStrategy(i, i, 0, MAX_COL));
        }

        WriteTable header = tableBuilder.build();

        List<List<String>> data = headerTable.stream().map(Collections::singletonList).collect(Collectors.toList());
        data.add(Collections.emptyList());// 空行

        writer.write(data, sheet, header);
    }

    /**
     * 写入竖直方向的table
     */
    private void writeVTable() {
        ExcelVTableData vTable = data.getCommonTable();

        // 将对象分解成 key-value-blank 的形式
        List<List<String>> rowList = vTable.getRowList(verticalTableFields, MAX_COL, writer.writeContext().currentWriteHolder());
        rowList.add(Collections.emptyList());// 空行

        WriteTable table = EasyExcel.writerTable(2)
                .needHead(Boolean.FALSE)// 不需要header
                .registerWriteHandler(new HorizontalCellStyleStrategy(null, VTABLE_STYLE))
                .build();
        writer.write(rowList, sheet, table);
    }

    /**
     * 写入水平方向的table
     */
    private void writeHTable() {
        List<? extends ExcelHTableData> hTable = data.getListTable();

        WriteTable table = EasyExcel.writerTable(3)
                .needHead(Boolean.TRUE)
                .includeColumnFiledNames(horizontalTableFields)// 需要的属性
                .registerWriteHandler(new HorizontalCellStyleStrategy(HTABLE_STYLE, HTABLE_STYLE))
                .build();
        for (int i = 0; i < hTable.size(); i++) {
            hTable.get(i).setNo(String.valueOf(i + 1));
        }

        writer.write(hTable, sheet, table);
    }

    /**
     * 写入footer
     */
    private void writeFooter() {
        if (this.checkFooterEmpty()) {
            return;
        }

        List<List<String[]>> footerTable = data.getFooterTable();


        WriteTable table = EasyExcel.writerTable(4)
                .needHead(Boolean.FALSE)// 不需要header
                .registerWriteHandler(new HorizontalCellStyleStrategy(null, FOOTER_STYLE))
                .build();

        // footer的自动对齐、补足空白单元格
        List<List<String>> data = this.handleFooter(footerTable);
        data.add(0, Collections.emptyList());// 空行

        writer.write(data, sheet, table);
    }

    private List<List<String>> handleFooter(List<List<String[]>> footerTable) {
        String blank = "    ";
        int[] blockLength = getBlockLength(footerTable);

        return rebuildFooter(footerTable, blockLength, blank);
    }

    private static List<List<String>> rebuildFooter(List<List<String[]>> footerTable, int[] blockLength, String blank) {
        List<List<String>> resultList = new ArrayList<>(footerTable.size());
        for (List<String[]> row : footerTable) {
            List<String> rowList = new ArrayList<>();
            resultList.add(rowList);
            for (int blockCount = 0; blockCount < row.size(); blockCount++) {
                String[] block = row.get(blockCount);
                int length = blockLength[blockCount];
                for (int i = 0; i < length; i++) {
                    if (i < block.length) {
                        rowList.add(block[i]);
                    } else {
                        rowList.add(blank);
                    }
                }
            }
        }
        return resultList;
    }

    private static int[] getBlockLength(List<List<String[]>> footerTable) {
        int maxBlockCount = footerTable.stream().map(List::size).max(Integer::compareTo).orElse(3);
        int[] blockLength = new int[maxBlockCount];

        for (int i = 0; i < blockLength.length; i++) {
            for (List<String[]> footerRow : footerTable) {
                if (i < footerRow.size()) {
                    String[] block = footerRow.get(i);
                    blockLength[i] = Math.max(blockLength[i], block.length + 1);
                }
            }
        }
        return blockLength;
    }

    private boolean checkFooterEmpty() {
        return data.getFooterTable().stream().flatMap(Collection::stream).noneMatch(s -> s.length > 0);
    }

    private int getMaxCol() {
        List<? extends ExcelHTableData> listTable = data.getListTable();
        Class<? extends ExcelHTableData> listTableClass = listTable.get(0).getClass();

        List<String> existedFields = FieldUtil.getExistedFieldStrings(horizontalTableFields, listTableClass, ExcelHTableData.class);
        return Math.max(existedFields.size(), DEFAULT_MAX_COL);
    }

}
