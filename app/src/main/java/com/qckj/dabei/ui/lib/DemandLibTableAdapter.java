package com.qckj.dabei.ui.lib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.lib.DemandLibInfo;
import com.qckj.dabei.util.DateUtils;
import com.qckj.dabei.util.GlideUtil;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * 需求库适配器
 * <p>
 * Created by yangzhizhong on 2019/4/9.
 */
public class DemandLibTableAdapter extends SimpleBaseAdapter<DemandLibInfo, DemandLibTableAdapter.ViewHolder> {

    public DemandLibTableAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.demand_lib_item_view;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, DemandLibInfo demandLibInfo, int position) {
        GlideUtil.displayImage(getContext(), demandLibInfo.getTxImg(), viewHolder.icon, R.mipmap.ic_launcher);
        viewHolder.name.setText(demandLibInfo.getName());
        if ("1".equals(demandLibInfo.getIsPay())) {
            viewHolder.payState.setText("已预付");
            viewHolder.payState.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_unprepaid_circlel));
        } else if ("2".equals(demandLibInfo.getIsPay())) {
            viewHolder.payState.setText("未预付");
            viewHolder.payState.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_prepaid_circlel));
        }
        viewHolder.content.setText(demandLibInfo.getMes());
        viewHolder.distance.setText(demandLibInfo.getDistance() + "km");
        viewHolder.time.setText(DateUtils.getTimeStringByMillisecondsWithFormatString(demandLibInfo.getTime(), "yyyy-MM-dd"));
        viewHolder.contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.icon)
        private ImageView icon;
        @FindViewById(R.id.name)
        private TextView name;
        @FindViewById(R.id.pay_state)
        private TextView payState;
        @FindViewById(R.id.content)
        private TextView content;
        @FindViewById(R.id.distance)
        private TextView distance;
        @FindViewById(R.id.contact_btn)
        private Button contactBtn;
        @FindViewById(R.id.time)
        private TextView time;
    }
}
