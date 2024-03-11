package com.kazusa.test.easyexcel.writer.file.impl;

import com.kazusa.test.easyexcel.anno.FileWriter;
import com.kazusa.test.easyexcel.dto.ExcelCustomExportDTO;
import com.kazusa.test.easyexcel.entity.ExportCustomCommon;
import com.kazusa.test.easyexcel.enums.ExcelFileTypeEnum;
import com.kazusa.test.easyexcel.writer.file.ExportFileWriter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description zip文件的file writer
 * @Author kazusa
 * @Date 2024-02-23 14:25
 */
@Component
@FileWriter(fileType = ExcelFileTypeEnum.ZIP)
public class ZipFileWriter implements ExportFileWriter {

    @Resource
    private ExcelFileWriter excelWriter;

    @Resource
    private ThreadPoolTaskExecutor poolExecutor;

    private static final int THREAD_COUNT;

    private static final int MULTI_THEAD_THRESHOLD = 20;

    private static final boolean useMultiThread;

    static {
        int maxThreadCount = Runtime.getRuntime().availableProcessors();
        THREAD_COUNT = Math.max(maxThreadCount, 4) / 4;
        useMultiThread = THREAD_COUNT > 1;
    }

    @Override
    public void writeFile(OutputStream responseOs, List<ExportCustomCommon> dataList, ExcelCustomExportDTO dto) {
        ZipOutputStream zos = new ZipOutputStream(responseOs);

        if (useMultiThread)
            multiThreadWrite(dataList, dto, zos);
        else
            singleThreadWrite(dataList, dto, zos);
    }

    private void singleThreadWrite(List<ExportCustomCommon> dataList, ExcelCustomExportDTO dto, ZipOutputStream zos) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            for (ExportCustomCommon data : dataList) {
                excelWriter.writeSingleSheetFile(bos, data, dto);
                zos.putNextEntry(new ZipEntry(data.getName() + ExcelFileTypeEnum.EXCEL.getSuffix()));
                zos.write(bos.toByteArray());
                zos.closeEntry();
                bos.reset();
            }
            zos.flush();
            zos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void multiThreadWrite(List<ExportCustomCommon> dataList, ExcelCustomExportDTO dto, ZipOutputStream zos) {
        List<List<ExportCustomCommon>> taskList = this.splitTask(dataList);
        int threadCount = taskList.size();
        Object lock = new Object();
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            poolExecutor.submit(new WriteOneExcelFile(zos, lock, latch, excelWriter, taskList.get(i), dto));
        }
        try {
            latch.await();
            zos.flush();
            zos.close();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<List<ExportCustomCommon>> splitTask(List<ExportCustomCommon> dataList) {
        int i = 0, size = dataList.size(), theadCount = Math.min(size, THREAD_COUNT), count = size / theadCount;
        if (count < MULTI_THEAD_THRESHOLD) {
            return Collections.singletonList(dataList);
        }
        List<List<ExportCustomCommon>> taskList = new ArrayList<>(theadCount);
        for (int j = 0; j < theadCount; j++) {
            if (j == theadCount - 1) {
                taskList.add(dataList.subList(i, size));
            } else {
                taskList.add(dataList.subList(i, i += count));
            }
        }
        return taskList;
    }

    private static class WriteOneExcelFile implements Runnable {

        private final ZipOutputStream ZOS;

        private final Object LOCK;

        private final CountDownLatch LATCH;

        private final ExcelFileWriter WRITER;

        private final List<ExportCustomCommon> DATA_LIST;

        private final ExcelCustomExportDTO DTO;

        public WriteOneExcelFile(ZipOutputStream zos, Object lock, CountDownLatch latch,
                                 ExcelFileWriter excelWriter, List<ExportCustomCommon> dataList, ExcelCustomExportDTO dto) {
            this.ZOS = zos;
            this.LOCK = lock;
            this.LATCH = latch;
            this.WRITER = excelWriter;
            this.DATA_LIST = dataList;
            this.DTO = dto;
        }

        @Override
        public void run() {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                for (ExportCustomCommon data : DATA_LIST) {
                    WRITER.writeSingleSheetFile(bos, data, DTO);
                    synchronized (LOCK) {
                        ZOS.putNextEntry(new ZipEntry(data.getName() + ExcelFileTypeEnum.EXCEL.getSuffix()));
                        ZOS.write(bos.toByteArray());
                        ZOS.closeEntry();
                    }
                    bos.reset();
                }
            } catch (IOException ignored) {
            } finally {
                LATCH.countDown();
            }
        }
    }
}
