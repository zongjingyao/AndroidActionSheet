package cn.zjy.actionsheet;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zongjingyao on 16/10/4.
 */

public class ActionSheet extends DialogFragment implements View.OnClickListener {
    public static final String TAG = "ActionSheet";
    public static final String TITLE = "title";
    public static final String TITLE_COLOR = "title_color";
    public static final String CANCEL_BTN_TITLE = "cancel_btn_title";
    public static final String CANCEL_BTN_TITLE_COLOR = "cancel_btn_title_color";
    public static final String OTHER_BTN_TITLES = "other_btn_titles";
    public static final String OTHER_BTN_TITLE_COLORS = "other_btn_title_colors";
    public static final String CANCELABLE_ON_TOUCH_OUTSIDE = "cancelable_on_touch_outside";

    private static final int DEFAULT_TEXT_SIZE = 18;

    private int mDefaultColor = Color.BLACK;
    private ActionSheetListener mActionSheetListener;
    private int mClickedBtnIdx = -1;

    public void show(FragmentManager fragmentManager) {
        Fragment fragment = fragmentManager.findFragmentByTag(TAG);
        if (fragment != null && fragment instanceof ActionSheet) {
            ((ActionSheet) fragment).dismiss();
        }

        show(fragmentManager, TAG);
    }

    public void setActionSheetListener(ActionSheetListener listener) {
        mActionSheetListener = listener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mActionSheetListener != null) {
            mActionSheetListener.onDismiss(this, mClickedBtnIdx >= 0);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, 0);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM | Gravity.LEFT | Gravity.RIGHT);
            WindowManager.LayoutParams params = window.getAttributes();
            params.windowAnimations = R.style.ActionSheetAnimation;
            window.setAttributes(params);
        }

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return createView();
    }

    private View createView() {
        Context context = getActivity();
        LinearLayout layout = new LinearLayout(context);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        int padding = (int) context.getResources().getDimension(R.dimen.action_sheet_padding);
        layout.setLayoutParams(lp);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setPadding(padding, 0, padding, padding);

        addTitle(layout);
        addOtherBtns(layout);
        addCancelBtn(layout);

        return layout;
    }

    private void addTitle(LinearLayout layout) {
        Bundle args = getArguments();
        String title = args.getString(TITLE);
        if (TextUtils.isEmpty(title)) return;

        int titleColor = args.getInt(TITLE_COLOR, mDefaultColor);
        int titleHeight = (int) getActivity().getResources().getDimension(R.dimen.action_sheet_btn_height);
        int bottomMargin = (int) getActivity().getResources().getDimension(R.dimen.action_sheet_btn_gap);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                titleHeight);
        lp.bottomMargin = bottomMargin;

        int background = R.drawable.action_sheet_other_btns_bg_top;
        String[] titles = args.getStringArray(OTHER_BTN_TITLES);
        if (titles == null || titles.length == 0) {
            background = R.drawable.action_sheet_other_btns_bg_single;
        }
        TextView tvTitle = new TextView(getActivity());
        tvTitle.setText(title);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE);
        tvTitle.setTextColor(titleColor);
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setBackgroundResource(background);
        layout.addView(tvTitle, lp);
    }

    private void addOtherBtns(LinearLayout layout) {
        Bundle args = getArguments();
        String[] titles = args.getStringArray(OTHER_BTN_TITLES);
        if (titles == null || titles.length == 0) return;

        int[] colors = args.getIntArray(OTHER_BTN_TITLE_COLORS);
        int btnHeight = (int) getActivity().getResources().getDimension(R.dimen.action_sheet_btn_height);
        int bottomMargin = (int) getActivity().getResources().getDimension(R.dimen.action_sheet_btn_gap);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                btnHeight);
        lp.bottomMargin = bottomMargin;

        int len = titles.length;
        for (int i = 0; i < len; i++) {
            Button btn = new Button(getActivity());
            btn.setText(titles[i]);
            btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE);
            btn.setTextColor(colors != null && i < colors.length ? colors[i] : mDefaultColor);
            btn.setTag(i);
            btn.setBackgroundResource(getOtherBtnBackground(i));
            btn.setOnClickListener(this);
            layout.addView(btn, lp);
        }
    }

    private int getOtherBtnBackground(int index) {
        int background = R.drawable.action_sheet_other_btns_bg_single;
        Bundle args = getArguments();
        String[] btnTitles = args.getStringArray(OTHER_BTN_TITLES);
        boolean hasTitle = !TextUtils.isEmpty(args.getString(TITLE));
        if (btnTitles == null || btnTitles.length == 0)
            return background;

        int totalCount = btnTitles.length;
        if (hasTitle) {
            totalCount++;
            index++;
        }
        if (totalCount == 1) {
            background = R.drawable.action_sheet_other_btns_bg_single;
        } else {
            if (index == 0) {
                background = R.drawable.action_sheet_other_btns_bg_top;
            } else if (index == totalCount - 1) {
                background = R.drawable.action_sheet_other_btns_bg_bottom;
            } else {
                background = R.drawable.action_sheet_other_btns_bg_middle;
            }
        }

        return background;
    }

    private void addCancelBtn(LinearLayout layout) {
        Bundle args = getArguments();
        String cancelTitle = args.getString(CANCEL_BTN_TITLE);
        if (TextUtils.isEmpty(cancelTitle)) return;

        int cancelBtnColor = args.getInt(CANCEL_BTN_TITLE_COLOR, mDefaultColor);
        int btnHeight = (int) getActivity().getResources().getDimension(R.dimen.action_sheet_btn_height);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                btnHeight);
        lp.topMargin = (int) getActivity().getResources().getDimension(R.dimen.action_sheet_padding);
        String[] otherBtnTitles = args.getStringArray(OTHER_BTN_TITLES);
        int index = otherBtnTitles == null ? 0 : otherBtnTitles.length;

        Button btnCancel = new Button(getActivity());
        btnCancel.setText(cancelTitle);
        btnCancel.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE);
        btnCancel.setTextColor(cancelBtnColor);
        btnCancel.setOnClickListener(this);
        btnCancel.setBackgroundResource(R.drawable.action_sheet_cancel_btn_bg);
        btnCancel.setTag(index);
        layout.addView(btnCancel, lp);
    }

    @Override
    public void onClick(View v) {
        mClickedBtnIdx = (int) v.getTag();
        if (mActionSheetListener != null && mClickedBtnIdx >= 0) {
            mActionSheetListener.onButtonClicked(ActionSheet.this, mClickedBtnIdx);
        }
        dismiss();
    }

    public static class Builder {
        private String mTitle;
        private int mTitleColor;
        private String mCancelBtnTitle;
        private int mCancelBtnTitleColor;
        private String[] mOtherBtnTitles;
        private int[] mOtherBtnTitleColors;
        private boolean mCancelableOnTouchOutside;
        private ActionSheetListener mActionSheetListener;

        public Builder setTitle(String title, int titleColor) {
            mTitle = title;
            mTitleColor = titleColor;
            return this;
        }

        public Builder setCancelBtn(String title, int titleColor) {
            mCancelBtnTitle = title;
            mCancelBtnTitleColor = titleColor;
            return this;
        }

        public Builder setOtherBtn(String[] titles, int[] titleColors) {
            mOtherBtnTitles = titles;
            mOtherBtnTitleColors = titleColors;
            return this;
        }

        public Builder setCancelableOnTouchOutside(boolean cancelable) {
            mCancelableOnTouchOutside = cancelable;
            return this;
        }

        public Builder setActionSheetListener(ActionSheetListener listener) {
            mActionSheetListener = listener;
            return this;
        }

        public ActionSheet build() {
            Bundle bundle = new Bundle();
            bundle.putString(TITLE, mTitle);
            bundle.putInt(TITLE_COLOR, mTitleColor);
            bundle.putString(CANCEL_BTN_TITLE, mCancelBtnTitle);
            bundle.putInt(CANCEL_BTN_TITLE_COLOR, mCancelBtnTitleColor);
            bundle.putStringArray(OTHER_BTN_TITLES, mOtherBtnTitles);
            bundle.putIntArray(OTHER_BTN_TITLE_COLORS, mOtherBtnTitleColors);
            bundle.putBoolean(CANCELABLE_ON_TOUCH_OUTSIDE, mCancelableOnTouchOutside);

            ActionSheet actionSheet = new ActionSheet();
            actionSheet.setActionSheetListener(mActionSheetListener);
            actionSheet.setArguments(bundle);

            return actionSheet;
        }
    }

    public interface ActionSheetListener {
        void onDismiss(ActionSheet actionSheet, boolean isByBtn);

        void onButtonClicked(ActionSheet actionSheet, int index);
    }

}

