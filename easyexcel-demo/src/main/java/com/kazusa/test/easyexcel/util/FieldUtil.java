package com.kazusa.test.easyexcel.util;

import cn.hutool.core.lang.Assert;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 反射操作属性工具类
 * @Author kazusa
 * @Date 2023-07-12 16:23
 */
public class FieldUtil {

    public static List<Field> getAllField(Class<?> clazz, Class<?> superClass) {
        List<Field> resultList = new ArrayList<>();
        List<String> fieldNameList = new ArrayList<>();
        superClass = superClass == null ? Object.class : superClass;
        while (clazz != null && !clazz.getName().toLowerCase().equals(superClass.getName())) {// 当父类为null的时候说明到达了Object类.
            List<Field> subFields = Arrays.asList(clazz.getDeclaredFields());
            List<Field> list = subFields.stream().filter(f -> !fieldNameList.contains(f.getName())).collect(Collectors.toList());
            List<String> nameList = list.stream().map(Field::getName).collect(Collectors.toList());
            resultList.addAll(list);
            fieldNameList.addAll(nameList);
            clazz = clazz.getSuperclass();
        }
        return resultList;
    }

    public static List<String> getExistedFieldStrings(List<String> fieldsList, Class<?> listDataClass, Class<?> superClass) {
        List<String> existedFields = getAllField(listDataClass, superClass).stream().map(Field::getName).collect(Collectors.toList());
        return fieldsList.stream().filter(existedFields::contains)
                .collect(Collectors.toList());
    }

    public static List<Field> getExistedFields(List<String> fieldsList, Class<?> listDataClass, Class<?> superClass) {
        List<Field> existedFields = getAllField(listDataClass, superClass);
        return existedFields.stream().filter(ef -> fieldsList.contains(ef.getName())).collect(Collectors.toList());
    }

    public static Object getFieldValue(String fieldName, Object object) {
        PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(object.getClass(), fieldName);
        Assert.notNull(pd, () -> new RuntimeException("PropertyDescriptor is null"));
        assert pd != null;
        Method readMethod = pd.getReadMethod();
        try {
            return readMethod.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
