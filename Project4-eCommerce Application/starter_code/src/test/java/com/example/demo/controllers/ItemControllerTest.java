package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() throws Exception{
        itemController = new ItemController();
        TestUtils.injectMocks(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void verify_get_lists_work_fine() throws Exception{
        Item i = new Item();
        i.setId(1L);
        i.setDescription("test product description");
        i.setName("test product");
        i.setPrice(new BigDecimal(200));

        Item i2 = new Item();
        i2.setId(1L);
        i2.setDescription("test product description");
        i2.setName("test product");
        i2.setPrice(new BigDecimal(200));

        List<Item> itemList = Arrays.asList(i, i2);

        when(itemRepo.findAll()).thenReturn(itemList);

        final ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();

        assertNotNull(items);
        assertEquals("test product", items.get(0).getName());
        assertEquals("test product description", items.get(0).getDescription());
        assertEquals(new BigDecimal(200), items.get(0).getPrice());

    }

    @Test
    public void verify_get_item_by_id_works_fine() throws Exception{
        Item i = new Item();
        i.setId(1L);
        i.setDescription("test product description");
        i.setName("test product");
        i.setPrice(new BigDecimal(200));

        when(itemRepo.findById(i.getId())).thenReturn(Optional.of(i));

        final ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item item = response.getBody();

        assertNotNull(item);
        assertEquals("test product", item.getName());
        assertEquals("test product description", item.getDescription());
        assertEquals(new BigDecimal(200), item.getPrice());
    }

    @Test
    public void verify_get_item_by_item_name_works_fine() throws Exception{
        Item i = new Item();
        i.setId(1L);
        i.setDescription("test product description");
        i.setName("test product");
        i.setPrice(new BigDecimal(200));

        Item i2 = new Item();
        i2.setId(1L);
        i2.setDescription("test product description");
        i2.setName("test product");
        i2.setPrice(new BigDecimal(200));

        List<Item> itemList = Arrays.asList(i, i2);

        when(itemRepo.findByName("test product")).thenReturn(itemList);

        final ResponseEntity<List<Item>> response = itemController.getItemsByName(i.getName());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();

        assertNotNull(items);
        assertEquals("test product", items.get(0).getName());
        assertEquals("test product description", items.get(0).getDescription());
        assertEquals(new BigDecimal(200), items.get(0).getPrice());

    }
}
