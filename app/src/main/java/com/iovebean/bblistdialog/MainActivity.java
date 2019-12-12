package com.iovebean.bblistdialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
    }

    public void textOnclicK(View view) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("child");
        strings.add("color");
        new MyUtils().showAlertList(this, strings,"目录",R.layout.buttonslist,R.id.buttongrid,
                R.layout.gridview_item,R.id.buttons,0,
                new MyUtils.ResultListner() {
            @Override
            public void getResult(String text, String img) {

                if(text.equals("child")){

                    doText2();
                }
                if(text.equals("color")){
                    doColorAlert();
                }
            }
                 /*   window.setGravity(Gravity.CENTER); 中间位置
                    window.setGravity(Gravity.BOTTEM); 底部位置
                    window.setGravity(Gravity.TOP); 顶部位置 */

                    @Override
                    public void getDialog(AlertDialog btdialog) {
                        DisplayMetrics dm = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(dm);

                        btdialog.getWindow().setGravity(Gravity.LEFT);
                        btdialog.getWindow().setLayout(dm.widthPixels/3, dm.heightPixels);

                    }

                    @Override
                    public int setStyleId() {
                        return R.style.DialogWhenLarge;
                    }
                });
    }
    public LinkedHashMap<Integer, String> getResId(Class<?> c) {
        try {
            Field[] declaredFields = c.getDeclaredFields();
            LinkedHashMap<Integer, String> maps = new LinkedHashMap<>();
            for(int i=0;i<declaredFields.length;i++){
                String name = declaredFields[i].getName();
                if(name.contains("cc")){
                    int anInt = declaredFields[i].getInt(declaredFields[i]);
                    //color_name
                    //name = name.replaceAll("cc([\\d]*)","");
                    // maps.put(anInt,name);
                    int color1 =ContextCompat.getColor(MainActivity.this.getApplication(),anInt);
                    String format = String.format("#%06x", color1 & 0x00FFFFFF);
                    maps.put(anInt,format);
                }
            }
            return maps;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void doColorAlert() {
        LinkedHashMap<Integer, String> map = getResId(R.color.class);
        new MyUtils().showAlertList(this, map, "color", R.layout.colorgride, R.id.colorgrid,
                R.layout.coloritem, R.id.text, R.id.img, new MyUtils.ResultListner() {
                    @Override
                    public void getResult(String text, String img) {
                        imageView.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,Integer.parseInt(img)));
                    }
                    @Override
                    public void getDialog(AlertDialog btdialog) {

                    }
                    @Override
                    public int setStyleId() {
                        return 0;
                    }
                });

    }

    private void doText2() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put(R.drawable.emotion_icon_23_popup,"1");
        linkedHashMap.put(R.drawable.emotion_icon_24_popup,"2");
        new MyUtils().showAlertList(this, linkedHashMap,"    ",R.layout.buttonslist2,R.id.buttongrid,
                R.layout.gridview_item2,R.id.text,R.id.img,
                new MyUtils.ResultListner() {
            @Override
            public void getResult(String text, String img) {

                if(text.equals("1")){
                    Log.e("tag","1");

                }
            }

                @Override public void getDialog(AlertDialog btdialog) {

                    //这个可设置大小

                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);

                    btdialog.getWindow().setGravity(Gravity.LEFT);
                    btdialog.getWindow().setLayout(dm.widthPixels/2, dm.heightPixels);


                }

                    @Override
                    public int setStyleId() {
                        return R.style.DialogWhenLarge;
                    }
                });
    }
}
