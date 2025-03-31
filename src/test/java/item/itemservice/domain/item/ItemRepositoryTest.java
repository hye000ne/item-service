package item.itemservice.domain.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRepositoryTest {
    ItemRepository itemRepository = new ItemRepository();

    @AfterEach
    void afterEach() {
        itemRepository.clearStore();
    }

    @Test
    void save() {
        //given
        Item item = new Item("itemA", 1000, 1);

        //when
        Item savedItem = itemRepository.save(item);

        //then
        Item findItem = itemRepository.findById(savedItem.getId());
        assertThat(savedItem).isEqualTo(findItem);
    }

    @Test
    void findAll() {
        //given
        Item itemA = new Item("itemA", 1000, 1);
        Item itemB = new Item("itemB", 2000, 3);

        itemRepository.save(itemA);
        itemRepository.save(itemB);

        //when
        List<Item> items = itemRepository.findAll();

        //then
        assertThat(items.size()).isEqualTo(2);
        assertThat(items).contains(itemA, itemB);
    }
    
    @Test
    void update() {
        //given
        Item savedItem = itemRepository.save(new Item("itemA", 1000, 1));
        Item updateParam = new Item("itemB", 2000, 3);

        //when
        itemRepository.update(savedItem.getId(), updateParam);
        Item findItem = itemRepository.findById(savedItem.getId());

        //then
        assertThat(findItem.getItemName()).isEqualTo(updateParam.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(updateParam.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(updateParam.getQuantity());
    }
}