package com.qckj.dabei.model.contact;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * 联系人信息
 * <p>
 * Created by yangzhizhong on 2019/3/20.
 */
public class ContactInfo implements Parcelable {

    private String contactName;

    private String contactPhone;

    private ContactInfo(Parcel in) {
        contactName = in.readString();
        contactPhone = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contactName);
        dest.writeString(contactPhone);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ContactInfo> CREATOR = new Creator<ContactInfo>() {
        @Override
        public ContactInfo createFromParcel(Parcel in) {
            return new ContactInfo(in);
        }

        @Override
        public ContactInfo[] newArray(int size) {
            return new ContactInfo[size];
        }
    };


    public ContactInfo() {
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    @NonNull
    @Override
    public String toString() {
        return "ContactInfo{" +
                "contactName='" + contactName + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                '}';
    }
}
