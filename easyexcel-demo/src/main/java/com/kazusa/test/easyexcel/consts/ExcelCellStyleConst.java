package com.kazusa.test.easyexcel.consts;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import static org.apache.poi.ss.usermodel.BorderStyle.THIN;

/**
 * @Description excel文件中不同部分的默认样式
 * @Author kazusa
 * @Date 2024-02-21 17:30
 */
public class ExcelCellStyleConst {

    public static final WriteCellStyle HEADER_STYLE;
    public static final WriteCellStyle VTABLE_STYLE;
    public static final WriteCellStyle HTABLE_STYLE;
    public static final WriteCellStyle FOOTER_STYLE;

    static {
        HEADER_STYLE = buildHeaderStyle();
        VTABLE_STYLE = initLeftBorderedStyle();
        HTABLE_STYLE = initLeftBorderedStyle();
        FOOTER_STYLE = initLeftBorderedStyle();
    }

    private static WriteCellStyle buildHeaderStyle() {
        WriteCellStyle result = initCenterStyle();

        WriteFont writeFont = new WriteFont();

        writeFont.setFontHeightInPoints((short) 20);
        writeFont.setBold(Boolean.TRUE);

        result.setWriteFont(writeFont);
        return result;
    }

    private static WriteCellStyle initCenterStyle() {
        WriteCellStyle result = new WriteCellStyle();

        result.setHorizontalAlignment(HorizontalAlignment.CENTER);
        result.setVerticalAlignment(VerticalAlignment.CENTER);

        return result;
    }

    private static WriteCellStyle initLeftBorderedStyle() {
        WriteCellStyle result = new WriteCellStyle();
        short bi = IndexedColors.BLACK.getIndex();
        short wi = IndexedColors.WHITE.getIndex();

        result.setHorizontalAlignment(HorizontalAlignment.LEFT);
        result.setVerticalAlignment(VerticalAlignment.CENTER);

        result.setFillForegroundColor(wi);
        result.setFillBackgroundColor(wi);

        result.setBorderBottom(THIN);
        result.setBorderTop(THIN);
        result.setBorderLeft(THIN);
        result.setBorderRight(THIN);

        result.setBottomBorderColor(bi);
        result.setTopBorderColor(bi);
        result.setLeftBorderColor(bi);
        result.setRightBorderColor(bi);

        return result;
    }
}
