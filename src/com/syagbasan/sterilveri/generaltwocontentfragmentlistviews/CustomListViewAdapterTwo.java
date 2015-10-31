/*
 * Copyright (c) 2014 Semih YAGBASAN, YAGBASAN HO
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.syagbasan.sterilveri.generaltwocontentfragmentlistviews;

import java.util.List;

import com.syagbasan.sterilveri.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
 
public class CustomListViewAdapterTwo extends ArrayAdapter<RowItemTwo> {
 
    Context context;
 
    public CustomListViewAdapterTwo(Context context, int resourceId,
            List<RowItemTwo> items) {
        super(context, resourceId, items);
        this.context = context;
    }
     
    /*private view holder class*/
    private class ViewHolder {
        TextView txtSatilanKisi;
        TextView txtSatisMiktari;
    }
     
    @SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItemTwo rowItem = getItem(position);
         
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.content_list_item, null);
            holder = new ViewHolder();
            holder.txtSatisMiktari = (TextView) convertView.findViewById(R.id.tvSatisMiktari);
            holder.txtSatilanKisi = (TextView) convertView.findViewById(R.id.tvSatilanKisi);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
                 
        holder.txtSatisMiktari.setText(rowItem.getAmount());
        holder.txtSatilanKisi.setText(rowItem.getPerson());
         
        return convertView;
    }
}