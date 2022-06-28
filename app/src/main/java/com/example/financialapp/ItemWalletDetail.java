package com.example.financialapp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemWalletDetail {
    public enum EnumType{
        Pengeluaran,
        Pemasukan
    }

    public enum EnumCategory {
        Tagihan,
        Hobi,
        Belanja,

        Hadiah,
        Gaji
    }

    private Date date;

    private EnumType type;
    private EnumCategory category;

    private String desc;
    private int price;

    public ItemWalletDetail(Date _date, EnumType _type, EnumCategory _category, String _desc, int _price){
        date = _date;
        type = _type;
        category = _category;
        desc = _desc;
        price = _price;
    }

    public EnumType GetType(){
        return type;
    }

    public EnumCategory GetCategory(){
        return category;
    }

    public String GetDate(){
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    public String GetDesc(){
        return desc;
    }

    public String GetPrice(){
        String _price = "";

        if(type == EnumType.Pengeluaran)
            _price = "- Rp." + price;
        else
            _price = "+ Rp." + price;

        return _price;
    }

    public int GetPriceInt(){
        return price;
    }
}
