package com.iovebean.bblistdialog.BbListDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.iovebean.bblistdialog.MainActivity;
import com.iovebean.bblistdialog.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;


public class BbListDialog {
    private static BbListDialog mBbListDialog;
    private SimpleAdapter adapter;

    public static BbListDialog getInstance(){
        if(mBbListDialog==null){
            mBbListDialog = new BbListDialog();
        }
        return mBbListDialog;
    }
    public static final String SEEKBAE ="seekbar" ;
    public static final String INPUT = "input";
    private static final String ADDVIEW ="addView" ;

    public static final String TEXTSHOW ="textShow" ;


    //返回系统dialog不需要show()并实现result
    public interface GetSystemDialogListner{

       <T> Dialog getSystemDialog(T context,int styleId,int msg,Object data);
    }

    public interface AddViewListner{
        /**
         *
         * @param msg 唯一标识
         * @param inflater 可以不使用
         * @return 需要添加到dialog里的view ,
         */
        <T>View bbAddView(T t,int msg, LayoutInflater inflater);
    }

    /**
     *
     * @param listner 返回 实现的dialog 不需要show() 并实现result
     * @param resultListner 需实现 style,result,dialog
     * @param msg 唯一标识
     */
    public void showSystemDialog(Context context,GetSystemDialogListner listner,ResultListner resultListner,int msg,Object data) {
        if(btdialog!=null){
            btdialog.cancel();

        }
        int StyleId = resultListner.getDialoSetStyleId((Activity) context,null,msg);
        Dialog systemDialog = listner.getSystemDialog(context,StyleId,msg,data);
        systemDialog.show();
        systemDialog.setCanceledOnTouchOutside(true);
        resultListner.getDialoSetStyleId((Activity) context,systemDialog,msg);
    }

    /**
     *
     * @param activity activity
     * @param title 名称
     * @param msg 唯一标识
     * @param resultListner 需要实现 listner 里的 kind ,ids,style,result,dialog
     */
    public void showBBDialog(Activity activity, String title, final int msg,
                             final ResultListner resultListner,String kind) {
        if(btdialog!=null){
            btdialog.cancel();

        }
        showSeekbar(kind,activity,title,resultListner ,msg);
        }

    private <T>void showSeekbar(String s, T activity, String title, final ResultListner resultListner,
                             final int msg) {
        if(null == s){
            Log.e("error","kind is null");
        }
        LayoutInflater inflater = LayoutInflater.from((Context) activity);
        int inflaterid = 0;
        int[] getids = resultListner.getids(msg);

        if(s==SEEKBAE){
            getids =  new int[]{R.layout.myseekbar,R.id.myseekbarid,R.id.myseekbaritx,R.id.action_cancel};

        }
        if(s == INPUT){
            getids  =   new int[]{R.layout.myinput, R.id.myedinput,R.id.action_ok,R.id.action_cancel};

        }
        if(s == TEXTSHOW){
            getids = new int[]{R.layout.dialog_action,R.id.dialog_text,R.id.action_ok,R.id.action_cancel};
        }
        inflaterid = getInflaterid(s, inflaterid,getids[0]);

        View btDialogView = inflater.inflate(inflaterid, null);
        ViewGroup parentViewGroup = (ViewGroup) btDialogView.getParent();
        if (parentViewGroup != null) {
            parentViewGroup.removeAllViews();
        }
        initDialog(s, resultListner, msg, btDialogView,activity,title,getids, null);


    }

    private<T> void initDialog(String s, final ResultListner resultListner, final int msg, View btDialogView,
                            final T activity, String title, int[] getids, View view) {
        int styleid =  resultListner.getDialoSetStyleId((Activity) activity,null,msg);
        BottomSheetDialog btdialogbuilder;
        if(styleid==0){
            btdialogbuilder = new BottomSheetDialog((Context) activity);
        }else {
            btdialogbuilder = new BottomSheetDialog((Context) activity,styleid);
        }
        switch (s){
            case TEXTSHOW:

                TextView textView = btDialogView.findViewById(getids[1]);

                textView.setText(title);
                btDialogView.findViewById(R.id.action_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogcancle();
                        resultListner.getResult(activity,null,null,1,msg,null);
                    }
                });
                btDialogView.findViewById(R.id.action_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogcancle();
                        resultListner.getResult(activity,null,null,0,msg,null);
                    }
                });
              //  btdialogbuilder.setTitle(title);
                btdialogbuilder.setContentView(btDialogView);

                break;
            case ADDVIEW:
                //btdialogbuilder.setTitle(title);
                btdialogbuilder.setContentView(view);
                btDialogView = view;
                break;
            case SEEKBAE:
                TextView  titleview = btDialogView.findViewById(R.id.myseekbaritx);
                titleview.setText(title);
                SeekBar seekBar = btDialogView.findViewById(getids[1]);

                final TextView showpro = btDialogView.findViewById(getids[2]);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        resultListner.getResult(activity,"","",progress,msg,null);
                        if(null != showpro){
                            showpro.setText(progress+"");
                        }

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
                btDialogView.findViewById(getids[3]).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      dialogcancle();
                    }
                });

                btdialogbuilder.setContentView(btDialogView);



                break;
            case INPUT:
                titleview = btDialogView.findViewById(R.id.inputtext);
                titleview.setText(title);
                final EditText editText = btDialogView.findViewById(getids[1]);

                btdialogbuilder.setContentView(btDialogView);
                btdialogbuilder.findViewById(R.id.action_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resultListner.getResult(activity,editText.getText().toString(),"",0,msg,null);
                       dialogcancle();
                    }
                });
                btdialogbuilder.findViewById(R.id.action_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogcancle();
                    }
                });

                break;
        }
        btdialog = btdialogbuilder;
        btdialog.create();
        btdialog.setCanceledOnTouchOutside(true);
        btdialog.show();

        if(s==ADDVIEW){
            btdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    btdialog.dismiss();
                }
            });
        }

        resultListner.getDialoSetStyleId((Activity) activity,btdialog,msg);
      //  setBottomSheetBehavior(btdialog, btDialogView, BottomSheetBehavior.STATE_EXPANDED);
        hideKeyboard((Activity) activity);
    }

    private void dialogcancle() {
        if (btdialog != null) {
            btdialog.cancel();
        }
    }

    private int getInflaterid(String s, int inflaterid, int getid) {
        switch (s){
            case SEEKBAE:
            case TEXTSHOW:
            case INPUT:
                inflaterid = getid;
                break;
        }
        return inflaterid;
    }

    /**
     *
     * @param activity activity
     * @param title 名称
     * @param msg 唯一标识
     * @param resultListner 需实现 style,result,dialog
     * @param addViewListner 在这里实现resultListner 里的result 并返回view
     */
    public void showAddViewAlertDialog(Activity activity, String title,  int msg, final ResultListner resultListner,AddViewListner addViewListner) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = addViewListner.bbAddView(activity,msg,inflater);
        initDialog(ADDVIEW, resultListner, msg, null,activity,title,null,view);

    }

    private  Dialog btdialog;
    private  ArrayList<Map<String, Object>> dataList;

    public static BbDialogHashMap bbHashmap = new BbDialogHashMap();
    /**
     *
     * @param activity
     * @param  map 把list 存在map中 内容 ,arraylist 如果是LinkedHashMap则key是图片id,value是文字,
     * @param title 标题
     * @param msg 唯一标识
     * @param resultListner 监听  需实现 ids,style,result,dialog
     * @param <T> 可以是arraylist LinkedHashMap 等
     */
    public <T> void showAlertList(final T activity, BbDialogHashMap map, String title, final int msg,
                                  final ResultListner resultListner ){
        Object list = map.get(msg);


        showclicklist(activity,resultListner,list,title, msg,new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String text = dataList.get(arg2).get("0").toString();

                Object o = dataList.get(arg2).get("1");
                String img ="-1";
                if(o!=null){
                    img = o.toString();
                }

                //img返回key,arg2返回位置
                Log.e("text","text"+text);
                resultListner.getResult(activity,text,img,arg2,msg,arg1);
                btdialog.dismiss();
                btdialog = null;
            }
        });
    }


    public static void setBottomSheetBehavior (final Dialog dialog, final View view, int beh) {

        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setState(beh);

        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                try {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.cancel();
                        }
                    }, 250);
                } catch (Exception e) {
                    Log.w("Browser", "Error cancel dialog");
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private <T ,E> void showclicklist(E activity,ResultListner resultListner,T bblist,String title,int msg,AdapterView.OnItemClickListener listener ) {
        int[] getids = resultListner.getids(msg);
        int layoutid = getids[0];
        int gridId = getids[1];
        int layoutitem = getids[2];
        int textid = getids[3];
        int imageid = getids[4];
        LayoutInflater inflater = LayoutInflater.from((Context) activity);

        View btDialogView = inflater.inflate(layoutid, null);
        ViewGroup parentViewGroup = (ViewGroup) btDialogView.getParent();
        if (parentViewGroup != null) {
            parentViewGroup.removeAllViews();
        }


        GridView gridView = btDialogView.findViewById(gridId);
        //图标下的文字

        Object text;
        dataList = new ArrayList<>();

        if(bblist instanceof ArrayList){

            ArrayList list = (ArrayList) bblist;
            if(list.get(0) instanceof Map ){
                dataList = list;
            }else {
                for(int i=0;i<list.size();i++){
                    Map<String, Object> map= new HashMap<>();

                    Object o = list.get(i);
                    if(o.getClass().isArray()){
                        Object[] o1 = (Object[])o;
                        for (int j =0;j<o1.length;j++){
                            map.put(j+"",o1[j]);
                        }
                    }else {
                        map.put("0",o);

                    }

                    dataList.add(map);
                }
            }


        }else if(bblist instanceof HashSet){
            HashSet list = (HashSet) bblist;
            for (Object next : list) {
                Map<String, Object> map = new HashMap<>();
                if(next.getClass().isArray()){
                    Object[] o1 = (Object[])next;
                    for (int j =0;j<o1.length;j++){
                        map.put(j+"",o1[j]);
                    }
                }else {
                    map.put("0",next);
                }
                dataList.add(map);
            }

        }else if(bblist instanceof LinkedHashMap){
            LinkedHashMap map1 = (LinkedHashMap) bblist;
            for (Object o : map1.entrySet()) {
                Map.Entry entry = (Map.Entry) o;

                Map<String, Object> map = new HashMap<>();



                map.put("1", entry.getKey());

                o = entry.getValue();

                    map.put("0",o );


                dataList.add(map);
            }

        }else if(bblist instanceof  HashMap){
            HashMap map1 = (HashMap) bblist;
            for (Object o : map1.entrySet()) {
                Map.Entry entry = (Map.Entry) o;

                Map<String, Object> map = new HashMap<>();



                map.put("1", entry.getKey());

                o = entry.getValue();

                    map.put("0",o );


                dataList.add(map);
            }

        }else if(bblist instanceof  String[]){
            String[] array = (String[]) bblist;

            for (String s : array) {

                Map<String, Object> map = new HashMap<>();

                map.put("0", s);
                dataList.add(map);
            }

        }else {

            Log.e("myUtils","error");
            return;
        }

        int i = getids.length -3;
        String[] from;

        int[] to;

        from = new String[i];
        to = new int[i];

        for(int x = 0;x<from.length;x++){
            from[x] = x+"";
            to[x] = getids[x+3];
        }


        // to= new int[]{textid};



         adapter = new SimpleAdapter((Context) activity, dataList,layoutitem, from, to);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(listener);
        int id =  resultListner.getDialoSetStyleId((Activity) activity,null,msg);

        if(id==0){
            //  btdialog = new BottomSheetDialog((Context) activity);
            //btdialog.setTitle(title);
            //btdialog.setContentView(btDialogView);
            //btdialog.create();
            btdialog = new android.app.AlertDialog.Builder((Context) activity).setTitle(title).setView(btDialogView).create();

        }else {
            //  btdialog = new BottomSheetDialog((Context) activity,id);
            // btdialog.setTitle(title);
            //btdialog.setContentView(btDialogView);
            //btdialog.create();
            btdialog = new android.app.AlertDialog.Builder((Context) activity,id).setTitle(title).setView(btDialogView).create();

        }

        btdialog.setCanceledOnTouchOutside(true);
        btdialog.show();
        hideKeyboard((Activity) activity);
        resultListner.getDialoSetStyleId((Activity) activity,btdialog,msg);
        //  setBottomSheetBehavior(btdialog, btDialogView, BottomSheetBehavior.STATE_EXPANDED);

    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
