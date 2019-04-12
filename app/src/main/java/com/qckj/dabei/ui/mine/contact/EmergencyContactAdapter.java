package com.qckj.dabei.ui.mine.contact;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.model.contact.ContactInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 紧急联系人是适配器
 * <p>
 * Created by yangzhizhong on 2019/3/20.
 */
public class EmergencyContactAdapter extends RecyclerView.Adapter<EmergencyContactAdapter.EmergencyContactViewHolder> {

    private Context mContent;
    private List<ContactInfo> mContactInfoList;

    public EmergencyContactAdapter(Context context) {
        this.mContent = context;
        mContactInfoList = new ArrayList<>();
    }

    public void setContactInfoList(List<ContactInfo> contactInfoList) {
        mContactInfoList.clear();
        mContactInfoList = contactInfoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EmergencyContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContent).inflate(R.layout.emergency_contact_item_view, parent, false);
        return new EmergencyContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyContactViewHolder emergencyContactViewHolder, int i) {
        ContactInfo contactInfo = mContactInfoList.get(i);
        emergencyContactViewHolder.contactName.setText(contactInfo.getContactName());
        emergencyContactViewHolder.contactPhone.setText(contactInfo.getContactPhone());
        if (i == (getItemCount() - 1)) {
            emergencyContactViewHolder.line.setVisibility(View.GONE);
        } else {
            emergencyContactViewHolder.line.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mContactInfoList.size();
    }

    static class EmergencyContactViewHolder extends RecyclerView.ViewHolder {

        TextView contactName;
        TextView contactPhone;
        View line;

        EmergencyContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contact_name);
            contactPhone = itemView.findViewById(R.id.contact_phone);
            line = itemView.findViewById(R.id.line);
        }
    }
}
