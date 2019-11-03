package com.codevsolution.base.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.codevsolution.base.notifications.fcm.data.PushNotification;
import com.codevsolution.freemarketsapp.R;

import java.util.ArrayList;

/**
 * Adaptador de notificaciones
 */
public class PushNotificationsAdapter
        extends RecyclerView.Adapter<PushNotificationsAdapter.ViewHolder> {

    ArrayList<PushNotification> pushNotifications = new ArrayList<>();

    public PushNotificationsAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_list_notification, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PushNotification newNotification = pushNotifications.get(position);

        holder.title.setText(newNotification.getTitle());
        holder.description.setText(newNotification.getDescription());
        holder.expiryDate.setText(String.format("Válido hasta el %s", newNotification.getExpiryDate()));
        holder.discount.setText(String.format("%d%%", (int) (newNotification.getDiscount() * 100)));
    }

    @Override
    public int getItemCount() {
        return pushNotifications.size();
    }

    public void replaceData(ArrayList<PushNotification> items) {
        setList(items);
        notifyDataSetChanged();
    }

    public void setList(ArrayList<PushNotification> list) {
        this.pushNotifications = list;
    }

    public void addItem(PushNotification pushMessage) {
        pushNotifications.add(0, pushMessage);
        notifyItemInserted(0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public TextView expiryDate;
        public TextView discount;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            description = itemView.findViewById(R.id.tv_description);
            expiryDate = itemView.findViewById(R.id.tv_expiry_date);
            discount = itemView.findViewById(R.id.tv_discount);
        }
    }
}
