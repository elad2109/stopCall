package com.example.stopcall.app.dal.dto;


import android.os.Parcel;
import android.os.Parcelable;

public class Phone implements Parcelable {
    public int id;
    public String phone;
    public boolean isBlocked;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(phone);
        out.writeInt(isBlocked ? 1 : 0);
    }


    public static final Parcelable.Creator<Phone> CREATOR
            = new Parcelable.Creator<Phone>() {
        public Phone createFromParcel(Parcel in) {
            return new Phone(in);
        }

        public Phone[] newArray(int size) {
            return new Phone[size];
        }
    };

    private Phone(Parcel in) {
        id = in.readInt();
        phone = in.readString();
        isBlocked = in.readInt() == 1 ? true : false;
    }

    public Phone() {
    }
}
