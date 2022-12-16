package com.example.fyp3;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class orderAdapter extends ArrayAdapter<OrderClass> {

    private Activity context;
    private List<OrderClass> userList;

    public orderAdapter (Activity context, List<OrderClass> userList)
    {
        super(context, R.layout.order_list, userList);
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.order_list,null,true);
        OrderClass orderClass = userList.get(position);

        TextView date_id,time_id,ordertype_id,orderstatus_id,remark_id, address_id;

        date_id = view.findViewById(R.id.date_id);
        time_id = view.findViewById(R.id.time_id);
        ordertype_id = view.findViewById(R.id.ordertype_id);
        orderstatus_id = view.findViewById(R.id.orderstatus_id);
        remark_id = view.findViewById(R.id.remark_id);
        address_id = view.findViewById(R.id.address_id);


        date_id.setText(userList.get(position).getDate());
        time_id.setText(userList.get(position).getTime());
        ordertype_id.setText(userList.get(position).getOrderType());
        orderstatus_id.setText(userList.get(position).getOrder_status());
        remark_id.setText(userList.get(position).getOrderRemark());
        address_id.setText(userList.get(position).getOrderAddress());


        return view;
    }
}
