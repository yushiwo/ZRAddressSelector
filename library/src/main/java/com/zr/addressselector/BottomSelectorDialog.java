package com.zr.addressselector;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.zr.addressselector.listener.OnAddressSelectedListener;
import com.zr.addressselector.util.ResUtils;


public class BottomSelectorDialog extends Dialog {

    public AddressSelector getSelector() {
        return selector;
    }

    private AddressSelector selector;

    public BottomSelectorDialog(Context context) {
        super(context, R.style.bottom_dialog);
        init(context);
    }

    public BottomSelectorDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    public BottomSelectorDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        selector = new AddressSelector(context);

        setContentView(selector.getView());

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = ResUtils.dp2px(context, 256);
        window.setAttributes(params);

        window.setGravity(Gravity.BOTTOM);
    }

    public void setOnAddressSelectedListener(OnAddressSelectedListener listener) {
        this.selector.setOnAddressSelectedListener(listener);
    }

    public static BottomSelectorDialog show(Context context) {
        return show(context, null);
    }

    public static BottomSelectorDialog show(Context context, OnAddressSelectedListener listener) {
        BottomSelectorDialog dialog = new BottomSelectorDialog(context, R.style.bottom_dialog);
        dialog.selector.setOnAddressSelectedListener(listener);
        dialog.show();

        return dialog;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        this.selector.clearCacheData();
    }
}
