package com.iovebean.bblistdialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity implements BbListDialog.ResultListner, BbListDialog.AddViewListner, BbListDialog.GetSystemDialogListner {

    private static final int TEXTONCLICK = 2;
    private static final int DOTEXT2 = 3;
    private static final int SEEKBAE = 4;
    private static final int INPUT =5 ;
    private static final int ADDVIEW = 6;
    private static final int DOSYSTEMDIALOG = 7;
    private ImageView imageView;
    private static final int COLORMSG =1;
    private LinkedHashMap<Integer, String> colorMap;
    private Dialog mbtdialog;

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
        strings.add("seekbar");
        strings.add("input");
        strings.add("addview");
        strings.add("systemDialog");
        new BbListDialog().showAlertList(this, strings,"目录",
                TEXTONCLICK,this);
    }

    private void doColorAlert() {
        LinkedHashMap<Integer, String> map = ResIdUtils. getResId(R.color.class,getApplication());
        this.colorMap =map;
        BbListDialog bbListDialog = new BbListDialog();
        bbListDialog.showAlertList(this, map, "color", COLORMSG, this);
    }
/*
*
*
* */
    private void doText2() {

        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put(R.drawable.emotion_icon_23_popup,"1");
        linkedHashMap.put(R.drawable.emotion_icon_24_popup,"2");
        new BbListDialog().showAlertList(this, linkedHashMap,"    ",
               DOTEXT2, this );
    }

    private void doAddViewAlert() {

        new BbListDialog().showAddViewAlertDialog(this,"addview",ADDVIEW,this,this);
    }
    private void doInputAlert() {
        new BbListDialog().showBBDialog(this,"input",
                INPUT, this);
    }

    private void doSeekbarAlert() {

        new BbListDialog().showBBDialog(this,"seekbar",
                SEEKBAE, this );
    }

    @Override
    public String getkind(int msg) {
        switch (msg){
            case SEEKBAE:
                return BbListDialog.SEEKBAE;
            case INPUT:
                return BbListDialog.INPUT;
        }

        return null;
    }
    private void doSystemDialog() {
        new BbListDialog().showSystemDialog(this,this,DOSYSTEMDIALOG);
    }

    @Override
    public int[] getids(int msg) {
        switch (msg){

            case SEEKBAE:
                return new int[]{R.layout.myseekbar,R.id.myseekbarid,R.id.myseekbaritx};
            case INPUT:
                return new int[]{R.layout.myinput, R.id.myedinput};
            case TEXTONCLICK:
                return new int[]{R.layout.buttonslist,R.id.buttongrid,
                        R.layout.gridview_item,R.id.buttons,0};
            case DOTEXT2:
                return new int[]{R.layout.buttonslist2,R.id.buttongrid,
                        R.layout.gridview_item2,R.id.text,R.id.img};
            case COLORMSG:
                return new int[]{ R.layout.colorgride, R.id.colorgrid,
                        R.layout.coloritem, R.id.text, R.id.img};
        }

        return new int[0];
    }

    @Override
    public void getResult(String text, String img, int id, int msg) {

        switch (msg){
            case DOSYSTEMDIALOG:
                if(id==1){
                    Toast.makeText(this,"hello",Toast.LENGTH_SHORT).show();
                }
                break;
            case ADDVIEW:
                Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
                //这种情况或许需要在ondistroid中mbtdialog=null;

                break;
            case TEXTONCLICK:
                if(text.equals("child")){
                    doText2();
                }
                if(text.equals("color")){
                    doColorAlert();
                }
                if(text.equals("seekbar")){
                    doSeekbarAlert();
                }
                if(text.equals("input")){
                    doInputAlert();
                }
                if(text.equals("addview")){
                    doAddViewAlert();
                }
                if(text.equals("systemDialog")){
                    doSystemDialog();
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
            case INPUT:
                Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
                break;
        }

    }



    @Override
    public void getDialog(Dialog bbdialog, int msg) {
        if(bbdialog ==null){
            Log.e("null","null");
        }
         this.mbtdialog = bbdialog;
        switch (msg){
            case DOSYSTEMDIALOG:
            case COLORMSG:
                break;
            case TEXTONCLICK:
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                bbdialog.getWindow().setGravity(Gravity.LEFT);
                bbdialog.getWindow().setLayout(dm.widthPixels/3, dm.heightPixels);

                break;
            case DOTEXT2:
                //这个可设置大小
                 dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                bbdialog.getWindow().setGravity(Gravity.LEFT);
                bbdialog.getWindow().setLayout(dm.widthPixels/2, dm.heightPixels);

                break;


        }
    }

    @Override
    public int setStyleId(int msg) {
        switch (msg){
            case ADDVIEW:
            case INPUT:
            case SEEKBAE:

            case COLORMSG:
                break;
            default:
                return R.style.DialogWhenLarge;

        }
        return 0;
    }

    @Override
    protected void onDestroy() {
        mbtdialog=null;
        super.onDestroy();
    }

    @Override
    public View bbAddView(int msg, LayoutInflater inflater) {
        switch (msg){
            case ADDVIEW:
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                final EditText editText = new EditText(this);
                linearLayout.addView(editText);

                Button button = new Button(this);
                button.setText(android.R.string.ok);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getResult(editText.getText().toString(),"",0,ADDVIEW);
                        if(mbtdialog!=null){
                            mbtdialog.dismiss();
                            mbtdialog=null;
                        }
                    }
                });
                linearLayout.addView(button);
                return linearLayout;

        }
        return null;
    }

    @Override
    public Dialog getSystemDialog(int styleId,int msg) {
        switch (msg){
            case DOSYSTEMDIALOG:
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(MainActivity.this);
                normalDialog.setMessage("hello");
                normalDialog.setPositiveButton("enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //自己实现这个方法
                        getResult(null,null,1,DOSYSTEMDIALOG);
                    }
                });
                // 显示
                AlertDialog alertDialog = normalDialog.create();
                return alertDialog;
        }
        return null;
    }
}
