package com.supreeta.dsgalpalli.foodvana.foodvana.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Supreeta on 1/20/2016.
 */
public class Product implements Parcelable {
    String product;
    int CatId;
    int ProId;

    public int getCatId() {
        return CatId;
    }

    public void setCatId(int catId) {
        CatId = catId;
    }

    public int getProId() {
        return ProId;
    }

    public void setProId(int proId) {
        ProId = proId;
    }

    public Product()
    {

    }

    public Product(String product, int catId,int proId) {
        this.product = product;
        this.CatId=catId;
        this.ProId=proId;

    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }



    @Override
    public String toString() {
        return ""+product;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags)
    {
        parcel.writeString(this.product);
        parcel.writeInt(this.CatId);
        parcel.writeInt(this.ProId);


    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel parcel)
        {
            Product product=new Product();
            product.product=parcel.readString();
            product.CatId=parcel.readInt();
            product.ProId=parcel.readInt();



            return product;

        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
