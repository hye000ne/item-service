package item.itemservice.web.item.form;

import item.itemservice.domain.item.DeliveryCode;
import item.itemservice.domain.item.Item;
import item.itemservice.domain.item.ItemRepository;
import item.itemservice.domain.item.ItemType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j // 로그를 사용할 수 있게 해주는 Lombok 어노테이션 (log.info 등 사용 가능)
@Controller // 해당 클래스가 Spring MVC 컨트롤러임을 나타냄
@RequestMapping("/form/items") // 공통 URL 경로 설정
@RequiredArgsConstructor // 생성자 주입을 Lombok이 자동 생성해줌
public class FormItemController {

    private final ItemRepository itemRepository; // 생성자 주입

    /**
     * 모든 요청에서 "regions"라는 이름으로 등록 지역 맵을 모델에 추가함
     */
    @ModelAttribute("regions")
    public Map<String, String> regions() {
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");

        return regions;
    }

    /**
     * Enum 타입의 아이템 분류값 배열을 모델에 추가
     */
    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes(){
        return ItemType.values(); // ENUM values()로 전체 리스트 반환
    }

    /**
     * 배송 옵션 리스트를 모델에 추가
     */
    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes(){
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST","빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL","일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW","느린 배송"));
        return deliveryCodes;
    }

    /**
     * 상품 목록 화면
     */
    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/items";
    }

    /**
     * 상품 상세 화면
     */
    @GetMapping("/{itemId}")
    public String item(Model model, @PathVariable Long itemId){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/item";
    }

    /**
     * 상품 등록 폼
     */
    @GetMapping("/add")
    public String addForm(Model model){
        model.addAttribute("item", new Item()); // 빈 폼 객체
        return "form/addForm";
    }

    /**
     * 상품 등록 처리
     */
    @PostMapping("/add")
    public String add(@ModelAttribute Item item, RedirectAttributes redirectAttributes){
        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());
        log.info("item.itemTypes={}", item.getItemType());
        log.info("item.deliveryCode={}", item.getDeliveryCode());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/form/items/{itemId}";
    }

    /**
     * 상품 수정 폼
     */
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/editForm";
    }

    /**
     * 상품 수정 처리
     */
    @PostMapping("/{itemId}/edit")
    public String edit(@ModelAttribute Item updateParam, @PathVariable Long itemId){
        itemRepository.update(itemId, updateParam);
        return "redirect:/form/items/{itemId}";
    }
}