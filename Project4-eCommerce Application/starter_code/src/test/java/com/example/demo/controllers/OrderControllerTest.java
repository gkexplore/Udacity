package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepo = mock(UserRepository.class);

    private OrderRepository orderRepo = mock(OrderRepository.class);

    @Before
    public void setUp() throws Exception{
        orderController = new OrderController();
        TestUtils.injectMocks(orderController, "userRepository", userRepo);
        TestUtils.injectMocks(orderController, "orderRepository", orderRepo);
    }

    @Test
    public void verify_user_is_able_to_place_an_order() throws Exception{
        Item item = new Item();
        item.setPrice(new BigDecimal(200));
        item.setName("test product");
        item.setDescription("test description");
        item.setId(1L);

        Cart c = new Cart();
        c.setItems(Arrays.asList(item));
        c.setId(1L);
        c.setTotal(new BigDecimal(200));

        User u = new User();
        u.setUsername("test");
        u.setPassword("testPassword");
        u.setCart(c);

        UserOrder userOrder = new UserOrder();
        userOrder.setTotal(new BigDecimal(200));
        userOrder.setId(1L);
        userOrder.setUser(u);

        when(userRepo.findByUsername(u.getUsername())).thenReturn(u);

        final ResponseEntity<UserOrder> response = orderController.submit(userOrder.getUser().getUsername());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder uo = response.getBody();

        assertNotNull(uo);
        assertEquals("test product", uo.getItems().get(0).getName());
        assertEquals("test description", uo.getItems().get(0).getDescription());
    }

    @Test
    public void verify_user_is_able_to_view_order_history() throws Exception{
        Item item = new Item();
        item.setPrice(new BigDecimal(200));
        item.setName("test product");
        item.setDescription("test description");
        item.setId(1L);

        Cart c = new Cart();
        c.setItems(Arrays.asList(item));
        c.setId(1L);
        c.setTotal(new BigDecimal(200));

        User u = new User();
        u.setUsername("test");
        u.setPassword("testPassword");
        u.setCart(c);

        UserOrder userOrder = new UserOrder();
        userOrder.setTotal(new BigDecimal(200));
        userOrder.setId(1L);
        userOrder.setUser(u);
        userOrder.setItems(Arrays.asList(item));

        when(userRepo.findByUsername(u.getUsername())).thenReturn(u);
        when(orderRepo.findByUser(u)).thenReturn(Arrays.asList(userOrder));

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(u.getUsername());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> uol = response.getBody();

        assertNotNull(uol);
        System.out.println(uol.size());

        assertEquals("test product", uol.get(0).getItems().get(0).getName());
        assertEquals("test description", uol.get(0).getItems().get(0).getDescription());
    }
}
