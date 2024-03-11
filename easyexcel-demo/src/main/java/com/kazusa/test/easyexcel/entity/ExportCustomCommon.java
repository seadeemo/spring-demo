package com.kazusa.test.easyexcel.entity;

import lombok.Data;

import java.util.List;

/**
 * @Description EasyExcel自定义导出 公共实体类
 * @Author kazusa
 * @Date 2023-05-30 16:10
 */
@Data
public final class ExportCustomCommon {

    /**
     * sheet/file名称
     */
    private String name;

    /**
     * 标题部分，list中一个元素表示单独占一行的一个标题
     */
    private List<String> headerTable;

    /**
     * 公共数据部分，应为一个业务对象
     */
    private ExcelVTableData commonTable;

    /**
     * 列表数据部分，应为一个业务对象列表
     */
    private List<? extends ExcelHTableData> listTable;

    /**
     * 列表数据的合计部分
     */
    private ExcelHTableData listTableSum;

    /**
     * 结尾部分 List<每一行的数据<每一个组的数据>>
     * 例如:
     * <pre>{@code
     * List.of(
     *  List.of(
     *      new String[]{"key1","value1"},
     *      new String[]{"key2","value2"}
     *  ),
     *  List.of(
     *      new String[]{"key3","value3"}
     *  )
     * )
     * }</pre>
     * 其导出excel格式为:<pre>
     * ____________________________________
     * key1 | value1 |    | key2 | value2 |
     * key3 | value3 |    |      |        |
     * ------------------------------------</pre>
     * 为空则不显示
     */
    private List<List<String[]>> footerTable;
}
