package com.example.quanlydanhba.ui.add;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlydanhba.R;
import com.example.quanlydanhba.data.ContactRepository;
import com.example.quanlydanhba.models.Contact;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddContactActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextInputLayout tilName;
    private TextInputLayout tilPhone;
    private TextInputLayout tilNotes;
    private TextInputEditText etName;
    private TextInputEditText etPhone;
    private TextInputEditText etNotes;
    private Spinner spGroup;
    private RadioGroup rgGender;
    private CheckBox cbFavorite;
    private Button btnSave;

    private ContactRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        repository = ContactRepository.getInstance();

        initViews();
        setupSpinner();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tilName = findViewById(R.id.tilName);
        tilPhone = findViewById(R.id.tilPhone);
        tilNotes = findViewById(R.id.tilNotes);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etNotes = findViewById(R.id.etNotes);
        spGroup = findViewById(R.id.spGroup);
        rgGender = findViewById(R.id.rgGender);
        cbFavorite = findViewById(R.id.cbFavorite);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupSpinner() {
        // Cài đặt adapter cho Spinner nhóm
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.contact_groups,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGroup.setAdapter(adapter);
    }

    private void setupListeners() {
        // Nút Back trên top bar
        btnBack.setOnClickListener(v -> finish());

        // Nút Lưu liên hệ
        btnSave.setOnClickListener(v -> handleSaveContact());
    }

    private void handleSaveContact() {
        tilName.setError(null);
        tilPhone.setError(null);

        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String phone = etPhone.getText() != null ? etPhone.getText().toString().trim() : "";
        String notes = etNotes.getText() != null ? etNotes.getText().toString().trim() : "";
        String group = spGroup.getSelectedItem() != null ? spGroup.getSelectedItem().toString() : "Khác";

        // Lấy giới tính được chọn
        String gender = "Nam";
        int checkedGenderId = rgGender.getCheckedRadioButtonId();
        if (checkedGenderId == R.id.rbFemale) {
            gender = "Nữ";
        } else if (checkedGenderId == R.id.rbOther) {
            gender = "Khác";
        }

        boolean isFavorite = cbFavorite.isChecked();

        // 1. Kiểm tra họ và tên: không được để trống
        if (TextUtils.isEmpty(name)) {
            tilName.setError(getString(R.string.error_empty_name));
            etName.requestFocus();
            return;
        }

        // 2. Kiểm tra số điện thoại: 10 chữ số, bắt đầu bằng 0
        // Biểu thức chính quy: ^0[0-9]{9}$
        if (TextUtils.isEmpty(phone) || !phone.matches("^0[0-9]{9}$")) {
            tilPhone.setError(getString(R.string.error_invalid_phone));
            etPhone.requestFocus();
            return;
        }

        // 3. Kiểm tra số điện thoại đã tồn tại trong danh bạ chưa
        if (repository.isPhoneExists(phone, null)) {
            tilPhone.setError(getString(R.string.error_duplicate_phone));
            etPhone.requestFocus();
            return;
        }

        // Tạo đối tượng Contact mới
        Contact newContact = new Contact(name, phone, group, gender, isFavorite, notes);

        // Lưu vào Repository
        repository.addContact(newContact);

        Toast.makeText(this, R.string.success_add_contact, Toast.LENGTH_SHORT).show();

        // Đóng Activity quay về màn hình danh sách
        finish();
    }
}
