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

package com.example.android.materialme;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.materialme.adapter.SportsAdapter;
import com.example.android.materialme.model.Sport;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;


/***
 * Main Activity for the Material Me app, a mock sports news application with poor design choices
 */
public class MainActivity extends AppCompatActivity {

    //Member variables
    private RecyclerView mRecyclerView;
    private ArrayList<Sport> mSportList;
    private SportsAdapter mAdapter;
    private FloatingActionButton fab;

    private TypedArray sportsImageResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);

        //Initialize the RecyclerView
        mRecyclerView = findViewById(R.id.recycler_view);

        //Set the Layout Manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Initialize the ArrayLIst that will contain the data
        mSportList = new ArrayList<>();

        //Initialize the adapter and set it ot the RecyclerView
        mAdapter = new SportsAdapter(this, mSportList);
        mRecyclerView.setAdapter(mAdapter);

        //Get the data
        initializeData();

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper
                .SimpleCallback(
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull  RecyclerView recyclerView ,
                                  @NonNull RecyclerView.ViewHolder viewHolder ,
                                  @NonNull RecyclerView.ViewHolder target) {

                int from = viewHolder.getBindingAdapterPosition();
                int to = target.getBindingAdapterPosition();

                Collections.swap(mSportList, from, to);

                mAdapter.notifyItemMoved(from, to);

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder , int direction) {

                int position = viewHolder.getBindingAdapterPosition();

                Sport mSport = mSportList.get(position);

                mSportList.remove(position);

                mAdapter.notifyItemRemoved(position);

                Snackbar.make(mRecyclerView, R.string.item_removed_text, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo_text , v -> {

                            mAdapter.restoreItem(mSport, position);

                        }).show();
            }

            @Override
            public void onChildDrawOver(@NonNull Canvas c , @NonNull RecyclerView recyclerView , RecyclerView.ViewHolder viewHolder , float dX , float dY , int actionState , boolean isCurrentlyActive) {

                new ViewDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.red))
                        .addActionIcon(R.drawable.ic_delete)
                        .create()
                        .decorate();

            }
        });

        helper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * Method for initializing the sports data from resources.
     */
    private void initializeData() {
        //Get the resources from the XML file
        String[] sportsList = getResources().getStringArray(R.array.sports_titles);
        String[] sportsInfo = getResources().getStringArray(R.array.sports_info);

        sportsImageResources = getResources().obtainTypedArray(R.array.sports_images);

        //Clear the existing data (to avoid duplication)
        mSportList.clear();

        //Create the ArrayList of Sports objects with the titles and information about each sport
        for(int i = 0; i < sportsList.length; i++){

            mSportList.add(new Sport(sportsList[i], sportsInfo[i], sportsImageResources.getResourceId(i,0)));
        }

        //Notify the adapter of the change
        mAdapter.notifyDataSetChanged();

        sportsImageResources.recycle();
    }


    public void refreshSports(View v) {

        initializeData();
    }
}
