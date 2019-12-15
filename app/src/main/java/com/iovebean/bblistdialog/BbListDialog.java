package com.iovebean.bblistdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class BbListDialog {

    public static final String SEEKBAE ="seekbar" ;
    public static final String INPUT = "input";
    private static final String ADDVIEW ="addView" ;


    public interface GetSystemDialogListner{
        Dialog getSystemDialog(int styleId,int msg);
    }
    public interface AddViewListner{
        /**
         *
         * @param msg 唯一标识
         * @param inflater 可以不使用
         * @return 需要添加到dialog里的view ,
         */
        View bbAddView(int msg, LayoutInflater inflater);
    }
    public interface ResultListner{
        /**
         *
         * @param msg 唯一标识
         * @return public static final String SEEKBAE ="seekbar" ;
         *           public static final String INPUT = "input";
         */
        String getkind(int msg);

        /**
         * //seekbar 可以有3个id:layout seekbarid,textid 或layout seekbarid;
         *  //input 需要有2个id :layout inputid;
         *  //list 需要有5个id :gridview所在layout,gridviewid,
         *    //                itemlayout 的id,itemlayout 的textviewid,item的imgid,
         * @param msg 唯一标识
         * @return 例子 R.layout.colorgride, R.id.colorgrid,
         *                         R.layout.coloritem, R.id.text, R.id.img
         *                         最后一个id 可以为-1;
         */
        int[] getids(int msg);

        /**
         * 结果监听
         * @param text 返回list的内容 如何是input 返回 input内容
         * @param img 如果有图片,返回图片id  ,是LinkedHashMap的key
         * @param pro 返回位置 如果是seekbar 返回进度
         * @param msg 唯一标识
         */
        void getResult(String text, String img,int pro,int msg);

        /**
         * 返回dialog
         * @param bbdialog 返回dialog
         * @param msg 唯一标识
         */
        void getDialog(Dialog bbdialog ,int msg);
        int setStyleId(int msg);
    }
    public void showSystemDialog(GetSystemDialogListner listner,ResultListner resultListner,int msg) {
        int StyleId = resultListner.setStyleId(msg);
        Dialog systemDialog = listner.getSystemDialog(StyleId,msg);
        systemDialog.show();
        systemDialog.setCanceledOnTouchOutside(true);
        resultListner.getDialog(systemDialog,msg);
    }
    public void showBBDialog(Activity activity, String title, final int msg,
                             final ResultListner resultListner) {

        showSeekbar(resultListner.getkind(msg),activity,title,resultListner ,msg);
        }

    private void showSeekbar(String s, Activity activity, String title, final ResultListner resultListner,
                             final int msg) {
        if(null == s){
            Log.e("error","kind is null");
        }
        LayoutInflater inflater = LayoutInflater.from(activity);
        int inflaterid = 0;
        int[] getids = resultListner.getids(msg);
        inflaterid = getInflaterid(s, inflaterid,getids[0]);

        View btDialogView = inflater.inflate(inflaterid, null);
        ViewGroup parentViewGroup = (ViewGroup) btDialogView.getParent();
        if (parentViewGroup != null) {
            parentViewGroup.removeAllViews();
        }
        initDialog(s, resultListner, msg, btDialogView,activity,title,getids, null);


    }

    private void initDialog(String s, final ResultListner resultListner, final int msg, View btDialogView,
                            Activity activity, String title, int[] getids, View view) {
        int styleid =  resultListner.setStyleId(msg);
        AlertDialog.Builder btdialogbuilder;
        if(styleid==0){
            btdialogbuilder = new android.app.AlertDialog.Builder(activity);
        }else {
            btdialogbuilder = new android.app.AlertDialog.Builder(activity,styleid);
        }
        switch (s){
            case ADDVIEW:
                btdialogbuilder.setView(view).setTitle(title);
                break;
            case SEEKBAE:
                SeekBar seekBar = btDialogView.findViewById(getids[1]);
                int textid =-1;
                if(getids.length==3){
                    textid =getids[2];
                }
                final TextView showpro = btDialogView.findViewById(textid);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        resultListner.getResult("","",progress,msg);
                        if(null != showpro){
                            showpro.setText(progress+"");
                        }
                        btdialog = null;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });


                btdialogbuilder.setTitle(title).setView(btDialogView).setPositiveButton(android.R.string.cancel,null).create();

                break;
            case INPUT:
                final EditText editText = btDialogView.findViewById(getids[1]);

                btdialogbuilder.setTitle(title).setView(btDialogView).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resultListner.getResult(editText.getText().toString(),"",0,msg);
                        btdialog = null;
                    }
                });

                break;
        }
        btdialog = btdialogbuilder.create();
        btdialog.setCanceledOnTouchOutside(true);
        btdialog.show();
        resultListner.getDialog(btdialog,msg);

    }

    private int getInflaterid(String s, int inflaterid, int getid) {
        switch (s){
            case SEEKBAE:
                inflaterid=getid;
                break;
            case INPUT:
                inflaterid = getid;
                break;
        }
        return inflaterid;
    }

    public void showAddViewAlertDialog(Activity activity, String title,  int msg, final ResultListner resultListner,AddViewListner addViewListner) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = addViewListner.bbAddView(msg,inflater);
        initDialog(ADDVIEW, resultListner, msg, null,activity,title,null,view);

    }

    private  android.app.AlertDialog btdialog;
    private  ArrayList<Map<String, Object>> dataList;

    /**
     *
     * @param activity
     * @param list 内容 ,arraylist 如果是LinkedHashMap则key是图片id,value是文字,
     * @param title 标题
     * @param msg 唯一标识
     * @param resultListner 监听
     * @param <T> 可以是arraylist LinkedHashMap 等
     */
    public <T> void showAlertList(Activity activity, T list, String title, final int msg,
                                  final ResultListner resultListner ){

        showclicklist(activity,resultListner,list,title, msg,new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String text = dataList.get(arg2).get("text").toString();
                String img = dataList.get(arg2).get("img").toString();
                //img返回key,arg2返回位置
                resultListner.getResult(text,img,arg2,msg);
                btdialog.dismiss();
                btdialog = null;
            }
        });
    }
    private <T> void showclicklist(Activity activity,ResultListner resultListner,T name,String title,int msg,AdapterView.OnItemClickListener listener ) {
        int[] getids = resultListner.getids(msg);
        int layoutid = getids[0];
        int gridId = getids[1];
        int layoutitem = getids[2];
        int textid = getids[3];
        int imageid = getids[4];
        LayoutInflater inflater = LayoutInflater.from(activity);

            View btDialogView = inflater.inflate(layoutid, null);
            ViewGroup parentViewGroup = (ViewGroup) btDialogView.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }


            GridView gridView = btDialogView.findViewById(gridId);
            //图标下的文字
        String[] from;

        int[] to;

                 Object text;
                dataList = new ArrayList<>();
                if(name instanceof ArrayList){
                    ArrayList list = (ArrayList) name;
                    for(int i=0;i<list.size();i++){
                        Map<String, Object> map= new HashMap<>();
                        map.put("img", "");
                        Object o = list.get(i);
                        if(o.getClass().isArray()){
                            Object[] o1 = (Object[])o;
                            text =o1[0];
                        }else {
                            text = o;
                        }
                        map.put("text",text);
                        dataList.add(map);
                    }
                   from= new String[]{"text"};
                    to= new int[]{textid};

                }else if(name instanceof HashSet){
                    HashSet list = (HashSet) name;
                    for (Object next : list) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("img", "");

                        if(next.getClass().isArray()){
                            Object[] o1 = (Object[])next;
                            text =o1[0];
                        }else {
                            text = next;
                        }
                        map.put("text", text);
                        dataList.add(map);
                    }
                    from= new String[]{"text"};
                    to= new int[]{textid};
                }else if(name instanceof LinkedHashMap){
                    LinkedHashMap map1 = (LinkedHashMap) name;
                    for (Object o : map1.entrySet()) {
                        Map.Entry entry = (Map.Entry) o;

                        Map<String, Object> map = new HashMap<>();



                        map.put("img", entry.getKey());

                         o = entry.getValue();
                        if(o.getClass().isArray()){
                            Object[] o1 = (Object[])o;
                            text =o1[0];
                        }else {
                            text = o;
                        }
                        map.put("text",text );
                        dataList.add(map);
                    }
                    from= new String[]{"img", "text"};

                    to= new int[]{imageid, textid};
                }else if(name instanceof  HashMap){
                    HashMap map1 = (HashMap) name;
                    for (Object o : map1.entrySet()) {
                        Map.Entry entry = (Map.Entry) o;

                        Map<String, Object> map = new HashMap<>();

                        map.put("img", entry.getKey());

                        o = entry.getValue();
                        if(o.getClass().isArray()){
                            Object[] o1 = (Object[])o;
                            text =o1[0];
                        }else {
                            text = o;
                        }
                        map.put("text", text);
                        dataList.add(map);
                    }
                    from= new String[]{"img", "text"};
                    to= new int[]{imageid, textid};
                }else if(name instanceof  String[]){
                    String[] array = (String[]) name;

                    for (String s : array) {

                        Map<String, Object> map = new HashMap<>();
                        map.put("img", "");
                        map.put("text", s);
                        dataList.add(map);
                    }
                    from= new String[]{"text"};
                    to= new int[]{textid};
                }else {

                    Log.e("myUtils","error");
                    return;
                }





            SimpleAdapter adapter = new SimpleAdapter(activity, dataList,layoutitem, from, to);

            gridView.setAdapter(adapter);

            gridView.setOnItemClickListener(listener);
           int id =  resultListner.setStyleId(msg);

           if(id==0){
               btdialog = new android.app.AlertDialog.Builder(activity).setTitle(title).setView(btDialogView).create();

           }else {
               btdialog = new android.app.AlertDialog.Builder(activity,id).setTitle(title).setView(btDialogView).create();

           }
        btdialog.setCanceledOnTouchOutside(true);
            btdialog.show();
            resultListner.getDialog(btdialog,msg);

    }


}
