package br.com.duoli.pricecompare.tools;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import br.com.duoli.pricecompare.R;
import br.com.duoli.pricecompare.activity.MainActivity;
import br.com.duoli.pricecompare.adapter.ProductAdapter;

public class SwipeDelete extends ItemTouchHelper.SimpleCallback {

    private MainActivity mainActivity;
    private ProductAdapter mAdapter;
    private ConstraintLayout mTipsLayout;

    private Drawable background;
    private Drawable xMark;
    private int xMarkMargin;
    private boolean initiated;

    public SwipeDelete(MainActivity mainActivity, ProductAdapter adapter, ConstraintLayout tipsLayout){
        super(0, ItemTouchHelper.LEFT);

        this.mainActivity = mainActivity;
        this.mAdapter = adapter;
        this.mTipsLayout = tipsLayout;
    }

    private void init() {
        background = new ColorDrawable(Color.RED);
        xMark = ContextCompat.getDrawable(mainActivity, R.drawable.ic_clear_24dp);
        xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        xMarkMargin = (int) mainActivity.getResources().getDimension(R.dimen.ic_clear_margin);
        initiated = true;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.deleteProduct(viewHolder.getAdapterPosition());
        if (mAdapter.getItemCount() == 0){
            mTipsLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;

        // not sure why, but this method get's called for viewholder that are already swiped away
        if (viewHolder.getAdapterPosition() == -1) {
            // not interested in those
            return;
        }

        if (!initiated) {
            init();
        }

        // draw red background
        background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        background.draw(c);

        // draw x mark
        int itemHeight = itemView.getBottom() - itemView.getTop();
        int intrinsicWidth = xMark.getIntrinsicWidth();
        int intrinsicHeight = xMark.getIntrinsicWidth();

        int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
        int xMarkRight = itemView.getRight() - xMarkMargin;
        int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
        int xMarkBottom = xMarkTop + intrinsicHeight;
        xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

        xMark.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
