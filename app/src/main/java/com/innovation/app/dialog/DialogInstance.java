package com.innovation.app.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.innovation.app.R;
import com.innovation.app.net.request.SpRequestQueue;

import java.util.Iterator;
import java.util.LinkedList;

public class DialogInstance {
    private Dialog showProgressDialog;
    private LinkedList<String> requestTags = new LinkedList<>();
    private static DialogInstance dialogInstance = null;

    public DialogInstance() {
    }

    public synchronized static DialogInstance getInstance() {
        if (dialogInstance == null) {
            dialogInstance = new DialogInstance();
        }
        return dialogInstance;
    }
    /**
     * 显示进度条
     *
     * @param context
     * @param text
     * @param tag request标签
     * @return
     */
    public Dialog showProgressDialog(Context context, String text) {
        if (showProgressDialog != null && showProgressDialog.isShowing()) {
            try {
                showProgressDialog.cancel();
                showProgressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        showProgressDialog = new Dialog(context, R.style.blank_dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.progress_layout_xml, null);
        TextView textView = (TextView) view.findViewById(R.id.progress_text);
        textView.setText(text);

        showProgressDialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        showProgressDialog.setCanceledOnTouchOutside(false);
        showProgressDialog.getWindow().getAttributes();
        if (!((Activity) context).isFinishing()) {
            showProgressDialog.show();
        }
        return showProgressDialog;
    }
    /**
     * 显示进度条
     *
     * @param context
     * @param text
     * @param tag request标签
     * @return
     */
    public Dialog showProgressDialog(Context context, String text, final String tag) {
        if (showProgressDialog != null && showProgressDialog.isShowing()) {
            try {
                showProgressDialog.cancel();
                showProgressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        showProgressDialog = new Dialog(context, R.style.blank_dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.progress_layout_xml, null);
        TextView textView = (TextView) view.findViewById(R.id.progress_text);
        textView.setText(text);

        showProgressDialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        showProgressDialog.setCanceledOnTouchOutside(false);
        showProgressDialog.getWindow().getAttributes();
        requestTags.add(tag);
        showProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Iterator<String> iterator = requestTags.iterator();
                while (iterator.hasNext()) {
                    String tag = iterator.next();
                    SpRequestQueue.get().cancelRequest(tag);
                    iterator.remove();
                }
            }
        });
        if (!((Activity) context).isFinishing()) {
            showProgressDialog.show();
        }
        return showProgressDialog;
    }
    /**
     * 显示进度条
     *
     * @param context
     * @param text
     * @return
     */
    public Dialog showProgressDialogContinue(Context context, String text) {
        if (showProgressDialog != null && showProgressDialog.isShowing()) {
            TextView textView = (TextView) showProgressDialog.findViewById(R.id.progress_text);
            textView.setText(text);
        }
        return showProgressDialog;
    }

    /**
     * 显示进度条
     *
     * @param context
     * @param text
     * @param anchor  在该View之下显示
     * @return
     */
    public Dialog showProgressDialog(Context context, String text, View anchor) {
        showProgressDialog = new Dialog(context, R.style.blank_dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.progress_layout_xml, null);
        TextView textView = (TextView) view.findViewById(R.id.progress_text);
        textView.setText(text);
        showProgressDialog.setContentView(view);
        showProgressDialog.setCanceledOnTouchOutside(false);

        Window dialogWindow = showProgressDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);

        int[] locations = new int[2];
        anchor.getLocationOnScreen(locations);
        int x = locations[0];
        int y = locations[1];
        //显示的坐标
        lp.x = x;
        lp.y = y;
        dialogWindow.setAttributes(lp);

        showProgressDialog.show();
        return showProgressDialog;
    }

    /**
     * 取消对话框progressdialog
     */
    public void cancleProgressDialog() {
        try {
            if (showProgressDialog != null) {
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.cancel();
                    showProgressDialog.dismiss();
                }
                showProgressDialog = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            showProgressDialog = null;
        }

    }
}
