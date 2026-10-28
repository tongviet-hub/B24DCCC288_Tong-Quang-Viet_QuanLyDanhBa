package com.example.quanlydanhba.utils;

import android.content.Context;
import androidx.core.content.ContextCompat;
import com.example.quanlydanhba.R;

public class AvatarColorHelper {

    private static final int[] AVATAR_COLORS = new int[]{
            R.color.avatar_1,
            R.color.avatar_2,
            R.color.avatar_3,
            R.color.avatar_4,
            R.color.avatar_5,
            R.color.avatar_6
    };

    /**
     * Trả về màu nền cho avatar dựa theo hash của tên liên hệ
     */
    public static int getAvatarColor(Context context, String name) {
        if (name == null || name.isEmpty()) {
            return ContextCompat.getColor(context, R.color.primary);
        }
        int hash = Math.abs(name.hashCode());
        int colorRes = AVATAR_COLORS[hash % AVATAR_COLORS.length];
        return ContextCompat.getColor(context, colorRes);
    }

    /**
     * Trả về màu nền cho nhãn nhóm (Group Badge)
     */
    public static int getGroupBgColor(Context context, String group) {
        if (group == null) {
            return ContextCompat.getColor(context, R.color.group_other_bg);
        }
        switch (group.trim()) {
            case "Gia đình":
                return ContextCompat.getColor(context, R.color.group_family_bg);
            case "Bạn bè":
                return ContextCompat.getColor(context, R.color.group_friend_bg);
            case "Công việc":
                return ContextCompat.getColor(context, R.color.group_work_bg);
            default:
                return ContextCompat.getColor(context, R.color.group_other_bg);
        }
    }

    /**
     * Trả về màu chữ cho nhãn nhóm (Group Badge)
     */
    public static int getGroupTextColor(Context context, String group) {
        if (group == null) {
            return ContextCompat.getColor(context, R.color.group_other_text);
        }
        switch (group.trim()) {
            case "Gia đình":
                return ContextCompat.getColor(context, R.color.group_family_text);
            case "Bạn bè":
                return ContextCompat.getColor(context, R.color.group_friend_text);
            case "Công việc":
                return ContextCompat.getColor(context, R.color.group_work_text);
            default:
                return ContextCompat.getColor(context, R.color.group_other_text);
        }
    }
}
