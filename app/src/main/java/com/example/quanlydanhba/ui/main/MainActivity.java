package com.example.quanlydanhba.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlydanhba.R;
import com.example.quanlydanhba.data.ContactRepository;
import com.example.quanlydanhba.models.Contact;
import com.example.quanlydanhba.ui.add.AddContactActivity;
import com.example.quanlydanhba.ui.detail.ContactDetailActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class MainActivity extends AppCompatActivity implements ContactAdapter.OnContactClickListener {

    private TextView tvTotalCount;
    private TextView tvFavoriteCount;
    private RecyclerView rvContacts;
    private LinearLayout layoutEmptyState;
    private ExtendedFloatingActionButton fabAddContact;

    private ContactAdapter adapter;
    private ContactRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = ContactRepository.getInstance();

        initViews();
        setupRecyclerView();
        setupListeners();
    }

    private void initViews() {
        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvFavoriteCount = findViewById(R.id.tvFavoriteCount);
        rvContacts = findViewById(R.id.rvContacts);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        fabAddContact = findViewById(R.id.fabAddContact);
    }

    private void setupRecyclerView() {
        adapter = new ContactAdapter(this, repository.getContacts(), this);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        rvContacts.setAdapter(adapter);
    }

    private void setupListeners() {
        // Nút Thêm liên hệ: mở AddContactActivity
        fabAddContact.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    /**
     * Cập nhật danh sách và khối thống kê
     */
    private void refreshData() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        int total = repository.getTotalCount();
        int favorites = repository.getFavoriteCount();

        tvTotalCount.setText(String.valueOf(total));
        tvFavoriteCount.setText(String.valueOf(favorites));

        // Ẩn/hiện Empty State
        if (total == 0) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            rvContacts.setVisibility(View.GONE);
        } else {
            layoutEmptyState.setVisibility(View.GONE);
            rvContacts.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onContactClick(Contact contact, int position) {
        // Mở ContactDetailActivity, truyền contact ID qua Intent
        Intent intent = new Intent(MainActivity.this, ContactDetailActivity.class);
        intent.putExtra(ContactDetailActivity.EXTRA_CONTACT_ID, contact.getId());
        startActivity(intent);
    }

    @Override
    public void onFavoriteClick(Contact contact, int position) {
        // Đảo ngược trạng thái yêu thích
        boolean newStatus = !contact.isFavorite();
        contact.setFavorite(newStatus);
        repository.updateFavorite(contact.getId(), newStatus);

        // Cập nhật lại view của item và khối thống kê
        adapter.notifyItemChanged(position);
        tvFavoriteCount.setText(String.valueOf(repository.getFavoriteCount()));
    }
}
