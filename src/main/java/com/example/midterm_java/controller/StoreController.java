package com.example.midterm_java.controller;

import com.example.midterm_java.model.CartItem;
import com.example.midterm_java.model.Product;
import com.example.midterm_java.model.SaleRecord;
import com.example.midterm_java.repository.ProductRepository;
import com.example.midterm_java.repository.SaleRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;

@Controller
//@RequiredArgsConstructor
public class StoreController {

    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;
    private final com.example.midterm_java.repository.CategoryRepository categoryRepository;

    public StoreController(
            ProductRepository productRepository,
            SaleRepository saleRepository,
            com.example.midterm_java.repository.CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.saleRepository = saleRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/store")
    public String store(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer categoryId,
            Model model,
            HttpSession session
    ) {
        model.addAttribute("products", getActiveProducts(search, categoryId));
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("search", search);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("cartCount", getCartCount(session));
        return "store/store";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Integer productId, HttpSession session, RedirectAttributes redirectAttributes) {
        Map<Integer, Integer> cart = getCart(session);
        cart.put(productId, cart.getOrDefault(productId, 0) + 1);
        session.setAttribute("cart", cart);

        redirectAttributes.addFlashAttribute("toastMessage", "Item added to cart!");
        redirectAttributes.addFlashAttribute("toastType", "success");
        return "redirect:/store";
    }

    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        Map<Integer, Integer> cart = getCart(session);
        List<CartItem> cartItems = new ArrayList<>();
        double total = 0.0;

        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            Optional<Product> pOpt = productRepository.findById(entry.getKey());
            if (pOpt.isPresent()) {
                CartItem item = new CartItem(pOpt.get(), entry.getValue());
                cartItems.add(item);
                total += item.getSubtotal();
            }
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "store/shoppingcart";
    }

    @PostMapping("/cart/update")
    public String updateCart(
            @RequestParam Integer productId,
            @RequestParam String action,
            HttpSession session
    ) {
        Map<Integer, Integer> cart = getCart(session);
        if (cart.containsKey(productId)) {
            int currentQty = cart.get(productId);
            if ("increase".equalsIgnoreCase(action)) {
                cart.put(productId, currentQty + 1);
            } else if ("decrease".equalsIgnoreCase(action)) {
                if (currentQty > 1) {
                    cart.put(productId, currentQty - 1);
                } else {
                    cart.remove(productId);
                }
            } else if ("remove".equalsIgnoreCase(action)) {
                cart.remove(productId);
            }
            session.setAttribute("cart", cart);
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/buy")
    public String buyCart(HttpSession session, RedirectAttributes redirectAttributes) {
        Map<Integer, Integer> cart = getCart(session);

        if (!cart.isEmpty()) {
            LocalDate today = LocalDate.now();
            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                Integer productId = entry.getKey();
                Integer purchasedQty = entry.getValue();

                Optional<Product> pOpt = productRepository.findById(productId);
                if (pOpt.isPresent()) {
                    Product product = pOpt.get();
                    // Deduct quantity from product database record
                    int currentStock = product.getQuantityNum();
                    int newStock = Math.max(0, currentStock - purchasedQty);
                    product.setQty(String.valueOf(newStock));
                    productRepository.save(product);

                    // Record sale
                    saleRepository.save(new SaleRecord(product, purchasedQty, today));
                }
            }
        }

        session.removeAttribute("cart");
        redirectAttributes.addFlashAttribute("toastMessage", "Purchase completed successfully!");
        redirectAttributes.addFlashAttribute("toastType", "success");
        return "redirect:/store";
    }

    // -------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------

    private List<Product> getActiveProducts(String search, Integer categoryId) {
        LocalDate today = LocalDate.now();
        List<Product> activeProducts = new ArrayList<>();
        for (Product prod : productRepository.findAll()) {
            String dateStr = prod.getExpireDate();
            if (dateStr != null && !dateStr.trim().isEmpty()) {
                try {
                    LocalDate expDate = LocalDate.parse(dateStr);
                    if (expDate.isBefore(today)) {
                        continue;
                    }
                } catch (Exception ignored) {
                }
            }

            // Search filter by product name
            if (search != null && !search.trim().isEmpty()) {
                String searchLower = search.trim().toLowerCase();
                if (prod.getPName() == null || !prod.getPName().toLowerCase().contains(searchLower)) {
                    continue;
                }
            }

            // Category filter
            if (categoryId != null) {
                if (prod.getProductCategory() == null || !categoryId.equals(prod.getProductCategory().getCatId())) {
                    continue;
                }
            }

            activeProducts.add(prod);
        }
        return activeProducts;
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, Integer> getCart(HttpSession session) {
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        return cart != null ? cart : new HashMap<>();
    }

    private int getCartCount(HttpSession session) {
        return getCart(session).values().stream().mapToInt(Integer::intValue).sum();
    }
}
