package item.itemservice.domain.item;

import item.itemservice.message.MessageUtils;

public enum ItemType {
    BOOK("itemType.BOOK"), FOOD("itemType.FOOD"), ETC("itemType.ETC");

    private final String messageKey;

    ItemType(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String getMessage(String messageKey){
        return MessageUtils.get(messageKey);
    }
}
