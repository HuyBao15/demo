package com.laptrinhJava.demo.service;

import com.laptrinhJava.demo.Model.CartItem;
import com.laptrinhJava.demo.Model.Order;
import com.laptrinhJava.demo.Model.OrderDetail;
import com.laptrinhJava.demo.repository.OrderDetailRepository;
import com.laptrinhJava.demo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CartService cartService;

    @Transactional
    public Order createOrder(String customerName, String address, String phoneNumber, String notes, List<CartItem> cartItems) {
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setAddress(address);
        order.setPhoneNumber(phoneNumber);
        order.setNotes(notes);
        order = orderRepository.save(order);

        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            orderDetailRepository.save(detail);
        }

        cartService.clearCart();
        return order;
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
}
