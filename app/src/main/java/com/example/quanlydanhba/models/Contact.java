package com.example.quanlydanhba.models;

import java.io.Serializable;
import java.util.UUID;

public class Contact implements Serializable {
    private String id;
    private String name;
    private String phone;
    private String group;
    private String gender;
    private boolean isFavorite;
    private String notes;

    public Contact(String name, String phone, String group, String gender, boolean isFavorite, String notes) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.phone = phone;
        this.group = group;
        this.gender = gender;
        this.isFavorite = isFavorite;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Lấy chữ cái đầu của tên (ưu tiên chữ cái đầu của từ cuối cùng trong tên)
     * Ví dụ: "Nguyễn Văn An" -> "A"
     */
    public String getInitialLetter() {
        if (name == null || name.trim().isEmpty()) {
            return "?";
        }
        String trimmed = name.trim();
        String[] parts = trimmed.split("\\s+");
        String lastWord = parts[parts.length - 1];
        if (!lastWord.isEmpty()) {
            return lastWord.substring(0, 1).toUpperCase();
        }
        return "?";
    }
}
