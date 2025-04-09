package item.itemservice.web.item.form;

import item.itemservice.domain.item.DeliveryCode;
import item.itemservice.domain.item.Item;
import item.itemservice.domain.item.ItemRepository;
import item.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Slf4j // 로그를 사용할 수 있게 해주는 Lombok 어노테이션 (log.info 등 사용 가능)
@Controller // 해당 클래스가 Spring MVC 컨트롤러임을 나타냄
@RequestMapping("/items") // 공통 URL 경로 설정
@RequiredArgsConstructor // 생성자 주입을 Lombok이 자동 생성해줌
public class ItemController {

    private final ItemRepository itemRepository; // 생성자 주입
    private final MessageSource messageSource;

    /**
     * 모든 요청에서 "regions"라는 이름으로 등록 지역 맵을 모델에 추가함
     */
    @ModelAttribute("regions")
    public Map<String, String> regions(Locale locale) {
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", messageSource.getMessage("region.seoul", null, locale));
        regions.put("BUSAN", messageSource.getMessage("region.busan", null, locale));
        regions.put("JEJU", messageSource.getMessage("region.jeju", null, locale));

        return regions;
    }

    /**
     * Enum 타입의 아이템 분류값 배열을 모델에 추가
     */
    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        return ItemType.values(); // ENUM values()로 전체 리스트 반환
    }

    /**
     * 배송 옵션 리스트를 모델에 추가
     */
    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes(Locale locale) {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", messageSource.getMessage("deliveryCode.fast", null, locale)));
        deliveryCodes.add(new DeliveryCode("NORMAL", messageSource.getMessage("deliveryCode.normal", null, locale)));
        deliveryCodes.add(new DeliveryCode("SLOW", messageSource.getMessage("deliveryCode.slow", null, locale)));

        return deliveryCodes;
    }

    /**
     * 상품 목록 화면
     */
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/items";
    }

    /**
     * 상품 상세 화면
     */
    @GetMapping("/{itemId}")
    public String item(Model model, @PathVariable Long itemId) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/item";
    }

    /**
     * 상품 등록 폼
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item()); // 빈 폼 객체
        return "form/addForm";
    }

    /**
     * 상품 등록 처리
     */
    @PostMapping("/add")
    public String add(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // 필드 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }
        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999까지 허용합니다."));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("globalError", "가격 * 수량의 합은 10,000원 이상이여야합니다. 현재 값은 " + resultPrice + "입니다"));
            }
        }

        // 검증 실패 로직
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "form/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/items/{itemId}";
    }

    /**
     * 상품 수정 폼
     */
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/editForm";
    }

    /**
     * 상품 수정 처리
     */
    @PostMapping("/{itemId}/edit")
    public String edit(@ModelAttribute Item updateParam, @PathVariable Long itemId) {
        itemRepository.update(itemId, updateParam);
        return "redirect:/items/{itemId}";
    }
}