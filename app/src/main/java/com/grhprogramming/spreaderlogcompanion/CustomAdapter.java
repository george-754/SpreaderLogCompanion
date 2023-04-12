package com.grhprogramming.spreaderlogcompanion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<DataModel> {

    private ArrayList<DataModel> dataSet;
    Context mContext;
    private DbHandler db;

    // View lookup cache
    private static class ViewHolder {
        TextView txtId;
        TextView txtDate;
        TextView txtFarmer;
        TextView txtField;
        TextView txtProduct;
        TextView txtAcres;
    }

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.main_row_item, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        //Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // View lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.main_row_item, parent, false);
            viewHolder.txtId = (TextView) convertView.findViewById(R.id.row_id);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.row_date);
            viewHolder.txtFarmer = (TextView) convertView.findViewById(R.id.row_farmer);
            viewHolder.txtField = (TextView) convertView.findViewById(R.id.row_field);
            viewHolder.txtProduct = (TextView) convertView.findViewById(R.id.row_product);
            viewHolder.txtAcres = (TextView) convertView.findViewById(R.id.row_acres);

            result = convertView;

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.txtId.setText(dataModel.getId());
        viewHolder.txtDate.setText(dataModel.getDate());
        viewHolder.txtFarmer.setText(dataModel.getFarmer());
        viewHolder.txtField.setText(dataModel.getField());
        viewHolder.txtProduct.setText(dataModel.getProduct());
        viewHolder.txtAcres.setText(dataModel.getAcres());

        // Return the completed view to render on screen
        return convertView;
    }
}
