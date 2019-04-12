package com.qckj.dabei.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.Toast;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;

import java.util.List;

/**
 * 选择dialog
 * <p>
 * Created by yangzhizhong on 2019/3/28.
 */
public class SelectDialog extends Dialog {

    @FindViewById(R.id.list_view)
    private ListView mListView;

    private Context context;
    private List<SelectItem> items;
    private SelectDialogAdapter selectDialogAdapter;

    public SelectDialog(@NonNull Context context, List<SelectItem> items) {
        super(context, R.style.SelectDialog);
        this.context = context;
        this.items = items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_view);
        ViewInject.inject(this);
        init();
        initListener();
    }

    private void initListener() {
        selectDialogAdapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<SelectItem>() {
            @Override
            public void onAdapterItemClick(int position, SelectItem selectItem) {
                Toast.makeText(context, selectItem.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        getWindow().setGravity(Gravity.BOTTOM);
        selectDialogAdapter = new SelectDialogAdapter(context);
        mListView.setAdapter(selectDialogAdapter);
        selectDialogAdapter.setData(items);
    }
}
