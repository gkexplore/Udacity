package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp(){
        cartController =  new CartController();
        TestUtils.injectMocks(cartController, "userRepository", userRepo);
        TestUtils.injectMocks(cartController, "cartRepository", cartRepo);
        TestUtils.injectMocks(cartController, "itemRepository", itemRepo);
    }

    @Test
    public void verify_user_can_add_an_item_to_cart() throws Exception{

        User u = new User();
        u.setId(1L);
        u.setUsername("test");
        u.setPassword("testPassword");
        u.setCart(new Cart());

        ModifyCartRequest m = new ModifyCartRequest();
        m.setItemId(1);
        m.setQuantity(1);
        m.setUsername("test");

        Item i = new Item();
        i.setId(1L);
        i.setDescription("test description");
        i.setName("test product name");
        i.setPrice(new BigDecimal(200));

        Cart c = new Cart();
        c.setUser(u);
        c.addItem(i);

        when(userRepo.findByUsername(u.getUsername())).thenReturn(c.getUser());
        when(itemRepo.findById(1L)).thenReturn(Optional.of(i));
        final ResponseEntity<Cart> response = cartController.addTocart(m);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertNotNull(cart);
        assertEquals("test description", cart.getItems().get(0).getDescription());
        assertEquals("test product name", cart.getItems().get(0).getName());
        assertEquals(new BigDecimal(200), cart.getTotal());

    }

    @Test
    public void verify_user_can_remove_item_from_cart() throws Exception{

        User u = new User();
        u.setId(1L);
        u.setUsername("test");
        u.setPassword("testPassword");
        u.setCart(new Cart());

        ModifyCartRequest m = new ModifyCartRequest();
        m.setItemId(1);
        m.setQuantity(1);
        m.setUsername("test");

        Item i = new Item();
        i.setId(1L);
        i.setDescription("test description");
        i.setName("test product name");
        i.setPrice(new BigDecimal(200));

        Cart c = new Cart();
        c.setUser(u);
        c.addItem(i);

        when(userRepo.findByUsername(u.getUsername())).thenReturn(c.getUser());
        when(itemRepo.findById(1L)).thenReturn(Optional.of(i));
        final ResponseEntity<Cart> response = cartController.removeFromcart(m);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertNotNull(cart);
        assertEquals(0, cart.getItems().size());
    }

}
