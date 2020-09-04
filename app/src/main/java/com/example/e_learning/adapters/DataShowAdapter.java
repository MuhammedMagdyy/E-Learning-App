package com.example.e_learning.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_learning.R;
import com.example.e_learning.models.Data;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DataShowAdapter extends RecyclerView.Adapter<DataShowAdapter.DataViewHloder> {
    ArrayList<Data> mData;

    public void setmData(ArrayList<Data> mData1) {
        this.mData = mData1;
    }

    @NonNull
    @Override
    public DataViewHloder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
        return new DataViewHloder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHloder holder, int position) {
        holder.fullName.setText(mData.get(position).full_name);
        holder.phoneNumber.setText(mData.get(position).phone_student);
        holder.phoneParents.setText(mData.get(position).phone_parents);
    }

    @Override
    public int getItemCount() {
        if (mData == null){
            return 0;
        } else {
            return mData.size();
        }
    }

    public class DataViewHloder extends RecyclerView.ViewHolder {
        TextView fullName, phoneNumber, phoneParents;
        public DataViewHloder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.fullName_data);
            phoneNumber = itemView.findViewById(R.id.phoneStudent_data);
            phoneParents = itemView.findViewById(R.id.phoneParents_data);
        }
    }

}
