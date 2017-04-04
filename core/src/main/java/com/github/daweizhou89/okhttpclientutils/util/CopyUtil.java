package com.github.daweizhou89.okhttpclientutils.util;

import com.github.daweizhou89.okhttpclientutils.DebugLog;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhoudawei on 2017/3/2.
 */
public class CopyUtil {

    /**
     * 拷贝对象值。
     * 条件：target的类型跟src的类型需要一致，
     * 或者target的类型是src的类型的子类型
     *
     * @param src
     * @param target
     */
    public static <S extends Serializable, T extends S> void copy(S src, T target) {
        if (src == null) {
            return;
        }
        Class srcClass = src.getClass();
        List<Field> srcFields = new ArrayList<>();
        getFields(srcClass, srcFields);
        if (srcFields.size() == 0) {
            return;
        }
        for (Field field : srcFields) {
            if ((field.getModifiers() & Modifier.FINAL) != 0) {
                continue;
            }
            final boolean accessible = field.isAccessible();
            try {
                field.setAccessible(true);
                field.set(target, field.get(src));
            } catch (Exception e) {
                DebugLog.e(CopyUtil.class, "copy", e);
            } finally {
                field.setAccessible(accessible);
            }
        }
    }

    /***
     * 拷贝对象值。
     * 忽略src类型和target类型之间的判断。
     * @param src
     * @param target
     */
    public static <S extends Serializable, T extends Serializable> void copyAdvanced (S src, T target) {
        if (src == null || target == null) {
            return;
        }
        Class srcClass = src.getClass();
        Class targetClass = target.getClass();
        List<Field> srcFields = new ArrayList<>();
        getFields(srcClass, srcFields);
        List<Field> targetFields = new ArrayList<>();
        getFields(targetClass, targetFields);
        if (srcFields.size() == 0
                || targetFields.size() == 0) {
            return;
        }
        // 名称相同的Field
        List<Pair<Field, Field>> matchedFields = new LinkedList<>();
        for (Field srcField : srcFields) {
            for (Field targetField : targetFields) {
                if (srcField.getName().equals(targetField.getName())
                        && (srcField.getModifiers() & Modifier.FINAL) == 0
                        && (targetField.getModifiers() & Modifier.FINAL) == 0
                        && srcField.getDeclaringClass().equals(targetField.getDeclaringClass())) {
                    matchedFields.add(Pair.create(srcField, targetField));
                    break;
                }
            }
        }

        for (Pair<Field, Field> pair : matchedFields) {
            final Field srcField = pair.first;
            final Field targetField = pair.second;
            final boolean srcFieldAccessible = srcField.isAccessible();
            final boolean targetFieldAccessible = targetField.isAccessible();
            try {
                srcField.setAccessible(true);
                targetField.setAccessible(true);
                targetField.set(target, srcField.get(src));
            } catch (Exception e) {
                DebugLog.e(CopyUtil.class, "copyAdvanced", srcField.getName() + ", srcField:" + srcField.getType() + ", targetField:" + targetField.getType(), e);
            } finally {
                srcField.setAccessible(srcFieldAccessible);
                targetField.setAccessible(targetFieldAccessible);
            }
        }
    }

    /***
     * @param clazz
     * @param fields
     */
    private static void getFields(Class<? extends Serializable> clazz, List<Field> fields) {
        // search superclasses
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            if (c.equals(Serializable.class)) {
                break;
            }
            Field[] declaredFields = clazz.getDeclaredFields();
            if (declaredFields != null && declaredFields.length != 0) {
                Collections.addAll(fields, declaredFields);
            }
        }
    }

}
