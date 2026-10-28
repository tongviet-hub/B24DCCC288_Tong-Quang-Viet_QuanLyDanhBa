package com.example.quanlydanhba.ui.main;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlydanhba.R;
import com.example.quanlydanhba.models.Contact;
import com.example.quanlydanhba.utils.AvatarColorHelper;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    public interface OnContactClickListener {
        void onContactClick(Contact contact, int position);
        void onFavoriteClick(Contact contact, int position);
    }

    private final Context context;
    private List<Contact> contactList;
    private final OnContactClickListener listener;

    public ContactAdapter(Context context, List<Contact> contactList, OnContactClickListener listener) {
        this.context = context;
        this.contactList = contactList;
        this.listener = listener;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.bind(contact, position);
    }

    @Override
    public int getItemCount() {
        return contactList != null ? contactList.size() : 0;
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private final FrameLayout layoutAvatar;
        private final TextView tvAvatarInitial;
        private final TextView tvContactName;
        private final TextView tvContactPhone;
        private final TextView tvGroupBadge;
        private final ImageView ivFavorite;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutAvatar = itemView.findViewById(R.id.layoutAvatar);
            tvAvatarInitial = itemView.findViewById(R.id.tvAvatarInitial);
            tvContactName = itemView.findViewById(R.id.tvContactName);
            tvContactPhone = itemView.findViewById(R.id.tvContactPhone);
            tvGroupBadge = itemView.findViewById(R.id.tvGroupBadge);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
        }

        public void bind(Contact contact, int position) {
            // Chữ cái đầu của tên
            tvAvatarInitial.setText(contact.getInitialLetter());

            // Màu nền avatar tròn động
            int avatarBgColor = AvatarColorHelper.getAvatarColor(context, contact.getName());
            GradientDrawable avatarDrawable = new GradientDrawable();
            avatarDrawable.setShape(GradientDrawable.OVAL);
            avatarDrawable.setColor(avatarBgColor);
            layoutAvatar.setBackground(avatarDrawable);

            // Tên và Số điện thoại
            tvContactName.setText(contact.getName());
            tvContactPhone.setText(contact.getPhone());

            // Nhóm và Màu sắc badge
            tvGroupBadge.setText(contact.getGroup());
            int groupBgColor = AvatarColorHelper.getGroupBgColor(context, contact.getGroup());
            int groupTextColor = AvatarColorHelper.getGroupTextColor(context, contact.getGroup());

            GradientDrawable badgeDrawable = new GradientDrawable();
            badgeDrawable.setShape(GradientDrawable.RECTANGLE);
            badgeDrawable.setCornerRadius(16f);
            badgeDrawable.setColor(groupBgColor);
            tvGroupBadge.setBackground(badgeDrawable);
            tvGroupBadge.setTextColor(groupTextColor);

            // Trạng thái yêu thích
            if (contact.isFavorite()) {
                ivFavorite.setImageResource(R.drawable.ic_star_filled);
            } else {
                ivFavorite.setImageResource(R.drawable.ic_star_border);
            }

            // Sự kiện nhấn vào dòng
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onContactClick(contact, getBindingAdapterPosition());
                }
            });

            // Sự kiện bấm vào dấu sao yêu thích
            ivFavorite.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onFavoriteClick(contact, getBindingAdapterPosition());
                }
            });
        }
    }
}
