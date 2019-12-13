package com.iovebean.bblistdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MyUtils {

    public interface ResultListner{

        void getResult(String text, String img,int id,int msg);
        void getDialog(AlertDialog btdialog ,int msg);
        int setStyleId(int msg);
    }
    private  android.app.AlertDialog btdialog;
    private  ArrayList<Map<String, Object>> dataList;

    /**
     *
     * @param activity
     * @param list
     * @param title
     * @param layoutid
     * @param gridid
     * @param layoutitem
     * @param textid
     * @param imageid
     * @param resultListner
     * @param <T>
     */
    public <T> void showAlertList(Activity activity, T list, String title,
                                  int layoutid, int gridid,
                                  int layoutitem, int textid, int imageid, final int msg,
                                  final ResultListner resultListner ){

        showclicklist(activity,resultListner,list,title,layoutid,gridid,layoutitem,textid,imageid, msg,new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String text = dataList.get(arg2).get("text").toString();
                String img = dataList.get(arg2).get("img").toString();
                //img返回key,arg2返回位置
                resultListner.getResult(text,img,arg2,msg);
                btdialog.dismiss();
            }
        });
    }
    private <T> void showclicklist(Activity activity,ResultListner resultListner,T name,String title,int layoutid,int gridId ,
                                   int layoutitem,int textid,int imageid,int msg,AdapterView.OnItemClickListener listener ) {

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
