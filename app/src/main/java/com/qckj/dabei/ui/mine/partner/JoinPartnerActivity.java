package com.qckj.dabei.ui.mine.partner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.model.mine.MemberInfo;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 加入合伙人
 * <p>
 * Created by yangzhizhong on 2019/3/20.
 */
public class JoinPartnerActivity extends BaseActivity {

    @FindViewById(R.id.member_list_view)
    private RecyclerView memberRecyclerView;

    @FindViewById(R.id.is_scan_agreement_btn)
    private CheckBox isScanAgreementBtn;

    @FindViewById(R.id.scan_agreement_btn)
    private LinearLayout scanAgreementBtn;

    @FindViewById(R.id.open_member_btn)
    private Button openMemberBtn;

    private MemberAdapter memberAdapter;
    private boolean isCheck = true;
    private MemberInfo selectMemberInfo;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, JoinPartnerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_partner);
        ViewInject.inject(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setInitialPrefetchItemCount(5);
        memberRecyclerView.setLayoutManager(linearLayoutManager);
        memberAdapter = new MemberAdapter(getActivity());
        memberRecyclerView.setAdapter(memberAdapter);
    }

    private void initData() {
        List<MemberInfo> memberInfos = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            MemberInfo memberInfo = new MemberInfo();
            memberInfo.setMemberName("商家" + i);
            memberInfo.setMemberLogo("" + i);
            memberInfo.setMemberIntroduce("商家介绍" + i);
            memberInfo.setMemberPrice("18" + i);
            memberInfos.add(memberInfo);
        }
        memberAdapter.setInfos(memberInfos);
    }

    private void initListener() {
        memberAdapter.setOnItemClickListener(new MemberAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, MemberInfo info) {
                selectMemberInfo = info;
                memberAdapter.setCurPosition(position);
            }
        });
        isScanAgreementBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheck = isChecked;
            }
        });
        scanAgreementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2019/4/12 跳转协议界面
            }
        });
        openMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheck) {

                } else {
                    showToast("请阅读合伙人协议");
                }
            }
        });
    }

}
