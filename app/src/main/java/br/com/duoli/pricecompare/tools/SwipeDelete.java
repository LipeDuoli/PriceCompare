package br.com.duoli.pricecompare.tools;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import br.com.duoli.pricecompare.adapter.ProductAdapter;

public class SwipeDelete extends ItemTouchHelper.SimpleCallback {

    private ProductAdapter mAdapter;
    private ConstraintLayout mTipsLayout;

    /**
     * Creates a Callback for the given drag and swipe allowance. These values serve as
     * defaults
     * and if you want to customize behavior per ViewHolder, you can override
     * {@link #getSwipeDirs(RecyclerView, ViewHolder)}
     * and / or {@link #getDragDirs(RecyclerView, ViewHolder)}.
     *  @param dragDirs  Binary OR of direction flags in which the Views can be dragged. Must be
     *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
     *                  #END},
     *                  {@link #UP} and {@link #DOWN}.
     * @param swipeDirs Binary OR of direction flags in which the Views can be swiped. Must be
     *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
     *                  #END},
     *                  {@link #UP} and {@link #DOWN}.
     * @param mTipsLayout
     */

    public SwipeDelete(ProductAdapter adapter, ConstraintLayout tipsLayout){
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);

        this.mAdapter = adapter;
        this.mTipsLayout = tipsLayout;
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
}
