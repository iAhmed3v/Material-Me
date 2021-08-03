package com.example.android.materialme;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ViewDecorator {

    private Canvas canvas;
    private RecyclerView recyclerView;
    private RecyclerView.ViewHolder viewHolder;
    private float dX;
    private float dY;
    private int actionState;
    private boolean isCurrentlyActive;

    private int swipeLeftBackgroundColor;
    private int swipeLeftActionIconId;
    private Integer swipeLeftActionIconTint;

    private int swipeRightBackgroundColor;
    private int swipeRightActionIconId;
    private Integer swipeRightActionIconTint;

    private int iconHorizontalMargin;

    private String mSwipeLeftText;
    private float mSwipeLeftTextSize = 14;
    private int mSwipeLeftTextUnit = TypedValue.COMPLEX_UNIT_SP;
    private int mSwipeLeftTextColor = Color.DKGRAY;
    private Typeface mSwipeLeftTypeface = Typeface.SANS_SERIF;

    private String mSwipeRightText;
    private float mSwipeRightTextSize = 14;
    private int mSwipeRightTextUnit = TypedValue.COMPLEX_UNIT_SP;
    private int mSwipeRightTextColor = Color.DKGRAY;
    private Typeface mSwipeRightTypeface = Typeface.SANS_SERIF;


    private ViewDecorator() {
        swipeLeftBackgroundColor = 0;
        swipeRightBackgroundColor = 0;
        swipeLeftActionIconId = 0;
        swipeRightActionIconId = 0;
        swipeLeftActionIconTint = null;
        swipeRightActionIconTint = null;
    }


    /**
     * Create a @ViewDecorator
     * @param canvas The canvas which RecyclerView is drawing its children
     * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to
     * @param viewHolder The ViewHolder which is being interacted by the User or it was interacted and simply animating to its original position
     * @param dX The amount of horizontal displacement caused by user's action
     * @param dY The amount of vertical displacement caused by user's action
     * @param actionState The type of interaction on the View. Is either ACTION_STATE_DRAG or ACTION_STATE_SWIPE.
     * @param isCurrentlyActive True if this view is currently being controlled by the user or false it is simply animating back to its original state
     */
    public ViewDecorator(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        this();
        this.canvas = canvas;
        this.recyclerView = recyclerView;
        this.viewHolder = viewHolder;
        this.dX = dX;
        this.dY = dY;
        this.actionState = actionState;
        this.isCurrentlyActive = isCurrentlyActive;
        this.iconHorizontalMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, recyclerView.getContext().getResources().getDisplayMetrics());
    }


    /**
     * Set the background color for either (left/right) swipe directions
     * @param backgroundColor The resource id of the background color to be set
     */
    public void setBackgroundColor(int backgroundColor) {
        this.swipeLeftBackgroundColor = backgroundColor;
        this.swipeRightBackgroundColor = backgroundColor;
    }

    /**
     * Set the action icon for either (left/right) swipe directions
     * @param actionIconId The resource id of the icon to be set
     */
    public void setActionIconId(int actionIconId) {
        this.swipeLeftActionIconId = actionIconId;
        this.swipeRightActionIconId = actionIconId;
    }

    /**
     * Set the background color for left swipe direction
     * @param swipeLeftBackgroundColor The resource id of the background color to be set
     */
    public void setSwipeLeftBackgroundColor(int swipeLeftBackgroundColor) {
        this.swipeLeftBackgroundColor = swipeLeftBackgroundColor;
    }

    /**
     * Set the action icon for left swipe direction
     * @param swipeLeftActionIconId The resource id of the icon to be set
     */
    public void setSwipeLeftActionIconId(int swipeLeftActionIconId) {
        this.swipeLeftActionIconId = swipeLeftActionIconId;
    }


    /**
     * Set the background color for right swipe direction
     * @param swipeRightBackgroundColor The resource id of the background color to be set
     */
    public void setSwipeRightBackgroundColor(int swipeRightBackgroundColor) {
        this.swipeRightBackgroundColor = swipeRightBackgroundColor;
    }

    /**
     * Set the action icon for right swipe direction
     * @param swipeRightActionIconId The resource id of the icon to be set
     */
    public void setSwipeRightActionIconId(int swipeRightActionIconId) {
        this.swipeRightActionIconId = swipeRightActionIconId;
    }


    /**
     * Decorate the RecyclerView item with the chosen backgrounds and icons
     */
    public void decorate() {
        try {
            if ( actionState != ItemTouchHelper.ACTION_STATE_SWIPE ) return;

            if ( dX > 0 ) {
                // Swiping Right
                canvas.clipRect(viewHolder.itemView.getLeft(), viewHolder.itemView.getTop(), viewHolder.itemView.getLeft() + (int) dX, viewHolder.itemView.getBottom());
                if ( swipeRightBackgroundColor != 0 ) {
                    final ColorDrawable background = new ColorDrawable(swipeRightBackgroundColor);
                    background.setBounds(viewHolder.itemView.getLeft(), viewHolder.itemView.getTop(), viewHolder.itemView.getLeft() + (int) dX, viewHolder.itemView.getBottom());
                    background.draw(canvas);
                }

                int iconSize = 0;
                if ( swipeRightActionIconId != 0 && dX > iconHorizontalMargin ) {
                    Drawable icon = ContextCompat.getDrawable(recyclerView.getContext(), swipeRightActionIconId);
                    if ( icon != null ) {
                        iconSize = icon.getIntrinsicHeight();
                        int halfIcon = iconSize / 2;
                        int top = viewHolder.itemView.getTop() + ((viewHolder.itemView.getBottom() - viewHolder.itemView.getTop()) / 2 - halfIcon);
                        icon.setBounds(viewHolder.itemView.getLeft() + iconHorizontalMargin, top, viewHolder.itemView.getLeft() + iconHorizontalMargin + icon.getIntrinsicWidth(), top + icon.getIntrinsicHeight());
                        if (swipeRightActionIconTint != null)
                            icon.setColorFilter(swipeRightActionIconTint, PorterDuff.Mode.SRC_IN);
                        icon.draw(canvas);
                    }
                }

                if ( mSwipeRightText != null && mSwipeRightText.length() > 0 && dX > iconHorizontalMargin + iconSize) {
                    TextPaint textPaint = new TextPaint();
                    textPaint.setAntiAlias(true);
                    textPaint.setTextSize(TypedValue.applyDimension(mSwipeRightTextUnit, mSwipeRightTextSize, recyclerView.getContext().getResources().getDisplayMetrics()));
                    textPaint.setColor(mSwipeRightTextColor);
                    textPaint.setTypeface(mSwipeRightTypeface);

                    int textTop = (int) (viewHolder.itemView.getTop() + ((viewHolder.itemView.getBottom() - viewHolder.itemView.getTop()) / 2.0) + textPaint.getTextSize()/2);
                    canvas.drawText(mSwipeRightText, viewHolder.itemView.getLeft() + iconHorizontalMargin + iconSize + (iconSize > 0 ? iconHorizontalMargin/2 : 0), textTop,textPaint);
                }

            } else if ( dX < 0 ) {
                // Swiping Left
                canvas.clipRect(viewHolder.itemView.getRight() + (int) dX, viewHolder.itemView.getTop(), viewHolder.itemView.getRight(), viewHolder.itemView.getBottom());
                if ( swipeLeftBackgroundColor != 0 ) {
                    final ColorDrawable background = new ColorDrawable(swipeLeftBackgroundColor);
                    background.setBounds(viewHolder.itemView.getRight() + (int) dX, viewHolder.itemView.getTop(), viewHolder.itemView.getRight(), viewHolder.itemView.getBottom());
                    background.draw(canvas);
                }

                int iconSize = 0;
                int imgLeft = viewHolder.itemView.getRight();
                if ( swipeLeftActionIconId != 0 && dX < - iconHorizontalMargin ) {
                    Drawable icon = ContextCompat.getDrawable(recyclerView.getContext(), swipeLeftActionIconId);
                    if ( icon != null ) {
                        iconSize = icon.getIntrinsicHeight();
                        int halfIcon = iconSize / 2;
                        int top = viewHolder.itemView.getTop() + ((viewHolder.itemView.getBottom() - viewHolder.itemView.getTop()) / 2 - halfIcon);
                        imgLeft = viewHolder.itemView.getRight() - iconHorizontalMargin - halfIcon * 2;
                        icon.setBounds(imgLeft, top, viewHolder.itemView.getRight() - iconHorizontalMargin, top + icon.getIntrinsicHeight());
                        if (swipeLeftActionIconTint != null)
                            icon.setColorFilter(swipeLeftActionIconTint, PorterDuff.Mode.SRC_IN);
                        icon.draw(canvas);
                    }
                }

                if ( mSwipeLeftText != null && mSwipeLeftText.length() > 0 && dX < - iconHorizontalMargin - iconSize ) {
                    TextPaint textPaint = new TextPaint();
                    textPaint.setAntiAlias(true);
                    textPaint.setTextSize(TypedValue.applyDimension(mSwipeLeftTextUnit, mSwipeLeftTextSize, recyclerView.getContext().getResources().getDisplayMetrics()));
                    textPaint.setColor(mSwipeLeftTextColor);
                    textPaint.setTypeface(mSwipeLeftTypeface);

                    float width = textPaint.measureText(mSwipeLeftText);
                    int textTop = (int) (viewHolder.itemView.getTop() + ((viewHolder.itemView.getBottom() - viewHolder.itemView.getTop()) / 2.0) + textPaint.getTextSize() / 2);
                    canvas.drawText(mSwipeLeftText, imgLeft - width - ( imgLeft == viewHolder.itemView.getRight() ? iconHorizontalMargin : iconHorizontalMargin/2 ), textTop, textPaint);
                }
            }
        } catch(Exception e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }
    }

    /**
     * A Builder for the ViewDecorator class
     */
    public static class Builder {

        private ViewDecorator mDecorator;




        /**
         * Create a builder for a ViewDecorator
         * @param canvas The canvas which RecyclerView is drawing its children
         * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to
         * @param viewHolder The ViewHolder which is being interacted by the User or it was interacted and simply animating to its original position
         * @param dX The amount of horizontal displacement caused by user's action
         * @param dY The amount of vertical displacement caused by user's action
         * @param actionState The type of interaction on the View. Is either ACTION_STATE_DRAG or ACTION_STATE_SWIPE.
         * @param isCurrentlyActive True if this view is currently being controlled by the user or false it is simply animating back to its original state
         */
        public Builder(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            mDecorator = new ViewDecorator(
                    canvas,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
            );
        }


        /**
         * Add a background color to both swiping directions
         * @param color A single color value in the form 0xAARRGGBB
         * @return This instance of @ViewDecorator.Builder
         */
        public Builder addBackgroundColor(int color) {
            mDecorator.setBackgroundColor(color);
            return this;
        }

        /**
         * Add an action icon to both swiping directions
         * @param drawableId The resource id of the icon to be set
         * @return This instance of @ViewDecorator.Builder
         */
        public Builder addActionIcon(int drawableId) {
            mDecorator.setActionIconId(drawableId);
            return this;
        }


        /**
         * Add a background color while swiping right
         * @param color A single color value in the form 0xAARRGGBB
         * @return This instance of @ViewDecorator.Builder
         */
        public Builder addSwipeRightBackgroundColor(int color) {
            mDecorator.setSwipeRightBackgroundColor(color);
            return this;
        }

        /**
         * Add an action icon while swiping right
         * @param drawableId The resource id of the icon to be set
         * @return This instance of @ViewDecorator.Builder
         */
        public Builder addSwipeRightActionIcon(int drawableId) {
            mDecorator.setSwipeRightActionIconId(drawableId);
            return this;
        }


        /**
         * Adds a background color while swiping left
         * @param color A single color value in the form 0xAARRGGBB
         * @return This instance of @ViewDecorator.Builder
         */
        public Builder addSwipeLeftBackgroundColor(int color) {
            mDecorator.setSwipeLeftBackgroundColor(color);
            return this;
        }

        /**
         * Add an action icon while swiping left
         * @param drawableId The resource id of the icon to be set
         * @return This instance of @ViewDecorator.Builder
         */
        public Builder addSwipeLeftActionIcon(int drawableId) {
            mDecorator.setSwipeLeftActionIconId(drawableId);
            return this;
        }



        /**
         * Create a ViewDecorator
         * @return The created @ViewDecorator
         */
        public ViewDecorator create() {
            return mDecorator;
        }
    }
}
