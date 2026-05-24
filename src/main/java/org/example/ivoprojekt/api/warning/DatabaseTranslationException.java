package org.example.ivoprojekt.api.warning;

public class DatabaseTranslationException {
    public static boolean foreignKeyException(Throwable cause) {
        return cause.getCause().getMessage().contains("foreign key constraint failed");
    }
}
