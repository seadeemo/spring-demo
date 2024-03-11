package com.kazusa.test.easyexcel.entity;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ConverterKeyBuild;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.write.metadata.holder.WriteHolder;
import com.kazusa.test.easyexcel.util.FieldUtil;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 竖直方向的Table抽象类
 * @Author kazusa
 * @Date 2024-02-19 16:41
 */
public abstract class ExcelVTableData extends ExcelData {

    public final List<List<String>> getRowList(List<String> vTableFields, int maxCol, WriteHolder currentWriteHolder) {
        if (CollUtil.isEmpty(vTableFields)) {
            return Collections.emptyList();
        }

        List<VTableBO> boList = this.getBOList(vTableFields);

        int eachRow = (maxCol + 1) / 3;

        return this.buildRowList(boList, eachRow, currentWriteHolder);
    }

    private List<List<String>> buildRowList(List<VTableBO> boList, int eachRow, WriteHolder currentWriteHolder) {
        List<List<String>> resultList = new ArrayList<>(boList.size() / eachRow + 1);
        List<String> rowList = null;

        VTableBO temp;
        Map<String, Converter> converterMap = currentWriteHolder.converterMap();
        GlobalConfiguration globalConfiguration = currentWriteHolder.globalConfiguration();
        String blank = "     ";

        for (int i = 0; i < boList.size(); i++) {
            if (i % eachRow == 0) {
                rowList = new ArrayList<>(eachRow * 3 - 1);
                resultList.add(rowList);
            }

            temp = boList.get(i);
            Converter converter = converterMap.get(ConverterKeyBuild.buildKey(temp.getValue().getClass()));

            String value = null;
            try {
                value = converter.convertToExcelData(temp.getValue(), null, globalConfiguration)
                        .getStringValue();
            } catch (Exception e) {
                e.printStackTrace();
            }

            rowList.add(temp.getFieldName());
            rowList.add(value);
            rowList.add(blank);
        }

        return resultList;
    }

    private List<VTableBO> getBOList(List<String> vTableFields) {
        List<Field> fieldList = FieldUtil.getExistedFields(vTableFields, this.getClass(), ExcelVTableData.class);

        return fieldList.stream().map(field -> {
                    VTableBO bo = new VTableBO();
                    Object fieldValue = FieldUtil.getFieldValue(field.getName(), this);
                    ExcelProperty anno = field.getAnnotation(ExcelProperty.class);

                    bo.setValue(fieldValue);
                    bo.setFieldName(field.getName());
                    bo.setOrder(anno == null ? Integer.MAX_VALUE : anno.order());

                    return bo;
                })
                .sorted(Comparator.comparingInt(b -> b.order))
                .collect(Collectors.toList());
    }

    @Data
    private final static class VTableBO {

        private String fieldName;

        private int order;

        private Object value;

    }

}
