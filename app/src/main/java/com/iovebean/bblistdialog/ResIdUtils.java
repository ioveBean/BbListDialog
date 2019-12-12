package com.iovebean.bblistdialog;

import android.content.Context;

import androidx.core.content.ContextCompat;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public class ResIdUtils {
    private static LinkedHashMap<Integer, String>  maps;

    public static LinkedHashMap<Integer, String> getResId(Class<?> c, Context context) {
        if(maps != null){
            return  maps;
        }
        try {
            Field[] declaredFields = c.getDeclaredFields();
            maps = new LinkedHashMap<>();
            for (Field declaredField : declaredFields) {
                String name = declaredField.getName();
                if (name.contains("cc")) {
                    int anInt = declaredField.getInt(declaredField);
                    //color_name
                    //name = name.replaceAll("cc([\\d]*)","");
                    // maps.put(anInt,name);
                    int color1 = ContextCompat.getColor(context, anInt);
                    String format = String.format("#%06x", color1 & 0x00FFFFFF);
                    maps.put(anInt, format);
                }
            }
            return maps;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
