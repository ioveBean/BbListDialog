package com.iovebean.bblistdialog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.iovebean.bblistdialog.BbListDialog.BbListDialog;
import com.iovebean.bblistdialog.BbListDialog.ResIdUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;


import static com.iovebean.bblistdialog.utils.OpenDirectory.initPermission;
import static com.iovebean.bblistdialog.MyResultListner.ADDVIEW;
import static com.iovebean.bblistdialog.MyResultListner.COLORMSG;
import static com.iovebean.bblistdialog.MyResultListner.DOSYSTEMDIALOG;
import static com.iovebean.bblistdialog.MyResultListner.DOTEXT2;
import static com.iovebean.bblistdialog.MyResultListner.INPUT;
import static com.iovebean.bblistdialog.MyResultListner.SEEKBAE;
import static com.iovebean.bblistdialog.MyResultListner.TEXTONCLICK;

public class MainActivity extends AppCompatActivity {



    ImageView imageView;

    LinkedHashMap<Integer, String> colorMap;
    private Dialog mbtdialog;
    private ArrayList<Object> pathFromSD;
    final ThreadLocal<TreeMap<Integer, StringBuilder>> treeMap1 = new ThreadLocal<TreeMap<Integer, StringBuilder>>() {
        @Override
        protected TreeMap<Integer, StringBuilder> initialValue() {
            return new TreeMap<Integer, StringBuilder>(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return Integer.compare(o1, o2);
                }
            });
        }
    };
    public int lin ;
    public int editortime =0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        initPermission(this);
    }

    public void textOnclicK(View view) {
        ArrayList<String> strings = new ArrayList<>();
        if(!BbListDialog.bbHashmap.containsKey(TEXTONCLICK)){
            strings.add("child");
            strings.add("color");
            strings.add("seekbar");
            strings.add("input");
            strings.add("addview");
            strings.add("systemDialog");
            strings.add("Directory");
            strings.add("editer");
            strings.add("text");
            BbListDialog.bbHashmap.put(TEXTONCLICK,strings);
        }

        new BbListDialog().showAlertList(this, BbListDialog.bbHashmap,"目录",
                TEXTONCLICK,MyResultListner.getInstance());
    }

    void doColorAlert() {

        BbListDialog bbListDialog = new BbListDialog();
        Object o = BbListDialog.bbHashmap.get(COLORMSG);
        if(o==null){
            LinkedHashMap<Integer, String> map = ResIdUtils. getResId(R.color.class,getApplication());
            this.colorMap =map;
            BbListDialog.bbHashmap.put(COLORMSG,map);
        }
        bbListDialog.showAlertList(this, BbListDialog.bbHashmap, "color", COLORMSG, MyResultListner.getInstance());
    }
/*
*
*
* */
public void doText2() {


        Object o = BbListDialog.bbHashmap.get(DOTEXT2);
        if(o==null){

            ArrayList<Object[]> objects = new ArrayList<>();
            HashMap<String, Object> map = new HashMap<>();
            objects.add(new Object[]{"2",R.drawable.emotion_icon_23_popup ,null});
            objects.add(new Object[]{"1",R.drawable.emotion_icon_23_popup,null});
            objects.add(new Object[]{"1",R.drawable.emotion_icon_23_popup ,null});
            BbListDialog.bbHashmap.put(DOTEXT2,objects);


        }

        new BbListDialog().showAlertList(this, BbListDialog.bbHashmap,"    ",
               DOTEXT2, MyResultListner.getInstance() );
    }

    void doAddViewAlert() {
        new BbListDialog().showAddViewAlertDialog(this,"addview",ADDVIEW,MyResultListner.getInstance(),MyResultListner.getInstance());
    }
    public void doInputAlert() {
        new BbListDialog().showBBDialog(this,"input",
                INPUT, MyResultListner.getInstance(),BbListDialog.INPUT);
    }

    public void doSeekbarAlert() {

        new BbListDialog().showBBDialog(this,"seekbar",
                SEEKBAE, MyResultListner.getInstance() ,BbListDialog.SEEKBAE);
    }

    void doSystemDialog() {
        new BbListDialog().showSystemDialog(this,MyResultListner.getInstance(),MyResultListner.getInstance(),DOSYSTEMDIALOG,null);
    }


    ArrayList<Object[]> list = new ArrayList<>();



    public String saveString(String text,String s)
    {
        String absolutePath = Environment.getExternalStorageDirectory().toString() + "/accounts";
        File destDir = new File(absolutePath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        try
        {
            absolutePath = Environment.getExternalStorageDirectory().toString() + "/bblistDialog/" + text+".txt";

            FileOutputStream outStream = new FileOutputStream(absolutePath,false);
            OutputStreamWriter writer = new OutputStreamWriter(outStream,"utf-8");
            writer.write(s);

            writer.flush();
            writer.close();//记得关闭

            outStream.close();
        }
        catch (Exception e)
        {
            Log.e("m", e.getMessage());
        }
        return  absolutePath;
    }



    @Override
    protected void onDestroy() {
        mbtdialog=null;
        super.onDestroy();
    }


}
