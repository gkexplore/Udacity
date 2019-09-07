package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController =  new UserController();
        TestUtils.injectMocks(userController, "userRepository", userRepo);
        TestUtils.injectMocks(userController, "cartRepository", cartRepo);
        TestUtils.injectMocks(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception{
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();

        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void find_by_user_name_returns_correct_user() throws Exception{
        User u = new User();
        u.setUsername("Karthik");
        u.setPassword("testPassword");
        u.setCart(new Cart());

        when(userRepo.findByUsername("Karthik")).thenReturn(u);

        final ResponseEntity<User> response = userController.findByUserName(u.getUsername());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals("Karthik", user.getUsername());
        assertEquals("testPassword", user.getPassword());
        assertTrue(user.getCart().equals(user.getCart()));

    }

    @Test
    public void find_by_id_returns_correct_user() throws Exception{
        User u = new User();
        u.setId(1L);
        u.setUsername("Karthik");
        u.setPassword("testPassword");
        u.setCart(new Cart());

        when(userRepo.findById(u.getId())).thenReturn(Optional.of(u));

        final ResponseEntity<User> response = userController.findById(u.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("Karthik", user.getUsername());
        assertEquals("testPassword", user.getPassword());
        assertTrue(user.getCart().equals(user.getCart()));

    }
}
