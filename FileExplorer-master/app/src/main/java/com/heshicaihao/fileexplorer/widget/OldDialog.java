package com.heshicaihao.fileexplorer.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.heshicaihao.fileexplorer.R;


public class OldDialog {
	private View view;
	private Context context;
	private Dialog dialog;
	private Display display;
	private LinearLayout lLayout_bg;
	// dialog的title
	private TextView tv_dialog_title;
	// dialog的内容
	private TextView tv_dialog_content;
	// 确定按钮
	private TextView btn_ok;
	// 取消按钮
	private TextView btn_no;

	public OldDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public OldDialog builder() {
		// 获取Dialog布局
		view = LayoutInflater.from(context).inflate(R.layout.view_dialog_old, null);
		lLayout_bg = (LinearLayout) view.findViewById(R.id.dialog_view);
		init();
		// 定义Dialog布局和参数
		dialog = new Dialog(context, R.style.AlertDialogStyle);
		dialog.setContentView(view);

		// 调整dialog背景大小
		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));
		return this;
	}

	private void init() {
		// dialog的title
		tv_dialog_title = (TextView) view.findViewById(R.id.tv_dia_pro_title);
		// dialog的内容
		tv_dialog_content = (TextView) view
				.findViewById(R.id.tv_dia_pro_content);
		// 为确定按钮初始化并添加监听事件
		btn_ok = (TextView) view.findViewById(R.id.ib_dia_pro_confirm);
		// btn_ok.setOnClickListener(this);
		// 为取消按钮初始化并添加监听事件
		btn_no = (TextView) view.findViewById(R.id.ib_dia_pro_cancel);
		// btn_no.setOnClickListener(this);
	}

	public OldDialog setTitle(String title) {
		// showTitle = true;
		if ("".equals(title)) {
			tv_dialog_title.setText("提示");
		} else {
			tv_dialog_title.setText(title);
		}
		return this;
	}

	public OldDialog hideTitle() {

		tv_dialog_title.setVisibility(View.INVISIBLE);

		return this;
	}

	/**
	 * 点击屏幕外面dialog是否消失
	 *
	 * @param iscancel
	 * @return
	 */
	public OldDialog setCanceledOnTouchOutside(Boolean iscancel) {
		dialog.setCanceledOnTouchOutside(iscancel);
		return this;
	}

	public OldDialog setMsg(String msg) {
		// showTitle = true;
		if ("".equals(msg)) {
			tv_dialog_content.setText("内容");
		} else {
			tv_dialog_content.setText(msg);
		}
		return this;
	}

	public OldDialog setPositiveButton(String text,
                                       final OnClickListener listener) {
		// showPosBtn = true;
		if ("".equals(text)) {
			btn_ok.setText("确定");
		} else {
			btn_ok.setText(text);
		}
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	public OldDialog setNegativeButton(String text
	/* final OnClickListener listener */) {
		// showNegBtn = true;
		if ("".equals(text)) {
			btn_no.setText("取消");
		} else {
			btn_no.setText(text);
		}
		btn_no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	public OldDialog setNegativeButton(String text,
                                       final OnClickListener listener ) {
		// showNegBtn = true;
		if ("".equals(text)) {
			btn_no.setText("取消");
		} else {
			btn_no.setText(text);
		}
		btn_no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	public void showDialog() {
		dialog.show();
	}

}
