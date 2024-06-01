package bambi.kinematics.utils;

import java.util.Locale;

public final class ArrayUtil {
    public static String[] shiftarray(String[] array) {
        String[] newarray = new String[array.length - 1];
        for (int i = array.length - 1; i > 0; --i) {
            newarray[i - 1] = array[i];
        }
        return newarray;
    }

    public static String[] arraytolowercase(String[] array) {
        String[] newarray = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            newarray[i] = array[i].toLowerCase(Locale.ENGLISH);
        }
        return newarray;
    }

    private ArrayUtil() {}
}
