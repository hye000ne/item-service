package item.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {
    private static final Map<Long, Item> store = new HashMap<>();
    private static long sequence = 0L;

    public Item save(Item item){
        item.setId(++sequence);
        store.put(item.getId(), item);

        return item;
    }

    public Item findById(Long id){
        return store.get(id);
    }

    public List<Item> findAll(){
        return new ArrayList<>(store.values());
    }

    public Long update(Long id, Item updateParam) {
        Item savedItem = store.get(id);
        savedItem.setItemName(updateParam.getItemName());
        savedItem.setPrice(updateParam.getPrice());
        savedItem.setQuantity(updateParam.getQuantity());

        return savedItem.getId();
    }

    public void clearStore(){
        store.clear();
    }
}
