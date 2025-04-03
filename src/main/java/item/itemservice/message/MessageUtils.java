package item.itemservice.message;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageUtils {
    private static MessageSource messageSource;

    public MessageUtils(MessageSource source) {
        MessageUtils.messageSource = source;
    }

    public static String get(String key){
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    public static String get(String key, Object[] args){
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }
}
