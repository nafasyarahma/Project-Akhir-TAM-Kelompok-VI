package com.example.financialapp;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemWalletRecyclerAdapter extends RecyclerView.Adapter<ItemWalletRecyclerAdapter.MyViewHolder> {
    private ArrayList<ItemWalletDetail> listItem;

    private  int income;
    private  int expense;
    private int balance;

    public ItemWalletRecyclerAdapter(ArrayList<ItemWalletDetail> _listItem) {
        listItem = _listItem;
    }

    @NonNull
    @Override
    public ItemWalletRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet_detail, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemWalletRecyclerAdapter.MyViewHolder holder, int position) {
        RefreshDetail(holder, listItem.get(position));
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public int GetExpense(){
        return expense;
    }

    public int GetIncome(){
        return income;
    }

    public int GetBalance(){
        return balance;
    }

    public void RefreshWallet(){
        ItemWalletDetail.EnumType type;
        income = 0;
        expense = 0;
        balance = 0;

        for (ItemWalletDetail _item : listItem) {
            type = _item.GetType();
            if(type == ItemWalletDetail.EnumType.Pengeluaran){
                expense += _item.GetPriceInt();
            }else {
                income += _item.GetPriceInt();
            }
        }

        balance = income - expense;
    }

    private void RefreshDetail(MyViewHolder _holder, ItemWalletDetail _item){
        ItemWalletDetail.EnumType type = _item.GetType();
        ItemWalletDetail.EnumCategory category = _item.GetCategory();
        String date = _item.GetDate();
        String desc = _item.GetDesc();
        String price = _item.GetPrice();

        Drawable layoutBg = _holder.layoutBg.getBackground();
        Drawable wrapped = DrawableCompat.wrap(layoutBg);
        switch (type){
            case Pengeluaran:
                DrawableCompat.setTint(wrapped, Color.argb(255,255,93,182));
                break;

            case Pemasukan:
                DrawableCompat.setTint(wrapped, Color.argb(255,114,191,254));
                break;
        }

        switch (category){
            case Tagihan:
                _holder.imgIcon.setBackgroundResource(R.drawable.bill);
                break;
            case Hobi:
                _holder.imgIcon.setBackgroundResource(R.drawable.hobbies);
                break;
            case Belanja:
                _holder.imgIcon.setBackgroundResource(R.drawable.shopping_bag);
                break;
            case Hadiah:
                _holder.imgIcon.setBackgroundResource(R.drawable.business_and_finance);
                break;
            case Gaji:
                _holder.imgIcon.setBackgroundResource(R.drawable.salary);
                break;
        }

        _holder.textDate.setText(date);
        _holder.textCategory.setText(category.name());
        _holder.textDesc.setText(desc);
        _holder.textPrice.setText(price);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout layoutBg;

        private ImageView imgIcon;

        private TextView textDate;
        private TextView textCategory;
        private TextView textDesc;
        private TextView textPrice;

        public MyViewHolder(final View itemView) {
            super(itemView);

            layoutBg = itemView.findViewById(R.id.item_wallet_bg);
            imgIcon = itemView.findViewById(R.id.item_wallet_icon);
            textDate = itemView.findViewById(R.id.item_wallet_date);
            textCategory = itemView.findViewById(R.id.item_wallet_category);
            textDesc = itemView.findViewById(R.id.item_wallet_desc);
            textPrice = itemView.findViewById(R.id.item_wallet_price);
        }
    }
}
