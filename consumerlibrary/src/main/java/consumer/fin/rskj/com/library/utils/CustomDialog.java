package consumer.fin.rskj.com.library.utils;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import consumer.fin.rskj.com.consumerlibrary.R;


public class CustomDialog extends Dialog {
	public CustomDialog(Context context) {
		super(context);
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String title;
		private int icon;
		private String message;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private OnClickListener positiveButtonClickListener;
		private OnClickListener negativeButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 * 
		 * @param message
		 * @return
		 * */
		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 * */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * * Set the Dialog title from String * * @param title * @return
		 * */
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}
        public Builder setIcon(int icon)
        {
        	this.icon = icon;
			return this;
        }
        
		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * * Set the positive button resource and it's listener * * @param
		 * positiveButtonText * @return
		 * */
		public Builder setPositiveButton(int positiveButtonText,
				OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
				OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}
		public Builder setNegativeButton(int negativeButtonText,
				OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}
		public Builder setNegativeButton(String negativeButtonText,
				OnClickListener listener) {
			this.negativeButtonText =negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}
		public CustomDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CustomDialog dialog = new CustomDialog(context,
					R.style.Dialog);
			View layout = inflater.inflate(R.layout.rskj_dialog_normal_layout, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			View tipLineView=layout.findViewById(R.id.dialog_tip_line);
			View lineView=layout.findViewById(R.id.dialog_line);
			if(title!=null)
			{
				((TextView) layout.findViewById(R.id.title)).setText(title);
				tipLineView.setVisibility(View.VISIBLE);
			}else
			{
				layout.findViewById(R.id.title).setVisibility(View.GONE);
				tipLineView.setVisibility(View.GONE);
			}
			if (icon!=0) {
				layout.findViewById(R.id.icon).setBackgroundResource(icon);
			}else
			{
				layout.findViewById(R.id.icon).setVisibility(View.GONE);
			}
			// set the confirm button
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.positiveButton))
						.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					layout.findViewById(R.id.positiveButton)
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);

								}
							});
				}
			} else {
				lineView.setVisibility(View.GONE);
				layout.findViewById(R.id.positiveButton).setVisibility(
						View.GONE);
			}

			if (negativeButtonText != null) {
				((Button) layout.findViewById(R.id.negativeButton))
						.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					layout.findViewById(R.id.negativeButton)
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);

								}
							});
				}
			} else {
				lineView.setVisibility(View.GONE);
				layout.findViewById(R.id.negativeButton).setVisibility(
						View.GONE);
			}

			if (message != null) {
				((TextView) layout.findViewById(R.id.message)).setText(message);
				if(icon==0)
				{
					((TextView) layout.findViewById(R.id.message)).setGravity(Gravity.CENTER);	
				}

			} else if (contentView != null) {
				// if no message set
				// add the contentView to the dialog body
				((LinearLayout) layout.findViewById(R.id.content))
						.removeAllViews();
				((LinearLayout) layout.findViewById(R.id.content)).addView(
						contentView, new LayoutParams(LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT));

			}
			dialog.setContentView(layout);
			return dialog;

		}
	}
}