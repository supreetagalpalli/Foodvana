package com.supreeta.dsgalpalli.foodvana.foodvana.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Supreeta on 1/23/2016.
 */
public class User implements Parcelable
{
    String email;
    String phoneno;

    public User() {
    }


    public User(String email, String phoneno) {
        this.email = email;
        this.phoneno = phoneno;
    }

    protected User(Parcel in) {
        email = in.readString();
        phoneno = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel parcel)
        {
            User user=new User();
            user.email=parcel.readString();
            user.phoneno=parcel.readString();
            return user;
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", phoneno='" + phoneno + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags)
    {

        parcel.writeString(this.email);
        parcel.writeString(this.phoneno);
    }


}

