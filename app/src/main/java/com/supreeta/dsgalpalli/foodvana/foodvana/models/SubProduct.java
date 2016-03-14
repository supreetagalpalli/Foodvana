package com.supreeta.dsgalpalli.foodvana.foodvana.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Supreeta on 1/20/2016.
 */
public class SubProduct implements Parcelable {
    String Title;
    float Price;
    String Description;
    int subproduct_id;
    String image;

    public SubProduct() {
    }

    public SubProduct(String title, float price, String description,int subproductid,String image_name) {
        Title = title;
        Price = price;
        Description = description;
        subproduct_id=subproductid;
        image=image_name;

    }


    protected SubProduct(Parcel in) {
        Title = in.readString();
        Price = in.readFloat();
        Description = in.readString();
        subproduct_id=in.readInt();
        image=in.readString();
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getSubproduct_id() {
        return subproduct_id;
    }

    public void setSubproduct_id(int subproduct_id) {
        this.subproduct_id = subproduct_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return ""+ Title +
                " Price " + Price
                ;
    }

    public static final Creator<SubProduct> CREATOR = new Creator<SubProduct>() {
        @Override
        public SubProduct createFromParcel(Parcel parcel)
        {
            SubProduct subProduct =new SubProduct();
            subProduct.Title=parcel.readString();
            subProduct.Price=parcel.readFloat();
            subProduct.Description=parcel.readString();
            subProduct.subproduct_id=parcel.readInt();
            subProduct.image=parcel.readString();


            return subProduct;
        }

        @Override
        public SubProduct[] newArray(int size) {
            return new SubProduct[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Title);
        dest.writeFloat(Price);
        dest.writeString(Description);
        dest.writeInt(subproduct_id);
        dest.writeString(image);
    }
}
