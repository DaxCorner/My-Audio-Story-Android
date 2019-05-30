package com.doozycod.childrenaudiobook.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.doozycod.childrenaudiobook.R;


public class CustomProgressBar {
	private Dialog popDialog;
	private Context context;

	public CustomProgressBar(Context context) {

		this.context = context;

	}

	/*
	 * This method display a message or alert for any functionality
	 */
	public void showProgress() {
		popDialog = new Dialog(context);
		popDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		popDialog.setContentView(R.layout.progressbar);
		popDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		popDialog.setCancelable(false);


		popDialog.show();
	}

	public void hideProgress() {

		if (popDialog != null) {
			popDialog.dismiss();

		}
	}

}
