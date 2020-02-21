package uk.ac.mmu.foodorderingapp.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uk.ac.mmu.foodorderingapp.Interface.ItemClickListener;
import uk.ac.mmu.foodorderingapp.R;

/**
 * admin side
 */

public class OrderViewHolderAdmin extends RecyclerView.ViewHolder {

    public TextView txtOrderId, txtOrderStatus, txtOrderPhone, txtOrderAddress, txtOrderDate;

    public Button btnEdit, btnRemove, btnDetail, btnDirection;


    //public ImageView btn_delete;

    public OrderViewHolderAdmin(@NonNull View itemView) {
        super(itemView);

        txtOrderAddress = (TextView) itemView.findViewById(R.id.order_Address);
        txtOrderId = (TextView) itemView.findViewById(R.id.order_id);
        txtOrderStatus = (TextView) itemView.findViewById(R.id.order_status);
        txtOrderPhone = (TextView) itemView.findViewById(R.id.order_phone);
        txtOrderDate = (TextView) itemView.findViewById(R.id.order_date);
        //btn_delete = (ImageView)itemView.findViewById(R.id.btn_delete);

        btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
        btnRemove = (Button) itemView.findViewById(R.id.btnRemove);
        btnDetail = (Button) itemView.findViewById(R.id.btnDetail);
        btnDirection = (Button) itemView.findViewById(R.id.btnDirection);


    }

}
