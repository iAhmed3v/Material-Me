/*
 * Copyright (C) 2016 Google Inc.
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

package com.example.android.materialme.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.materialme.DetailActivity;
import com.example.android.materialme.R;
import com.example.android.materialme.model.Sport;

import java.util.ArrayList;

/***
 * The adapter class for the RecyclerView, contains the sports data
 */
public class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.ViewHolder>{

    //Member variables
    public ArrayList<Sport> mSportList;
    private Context mContext;

    /**
     * Constructor that passes in the sports data and the context
     * @param sportList ArrayList containing the sports data
     * @param context Context of the application
     */
    public SportsAdapter(Context context , ArrayList <Sport> sportList) {
        this.mSportList = sportList;
        this.mContext = context;
    }


    /**
     * Required method for creating the viewHolder objects.
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return The newly create ViewHolder.
     */
    @Override
    public SportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    /**
     * Required method that binds the data to the viewHolder.
     * @param holder The viewHolder into which the data should be put.
     * @param position The adapter position.
     */
    @Override
    public void onBindViewHolder(SportsAdapter.ViewHolder holder, int position) {
        //Get current sport
        Sport currentSport = mSportList.get(position);
        //Populate the textviews with data
        holder.bindTo(currentSport);
    }


    /**
     * Required method for determining the size of the data set.
     * @return Size of the data set.
     */
    @Override
    public int getItemCount() {
        return mSportList.size();
    }

    /**
     * ViewHolder class that represents each row of data in the RecyclerView
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Member Variables for the TextViews
        private TextView mTitleText;
        private TextView mInfoText;
        private ImageView mSportsImage;

        /**
         * Constructor for the ViewHolder, used in onCreateViewHolder().
         * @param itemView The rootview of the list_item.xml layout file
         */
        ViewHolder(View itemView) {
            super(itemView);

            //Initialize the views
            mTitleText = itemView.findViewById(R.id.title);
            mInfoText = itemView.findViewById(R.id.subTitle);
            mSportsImage = itemView.findViewById(R.id.sports_image);

            itemView.setOnClickListener((View.OnClickListener) this);
        }

        void bindTo(Sport currentSport){
            //Populate the textviews with data
            mTitleText.setText(currentSport.getTitle());
            mInfoText.setText(currentSport.getInfo());

            Glide.with(mContext).load(currentSport.getImageResource()).into(mSportsImage);


        }

        @Override
        public void onClick(View v) {

            Sport currentSport = mSportList.get(getBindingAdapterPosition());


            Intent detailIntent = new Intent(mContext, DetailActivity.class);
            detailIntent.putExtra("title", currentSport.getTitle());
            detailIntent.putExtra("imageResource", currentSport.getImageResource());

            mContext.startActivity(detailIntent);
        }
    }

    //This is methode for restoring the deleted items
    public void restoreItem(Sport mSport, int position){

        mSportList.add(position, mSport);

        notifyItemInserted(position);
    }
}
