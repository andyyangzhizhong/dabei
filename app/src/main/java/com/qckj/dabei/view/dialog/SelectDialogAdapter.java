package com.qckj.dabei.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * Created by yangzhizhong on 2019/3/28.
 */
public class SelectDialogAdapter extends SimpleBaseAdapter<SelectItem, SelectDialogAdapter.ViewHolder> {

    public SelectDialogAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_select_item_view;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, SelectItem selectItem, int position) {
        viewHolder.itemView.setText(selectItem.getName());
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.item_name)
        private TextView itemView;
    }
}
