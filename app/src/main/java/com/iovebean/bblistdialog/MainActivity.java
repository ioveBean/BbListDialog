package com.iovebean.bblistdialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MyUtils.ResultListner {

    private static final int TEXTONCLICK = 2;
    private static final int DOTEXT2 = 3;
    private ImageView imageView;
    private static final int COLORMSG =1;
    private LinkedHashMap<Integer, String> colorMap;

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
                TEXTONCLICK,this);
    }

    private void doColorAlert() {
        LinkedHashMap<Integer, String> map = ResIdUtils. getResId(R.color.class,getApplication());
        this.colorMap =map;
        MyUtils myUtils = new MyUtils();

        myUtils.showAlertList(this, map, "color", R.layout.colorgride, R.id.colorgrid,
                R.layout.coloritem, R.id.text, R.id.img, COLORMSG, this);

    }
/*
*
*
* */
    private void doText2() {

        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put(R.drawable.emotion_icon_23_popup,"1");
        linkedHashMap.put(R.drawable.emotion_icon_24_popup,"2");
        new MyUtils().showAlertList(this, linkedHashMap,"    ",R.layout.buttonslist2,R.id.buttongrid,
                R.layout.gridview_item2,R.id.text,R.id.img,
               DOTEXT2, this );
    }

    @Override
    public void getResult(String text, String img, int id, int msg) {
        switch (msg){

            case TEXTONCLICK:
                if(text.equals("child")){
                    doText2();
                }
                if(text.equals("color")){
                    doColorAlert();
                }
                break;
            case DOTEXT2:
                if(text.equals("1")){
                    Log.e("tag","1");
                }
                break;
            case COLORMSG:

                imageView.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,Integer.parseInt(img)));

                String s = colorMap.get(Integer.parseInt(img));
                Log.e("name",s);
                break;
        }
    }

    @Override
    public void getDialog(AlertDialog btdialog, int msg) {
        switch (msg){
            case COLORMSG:
                break;
            case TEXTONCLICK:
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                btdialog.getWindow().setGravity(Gravity.LEFT);
                btdialog.getWindow().setLayout(dm.widthPixels/3, dm.heightPixels);

                break;
            case DOTEXT2:
                //这个可设置大小
                 dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                btdialog.getWindow().setGravity(Gravity.LEFT);
                btdialog.getWindow().setLayout(dm.widthPixels/2, dm.heightPixels);
                break;
        }
    }

    @Override
    public int setStyleId(int msg) {
        switch (msg){
            case COLORMSG:
                break;
            default:
                return R.style.DialogWhenLarge;

        }
        return 0;
    }
}
