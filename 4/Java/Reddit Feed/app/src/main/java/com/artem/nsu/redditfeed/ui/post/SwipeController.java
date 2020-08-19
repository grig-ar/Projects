package com.artem.nsu.redditfeed.ui.post;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import com.artem.nsu.redditfeed.enums.ButtonsState;
import com.artem.nsu.redditfeed.ui.feed.FeedAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE;
import static androidx.recyclerview.widget.ItemTouchHelper.RIGHT;
import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;

public class SwipeController extends Callback {

    private static final float WIDTH_BUTTON = 200;

    private boolean mSwipeBack = false;
    private ButtonsState mButtonsState = ButtonsState.GONE;
    private Boolean mIsFavorite = false;
    private RectF mButtonInstance = null;
    private RecyclerView.ViewHolder mCurrentItemViewHolder = null;
    private SwipeControllerActions mButtonsActions = null;

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT | RIGHT);
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (mSwipeBack) {
            mSwipeBack = mButtonsState != ButtonsState.GONE;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ACTION_STATE_SWIPE) {
            if (mButtonsState != ButtonsState.GONE) {
                if (mButtonsState == ButtonsState.LEFT_VISIBLE) {
                    dX = Math.max(dX, WIDTH_BUTTON);
                }
                if (mButtonsState == ButtonsState.RIGHT_VISIBLE) {
                    dX = Math.min(dX, -WIDTH_BUTTON);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            } else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
        if (mButtonsState == ButtonsState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        mCurrentItemViewHolder = viewHolder;
    }

    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
        float buttonWidthWithoutPadding = WIDTH_BUTTON - 10;
        float corners = 8;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        RectF leftButton = new RectF(itemView.getLeft(), itemView.getBottom() - buttonWidthWithoutPadding - 75f,
                itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom() - 75f);

        RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding,
                itemView.getBottom() - buttonWidthWithoutPadding - 75f, itemView.getRight(), itemView.getBottom() - 75f);

        mButtonInstance = null;

        if (mButtonsState == ButtonsState.LEFT_VISIBLE && !mIsFavorite) {
            p.setColor(Color.GREEN);
            c.drawRoundRect(leftButton, corners, corners, p);
            drawText("Save", c, leftButton, p);
            mButtonInstance = leftButton;
        } else if (mButtonsState == ButtonsState.LEFT_VISIBLE && mIsFavorite) {
            p.setColor(Color.RED);
            c.drawRoundRect(leftButton, corners, corners, p);
            drawText("Delete", c, leftButton, p);
            mButtonInstance = leftButton;
        } else if (mButtonsState == ButtonsState.RIGHT_VISIBLE) {
            p.setColor(Color.BLUE);
            c.drawRoundRect(rightButton, corners, corners, p);
            drawText("Share", c, rightButton, p);
            mButtonInstance = rightButton;
        }
    }

    private void drawText(String text, Canvas c, RectF button, Paint paint) {
        float textSize = 30;
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);

        float textWidth = paint.measureText(text);
        c.drawText(text, button.centerX() - (textWidth / 2), button.centerY() + (textSize / 2), paint);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  float dx, float dy, int actionState, boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener((v, event) -> {
            mSwipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
            if (mSwipeBack) {
                setFavorite(((FeedAdapter.PostViewHolder) viewHolder).getPostEntity().isFavorite());
                if (dx < -WIDTH_BUTTON) {
                    mButtonsState = ButtonsState.RIGHT_VISIBLE;
                } else if (dx > WIDTH_BUTTON) {
                    mButtonsState = ButtonsState.LEFT_VISIBLE;
                }
                if (mButtonsState != ButtonsState.GONE) {
                    setTouchDownListener(c, recyclerView, viewHolder, dx, dy, actionState, isCurrentlyActive);
                    setItemsClickable(recyclerView, false);
                }
            }
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder,
                                      final float dx, final float dy, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(c, recyclerView, viewHolder, dx, dy, actionState, isCurrentlyActive);
            }
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder,
                                    final float dx, final float dy, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                SwipeController.super.onChildDraw(c, recyclerView, viewHolder, 0f, dy, actionState, isCurrentlyActive);
                recyclerView.setOnTouchListener((v1, event1) -> false);
                setItemsClickable(recyclerView, true);
                mSwipeBack = false;
                if (mButtonsActions != null && mButtonInstance != null && mButtonInstance.contains(event.getX(), event.getY())) {
                    if (mButtonsState == ButtonsState.LEFT_VISIBLE) {
                        mButtonsActions.onLeftClicked(viewHolder.getAdapterPosition());
                    } else if (mButtonsState == ButtonsState.RIGHT_VISIBLE) {
                        mButtonsActions.onRightClicked(viewHolder.getAdapterPosition());
                    }
                }
                mButtonsState = ButtonsState.GONE;
                mCurrentItemViewHolder = null;
            }
            return false;
        });
    }

    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        int length = recyclerView.getChildCount();
        for (int i = 0; i < length; ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    public void onDraw(Canvas c) {
        if (mCurrentItemViewHolder != null) {
            drawButtons(c, mCurrentItemViewHolder);
        }
    }

    public SwipeControllerActions getButtonsActions() {
        return mButtonsActions;
    }

    public void setButtonsActions(SwipeControllerActions buttonsActions) {
        this.mButtonsActions = buttonsActions;
    }

    public void setFavorite(Boolean saved) {
        mIsFavorite = saved;
    }

}
