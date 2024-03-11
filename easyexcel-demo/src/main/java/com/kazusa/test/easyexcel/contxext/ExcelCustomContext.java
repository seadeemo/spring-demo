package com.kazusa.test.easyexcel.contxext;

import cn.hutool.core.util.ObjectUtil;
import com.kazusa.test.easyexcel.anno.EasyExcelBillExport;
import com.kazusa.test.easyexcel.dto.ExcelCustomExportDTO;
import com.kazusa.test.easyexcel.entity.ExportCustomCommon;
import com.kazusa.test.easyexcel.enums.ExcelBillTypeEnum;
import com.kazusa.test.easyexcel.enums.ExcelFileTypeEnum;
import com.kazusa.test.easyexcel.exporter.EasyExcelExporter;
import com.kazusa.test.easyexcel.service.EasyExcelDataCollector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description excel导出上下文
 * @Author kazusa
 * @Date 2023-07-14 9:29
 */
@Service
@Slf4j
public class ExcelCustomContext {

    @Resource
    private ApplicationContext applicationContext;

    private final List<EasyExcelDataCollector> collectors = new ArrayList<>();// 查询数据对象的集合,通常是serviceImpl对象

    @Resource
    private EasyExcelExporter exporter;

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 下载文件
     */
    public void download(ExcelCustomExportDTO exportDTO, HttpServletResponse response, String fileName) throws IOException {
        try {
            EasyExcelDataCollector collector = this.getCollector(exportDTO.getBillType());
            //查询数据
            List<ExportCustomCommon> data = collector.getCustomExcelData(exportDTO);
            OutputStream os = response.getOutputStream();

            this.configResponse(response, exportDTO, fileName);

            long start = System.currentTimeMillis();
            exporter.export(os, data, exportDTO);
            log.info("excel导出用时：{} ms", System.currentTimeMillis() - start);
            os.flush();
            os.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println("{data\":null,msg\":\"excel export error!\"}");
        }
    }

    public void download(ExcelCustomExportDTO exportDTO, HttpServletResponse response) throws IOException {
        this.download(exportDTO, response, null);
    }

    @PostConstruct
    public void initCollectors() {
        Map<String, EasyExcelDataCollector> beans = applicationContext.getBeansOfType(EasyExcelDataCollector.class);
        collectors.addAll(beans.values());
    }

    /**
     * 获取该类型单据的数据查询对象
     */
    private EasyExcelDataCollector getCollector(ExcelBillTypeEnum type) {
        return collectors.stream()
                .filter(f -> type.equals(f.getClass().getAnnotation(EasyExcelBillExport.class).type()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("导出单据类型错误"));
    }

    /**
     * 设置响应对象
     */
    private void configResponse(HttpServletResponse response, ExcelCustomExportDTO exportDTO, String fileName) throws UnsupportedEncodingException {
        if (ObjectUtil.isEmpty(fileName)) {
            fileName = exportDTO.getBillType().getDesc() + DATE_FORMAT.format(new Date());
        }
        ExcelFileTypeEnum fileType = exportDTO.getFileType();

        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");

        if (fileType == ExcelFileTypeEnum.EXCEL) {
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + fileType.getSuffix());
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        } else if (fileType == ExcelFileTypeEnum.ZIP) {
            response.setHeader("Content-Type", "application/octet-stream");
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + fileType.getSuffix());
        }
    }
}
