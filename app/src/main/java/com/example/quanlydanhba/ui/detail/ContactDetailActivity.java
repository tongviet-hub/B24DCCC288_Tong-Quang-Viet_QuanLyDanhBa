package com.example.quanlydanhba.ui.detail;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlydanhba.R;
import com.example.quanlydanhba.data.ContactRepository;
import com.example.quanlydanhba.models.Contact;
import com.example.quanlydanhba.utils.AvatarColorHelper;

public class ContactDetailActivity extends AppCompatActivity {

    public static final String EXTRA_CONTACT_ID = "extra_contact_id";

    private ImageButton btnBackTop;
    private FrameLayout layoutAvatarDetail;
    private TextView tvAvatarDetailInitial;
    private TextView tvDetailName;
    private TextView tvDetailGroupBadge;
    private TextView tvDetailPhone;
    private TextView tvDetailGroupAndGender;
    private TextView tvDetailNotes;
    private CheckBox cbDetailFavorite;
    private Button btnCall;
    private Button btnBackBottom;
    private Button btnDelete;

    private ContactRepository repository;
    private Contact currentContact;
    private String contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        repository = ContactRepository.getInstance();

        // Lấy contact ID từ Intent
        contactId = getIntent().getStringExtra(EXTRA_CONTACT_ID);
        currentContact = repository.getContactById(contactId);

        if (currentContact == null) {
            Toast.makeText(this, "Không tìm thấy thông tin liên hệ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        displayContactData();
        setupListeners();
    }

    private void initViews() {
        btnBackTop = findViewById(R.id.btnBackTop);
        layoutAvatarDetail = findViewById(R.id.layoutAvatarDetail);
        tvAvatarDetailInitial = findViewById(R.id.tvAvatarDetailInitial);
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailGroupBadge = findViewById(R.id.tvDetailGroupBadge);
        tvDetailPhone = findViewById(R.id.tvDetailPhone);
        tvDetailGroupAndGender = findViewById(R.id.tvDetailGroupAndGender);
        tvDetailNotes = findViewById(R.id.tvDetailNotes);
        cbDetailFavorite = findViewById(R.id.cbDetailFavorite);
        btnCall = findViewById(R.id.btnCall);
        btnBackBottom = findViewById(R.id.btnBackBottom);
        btnDelete = findViewById(R.id.btnDelete);
    }

    private void displayContactData() {
        // 1. Chữ cái đầu của tên & avatar tròn lớn
        tvAvatarDetailInitial.setText(currentContact.getInitialLetter());

        int avatarColor = AvatarColorHelper.getAvatarColor(this, currentContact.getName());
        GradientDrawable avatarDrawable = new GradientDrawable();
        avatarDrawable.setShape(GradientDrawable.OVAL);
        avatarDrawable.setColor(avatarColor);
        layoutAvatarDetail.setBackground(avatarDrawable);

        // 2. Họ và tên
        tvDetailName.setText(currentContact.getName());

        // 3. Nhóm (Badge)
        tvDetailGroupBadge.setText(currentContact.getGroup());
        int groupBgColor = AvatarColorHelper.getGroupBgColor(this, currentContact.getGroup());
        int groupTextColor = AvatarColorHelper.getGroupTextColor(this, currentContact.getGroup());

        GradientDrawable badgeDrawable = new GradientDrawable();
        badgeDrawable.setShape(GradientDrawable.RECTANGLE);
        badgeDrawable.setCornerRadius(20f);
        badgeDrawable.setColor(groupBgColor);
        tvDetailGroupBadge.setBackground(badgeDrawable);
        tvDetailGroupBadge.setTextColor(groupTextColor);

        // 4. Số điện thoại
        tvDetailPhone.setText(currentContact.getPhone());

        // 5. Nhóm & Giới tính
        String groupAndGenderText = "Nhóm: " + currentContact.getGroup() + "  •  Giới tính: " + currentContact.getGender();
        tvDetailGroupAndGender.setText(groupAndGenderText);

        // 6. Ghi chú
        if (TextUtils.isEmpty(currentContact.getNotes())) {
            tvDetailNotes.setText(R.string.no_notes);
            tvDetailNotes.setTextColor(getColor(R.color.text_muted));
        } else {
            tvDetailNotes.setText(currentContact.getNotes());
            tvDetailNotes.setTextColor(getColor(R.color.text_primary));
        }

        // 7. Yêu thích CheckBox
        cbDetailFavorite.setChecked(currentContact.isFavorite());
    }

    private void setupListeners() {
        // Nút Back trên Top Bar
        btnBackTop.setOnClickListener(v -> finish());

        // Nút Quay lại ở dưới
        btnBackBottom.setOnClickListener(v -> finish());

        // Cập nhật trạng thái Yêu thích khi CheckBox thay đổi
        cbDetailFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (currentContact != null) {
                currentContact.setFavorite(isChecked);
                repository.updateFavorite(currentContact.getId(), isChecked);
            }
        });

        // Nút Gọi điện: Intent ngầm định (ACTION_DIAL)
        btnCall.setOnClickListener(v -> handleCallContact());

        // Nút Xóa liên hệ: xác nhận và xóa khỏi Repository
        btnDelete.setOnClickListener(v -> confirmDeleteContact());
    }

    /**
     * Dùng Intent ngầm định (ACTION_DIAL) để mở ứng dụng gọi điện hệ thống
     */
    private void handleCallContact() {
        if (currentContact != null && !TextUtils.isEmpty(currentContact.getPhone())) {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + currentContact.getPhone()));
            startActivity(dialIntent);
        } else {
            Toast.makeText(this, "Không có số điện thoại hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Hiển thị hộp thoại xác nhận trước khi xóa
     */
    private void confirmDeleteContact() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete_title)
                .setMessage(R.string.dialog_delete_message)
                .setPositiveButton(R.string.dialog_btn_confirm_delete, (dialog, which) -> {
                    // Xóa liên hệ khỏi repository
                    repository.deleteContact(currentContact.getId());
                    Toast.makeText(ContactDetailActivity.this, R.string.msg_contact_deleted, Toast.LENGTH_SHORT).show();
                    // Trở về danh sách
                    finish();
                })
                .setNegativeButton(R.string.dialog_btn_cancel, null)
                .show();
    }
}
