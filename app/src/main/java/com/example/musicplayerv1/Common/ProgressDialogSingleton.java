package com.example.musicplayerv1.Common;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogSingleton {
    private static ProgressDialog progressDialog;
    private static ProgressDialogSingleton progressDialogSingleton;
    private ProgressDialogSingleton(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Loading. Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
    }
    public static ProgressDialogSingleton getInstance(Context context){
        if(progressDialogSingleton!= null){
            return progressDialogSingleton;
        }
        else{
            progressDialogSingleton =  new ProgressDialogSingleton(context);
          return progressDialogSingleton;

        }
    }
    public  void show(){
        progressDialog.show();
    }
    public  void dismiss(){
        progressDialog.dismiss();
    }
}
