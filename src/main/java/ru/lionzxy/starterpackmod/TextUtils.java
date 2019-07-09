package ru.lionzxy.starterpackmod;

import javax.annotation.Nullable;

public class TextUtils {
    public static boolean isEmpty(@Nullable String str) {
        return str == null || str.length() == 0;
    }
}