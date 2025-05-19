package space.thinhtran.warehouse.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

public class MessagesUtil {
    private static final ResourceBundle messageBundle = ResourceBundle.getBundle("message/messages",
            Locale.getDefault());

    private MessagesUtil() {
    }

    public static String getMessage(String errorCode, Object... args) {
        String message;
        try {
            message = messageBundle.getString(errorCode);
        } catch (MissingResourceException ex) {
            message = errorCode;
        }

        FormattingTuple format = MessageFormatter.arrayFormat(message, args);

        return format.getMessage();
    }
}
