package com.iovebean.bblistdialog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void textOnclicK(View view) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("第一张");
        strings.add("第二张");
        new MyUtils().showAlertList(this, strings,"目录",R.layout.buttonslist,R.id.buttongrid,
                R.layout.gridview_item,R.id.buttons,0,
                new MyUtils.ResultListner() {
            @Override
            public void getResult(String text, String img) {

                if(text.equals("第一张")){
                    Log.e("tag","第一张");
                    doText2();
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
