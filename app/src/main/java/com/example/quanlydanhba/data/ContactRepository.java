package com.example.quanlydanhba.data;

import com.example.quanlydanhba.models.Contact;
import java.util.ArrayList;

public class ContactRepository {
    private static ContactRepository instance;
    private final ArrayList<Contact> contactList;

    private ContactRepository() {
        contactList = new ArrayList<>();
        initSampleData();
    }

    public static synchronized ContactRepository getInstance() {
        if (instance == null) {
            instance = new ContactRepository();
        }
        return instance;
    }

    /**
     * Khởi tạo dữ liệu mẫu theo yêu cầu đề bài
     */
    private void initSampleData() {
        contactList.add(new Contact(
                "Phạm Quốc Tuấn",
                "0982123456",
                "Bạn bè",
                "Nam",
                false,
                "Bạn thân thời cấp 3."
        ));

        contactList.add(new Contact(
                "Hoàng Thùy Dương",
                "0915789654",
                "Gia đình",
                "Nữ",
                true,
                "Chị gái ruột."
        ));

        contactList.add(new Contact(
                "Đỗ Minh Khang",
                "0973456789",
                "Công việc",
                "Nam",
                false,
                "Đồng nghiệp cùng phòng ban."
        ));
    }

    public ArrayList<Contact> getContacts() {
        return contactList;
    }

    public void addContact(Contact contact) {
        if (contact != null) {
            contactList.add(0, contact); // Đưa lên đầu danh sách
        }
    }

    public Contact getContactById(String id) {
        if (id == null) return null;
        for (Contact c : contactList) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public boolean deleteContact(String id) {
        if (id == null) return false;
        for (int i = 0; i < contactList.size(); i++) {
            if (contactList.get(i).getId().equals(id)) {
                contactList.remove(i);
                return true;
            }
        }
        return false;
    }

    public void updateFavorite(String id, boolean isFavorite) {
        Contact c = getContactById(id);
        if (c != null) {
            c.setFavorite(isFavorite);
        }
    }

    /**
     * Kiểm tra xem số điện thoại đã tồn tại chưa
     */
    public boolean isPhoneExists(String phone, String excludeId) {
        if (phone == null) return false;
        String cleanPhone = phone.trim();
        for (Contact c : contactList) {
            if (excludeId != null && c.getId().equals(excludeId)) {
                continue;
            }
            if (c.getPhone() != null && c.getPhone().trim().equals(cleanPhone)) {
                return true;
            }
        }
        return false;
    }

    public int getTotalCount() {
        return contactList.size();
    }

    public int getFavoriteCount() {
        int count = 0;
        for (Contact c : contactList) {
            if (c.isFavorite()) {
                count++;
            }
        }
        return count;
    }
}
