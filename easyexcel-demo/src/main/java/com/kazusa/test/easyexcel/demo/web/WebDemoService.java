package com.kazusa.test.easyexcel.demo.web;

import com.kazusa.test.easyexcel.anno.EasyExcelBillExport;
import com.kazusa.test.easyexcel.demo.web.domain.ExcelTestHTable;
import com.kazusa.test.easyexcel.demo.web.domain.ExcelTestVTable;
import com.kazusa.test.easyexcel.dto.ExcelCustomExportDTO;
import com.kazusa.test.easyexcel.entity.ExportCustomCommon;
import com.kazusa.test.easyexcel.enums.ExcelBillTypeEnum;
import com.kazusa.test.easyexcel.service.EasyExcelDataCollector;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description
 * @Author kazusa
 * @Date 2023-12-04 13:10
 */
@Service
@EasyExcelBillExport(type = ExcelBillTypeEnum.DEMO_TEST)
public class WebDemoService implements EasyExcelDataCollector {

    @Override
    public List<ExportCustomCommon> getCustomExcelData(ExcelCustomExportDTO exportDTO) {
        List<ExportCustomCommon> resultList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ExportCustomCommon excel = new ExportCustomCommon();
            excel.setName("测试file or sheet名称" + i);
            excel.setHeaderTable(Arrays.asList("title1" + i, "title2" + i));
            excel.setFooterTable(
                    Arrays.asList(
                            Arrays.asList(new String[]{"key1:", "value1", "value1"}, new String[]{"key2:", "value2", "value1"}),
                            Arrays.asList(new String[]{"key3:", "value3"}, new String[]{"key4:", "value4"})
                    )
            );

            ExcelTestVTable vTable = new ExcelTestVTable();
            vTable.setField1("setField1");
            vTable.setField2(new BigDecimal("123.00"));
            vTable.setField3(i);
            vTable.setField4(i % 2 == 0);
            vTable.setField5("setField1");
            vTable.setField6("setField1");
            vTable.setField7("setField1");
            vTable.setField8("setField1");
            vTable.setField9("setField1333333" + i);
            excel.setCommonTable(vTable);

            List<ExcelTestHTable> hTable = new ArrayList<>();
            for (int j = 0; j < 2500; j++) {
                ExcelTestHTable h = new ExcelTestHTable();
                h.setField1("setField1");
                h.setField2(new BigDecimal("123.00"));
                h.setField3(i);
                h.setField4(i % 2 == 0);
                h.setField5("setField1");
                h.setField6("setField1");
                h.setField7("setField1");
                h.setField8("setField1");
                h.setField9("setField1333333" + i);
                hTable.add(h);
            }
            excel.setListTable(hTable);

            resultList.add(excel);
        }
        return resultList;
    }

}
