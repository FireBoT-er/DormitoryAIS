package ru.cw.dormitoryais;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Обеспечивает проверку форматов файлов.
 */
public class FormatCheck {
    /**
     * Проверяет, является ли файл изображением по соответствию заголовочных байтов.
     */
    public static boolean isImage(byte[] bytes)
    {
        byte[] png = new byte[] { (byte) 137, (byte) 80, (byte) 78, (byte) 71 };
        byte[] jpeg = new byte[] { (byte) 255, (byte) 216, (byte) 255, (byte) 224 };
        byte[] jpeg2 = new byte[] { (byte) 255, (byte) 216, (byte) 255, (byte) 225 };

        byte[] toCheck = new byte[] { Array.getByte(bytes, 0), Array.getByte(bytes, 1), Array.getByte(bytes, 2), Array.getByte(bytes, 3) };

        if (Arrays.equals(png, toCheck))
        {
            return true;
        }

        if (Arrays.equals(jpeg, toCheck))
        {
            return true;
        }

        if (Arrays.equals(jpeg2, toCheck))
        {
            return true;
        }

        return false;
    }
}
