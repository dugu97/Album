package com.example.album.ProgressDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.album.R;

public class SuccinctProgress {

	private static ProgressDialog pd;

	public static void showSuccinctProgress(Context context, String message) {

		pd = new ProgressDialog(context, R.style.succinctProgressDialog);
		pd.setCanceledOnTouchOutside(false);
		pd.setCancelable(false);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(400, 400);
		View view = LayoutInflater.from(context).inflate(R.layout.succinct_progress_content, null);
		ImageView mProgressIcon = (ImageView) view
				.findViewById(R.id.progress_icon);

		mProgressIcon.setImageResource(R.drawable.loading);
		TextView mProgressMessage = (TextView) view.findViewById(R.id.progress_message);

		mProgressMessage.setText(message);
		new AnimationUtils();

		Animation jumpAnimation = AnimationUtils.loadAnimation(context, R.anim.succinct_animation);
		mProgressIcon.startAnimation(jumpAnimation);
		pd.show();

		pd.setContentView(view, params);

	}


	public static boolean isShowing() {

		return pd != null && pd.isShowing();
	}

	public static void dismiss() {

		if (isShowing()) {
			pd.dismiss();
		}

	}
}
