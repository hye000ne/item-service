package item.itemservice.web.item.form;

import item.itemservice.domain.item.Item;
import item.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
public class FormItemController {
    private final ItemRepository itemRepository;

    @ModelAttribute("regions")
    private Map<String, String> regions() {
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");

        return regions;
    }

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);

        return "form/items";
    }

    @GetMapping("/{itemId}")
    public String item(Model model, @PathVariable Long itemId){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "form/item";
    }

    @GetMapping("/add")
    public String addForm(Model model){
        model.addAttribute("item", new Item());

        return "form/addForm";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Item item, RedirectAttributes redirectAttributes){
        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());

        Item saveditem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", saveditem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/form/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "form/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@ModelAttribute Item updateParam, @PathVariable Long itemId){
        itemRepository.update(itemId, updateParam);

        return "redirect:/form/items/{itemId}";
    }
}
