package com.example.paperlessmeeting_demo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;


public class VoteInfoDialog extends Dialog {
    private VoteInfoDialog(Context context, int themeResId) {
    super(context, themeResId);
}

    public static class Builder {

        private View mLayout;
        private RelativeLayout mlay;
        private ImageView mIcon;
        private TextView mTitle;
        private TextView mMessage;
        private ImageView mButton;

        private View.OnClickListener mButtonClickListener;

        private VoteInfoDialog mDialog;

        public Builder(Context context) {
            mDialog = new VoteInfoDialog(context, R.style.InfoDialog);
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mLayout = inflater.inflate(R.layout.dialog_info, null);
            mlay = mLayout.findViewById(R.id.dialog_header);
            mIcon = mLayout.findViewById(R.id.dialog_icon);
            mTitle = mLayout.findViewById(R.id.dialog_title);
            mMessage = mLayout.findViewById(R.id.dialog_message);
            mButton = mLayout.findViewById(R.id.dialog_close_icon);
        }

        /**
         * Use resource id as Dialog icon
         */
        public Builder setIcon(int resId) {
            mIcon.setImageResource(resId);
            return this;
        }

        /**
         * Use Bitmap as dialog_info icon
         */
        public Builder setIcon(Bitmap bitmap) {
            mIcon.setImageBitmap(bitmap);
            return this;
        }

        public Builder setTitle(@NonNull String title) {
            mTitle.setText(title);
            mTitle.setVisibility(View.VISIBLE);
            return this;
        }

        public Builder setMessage(@NonNull String message) {
            mMessage.setText(message);
            mMessage.setVisibility(View.VISIBLE);
            return this;
        }

        /**
         * Set text and listener for button
         */
        public Builder setButton(View.OnClickListener listener) {
            mButtonClickListener = listener;
            return this;
        }

        public VoteInfoDialog create() {
            mButton.setOnClickListener(view -> {
                if (mButtonClickListener != null)
                    mButtonClickListener.onClick(view);
                mDialog.dismiss();
            });
            mDialog.setContentView(mLayout);
            mDialog.setCancelable(true);                //User can click back to close dialog_info
            mDialog.setCanceledOnTouchOutside(false);   //User can not click outside area to close dialog_info
            return mDialog;
        }
    }
}