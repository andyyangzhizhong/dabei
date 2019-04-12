package com.qckj.dabei.ui.mine.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.model.contact.ContactInfo;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 紧急联系人
 * <p>
 * Created by yangzhizhong on 2019/3/20.
 */
public class EmergencyContactActivity extends BaseActivity {

    @FindViewById(R.id.emergency_contact_list)
    private RecyclerView mEmergencyContactList;
    private EmergencyContactAdapter mEmergencyContactAdapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, EmergencyContactActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);
        ViewInject.inject(this);
        init();
        initData();
    }

    private void initData() {
        List<ContactInfo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ContactInfo info = new ContactInfo();
            info.setContactName("大贝" + i);
            info.setContactPhone("182467834" + i);
            list.add(info);
        }
        mEmergencyContactAdapter.setContactInfoList(list);
    }

    private void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mEmergencyContactList.setLayoutManager(linearLayoutManager);
        mEmergencyContactAdapter = new EmergencyContactAdapter(this);
        mEmergencyContactList.setAdapter(mEmergencyContactAdapter);
    }

    @OnClick(R.id.add_contact_btn)
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.add_contact_btn:
                EmergencyContactAddActivity.startActivity(getActivity());
                break;
        }
    }
}

