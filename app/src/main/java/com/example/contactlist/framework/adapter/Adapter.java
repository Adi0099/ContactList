package com.example.contactlist.framework.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.contactlist.R;
import com.example.contactlist.framework.model.DataBaseModel;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ContactViewHolder>{

    private List<DataBaseModel> contactVOList;
    private Context mContext;
    public Adapter(List<DataBaseModel> contactVOList, Context mContext){
        this.contactVOList = contactVOList;
        this.mContext = mContext;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.contacts_list_item, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        DataBaseModel contactVO = contactVOList.get(position);
        holder.tvContactId.setText(contactVO.getCid());
        holder.tvContactName.setText(contactVO.getName());
        holder.tvPhoneNumber.setText(contactVO.getNumber());
    }

    @Override
    public int getItemCount() {
        return contactVOList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{

        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;
        TextView tvContactId;

        public ContactViewHolder(View itemView) {
            super(itemView);
            ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            tvContactId = (TextView) itemView.findViewById(R.id.cid);
            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
        }
    }
}