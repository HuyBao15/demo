package com.laptrinhJava.demo.Controller;

import com.laptrinhJava.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public String showCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("totalPrice", cartService.getTotalPrice());
        return "/cart/cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity) {
        cartService.addToCart(productId, quantity);
        return "redirect:/cart";
    }

    /*@PostMapping("/update/{productId}")
    public String updateCartItem(@PathVariable Long productId, @RequestParam int quantity) {
        if (quantity < 1) {
            cartService.removeFromCart(productId);
        } else {
            cartService.updateCartItem(productId, quantity);
        }
        return "redirect:/cart";
    }*/

    @PostMapping("/update/{productId}")
    public String updateCartItem(@PathVariable Long productId, @RequestParam int quantity) {
        cartService.updateCartItem(productId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }
}
