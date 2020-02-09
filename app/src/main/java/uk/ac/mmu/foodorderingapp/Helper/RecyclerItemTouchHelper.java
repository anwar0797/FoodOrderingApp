package uk.ac.mmu.foodorderingapp.Helper;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import uk.ac.mmu.foodorderingapp.Interface.RecyclerItemTouchHelperListener;
import uk.ac.mmu.foodorderingapp.ViewHolder.CartViewHolder;
import uk.ac.mmu.foodorderingapp.ViewHolder.FavouritesViewHolder;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if(listener != null)
            listener.onSwiped(viewHolder,direction,viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void clearView(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof CartViewHolder) {
            View foregroundView = ((CartViewHolder) viewHolder).view_foreground;
            getDefaultUIUtil().clearView(foregroundView);
        }
        else if (viewHolder instanceof FavouritesViewHolder)
        {
            View foregroundView = ((FavouritesViewHolder) viewHolder).view_foreground;
            getDefaultUIUtil().clearView(foregroundView);
        }
    }

    @Override
    public void onChildDraw( Canvas c, RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (viewHolder instanceof CartViewHolder) {
            View foregroundView = ((CartViewHolder) viewHolder).view_foreground;
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
        }
        else if (viewHolder instanceof FavouritesViewHolder) {
            View foregroundView = ((FavouritesViewHolder) viewHolder).view_foreground;
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder != null)
        {
            if (viewHolder instanceof CartViewHolder)
            {
                View foregroundView = ((CartViewHolder)viewHolder).view_foreground;
                getDefaultUIUtil().onSelected(foregroundView);
            }
            else if (viewHolder instanceof FavouritesViewHolder)
            {
                View foregroundView = ((FavouritesViewHolder)viewHolder).view_foreground;
                getDefaultUIUtil().onSelected(foregroundView);
            }
        }
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (viewHolder instanceof CartViewHolder)
        {
            View foregroundView = ((CartViewHolder)viewHolder).view_foreground;
            getDefaultUIUtil().onDrawOver(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive);
        }
        else if (viewHolder instanceof FavouritesViewHolder)
        {
            View foregroundView = ((FavouritesViewHolder)viewHolder).view_foreground;
            getDefaultUIUtil().onDrawOver(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive);
        }
    }
}
