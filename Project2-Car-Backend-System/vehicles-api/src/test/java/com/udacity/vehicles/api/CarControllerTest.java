package com.udacity.vehicles.api;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.Price;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.service.CarService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Implements testing of the CarController class.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Import({CarResourceAssembler.class})
public class CarControllerTest {


    private MockMvc mvc;

    @Autowired
    private JacksonTester<Car> json;

    @InjectMocks
    private CarService carService;

    @MockBean(name="carRepository")
    private CarRepository carRepository;

    @MockBean
    private PriceClient priceClient;

    @MockBean
    private MapsClient mapsClient;

    @Autowired
    private WebApplicationContext webApplicationContext;


    /**
     * Creates pre-requisites for testing, such as an example car.
     */
    @Before
    public void setup() {
        this.mvc= MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
        Car car = getCar();
        car.setId(1L);
        given(carRepository.save(any())).willReturn(car);
        given(carRepository.findById(any())).willReturn(java.util.Optional.of(car));
        given(carRepository.findAll()).willReturn(Collections.singletonList(car));
    }

    /**
     * Tests for successful creation of new car in the system
     * @throws Exception when car creation fails in the system
     */
    @Test
    public void createCar() throws Exception {
        Car car = getCar();
        when(carRepository.findById(any())).thenReturn(null);
        when(carRepository.save(car)).thenReturn(car);
        mvc.perform(
                post(new URI("/cars"))
                        .content(json.write(car).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }


    /**
     * Tests for successfule updation of an existing car in the system
     * @throws Exception when car updattion fails in the system
     */
    @Test
    public void updateCar() throws Exception{
        Car car = getCar();
        car.setId(1L);
        car.setCondition(Condition.NEW);

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carRepository.save(car)).thenReturn(car);
        mvc.perform(
                put(new URI("/cars/1"))
                .content(json.write(car).getJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.condition").value("NEW"))
                .andExpect(jsonPath("$.details.body").value("sedan"));
    }


    /**
     * Tests if the read operation appropriately returns a list of vehicles.
     * @throws Exception if the read operation of the vehicle list fails
     */
    @Test
    public void listCars() throws Exception {
        Car carOne = getCar();
        Car carTwo = getCar();

        carOne.setId(1L);

        carTwo.setId(2L);
        carTwo.setCondition(Condition.NEW);
        Details details = carTwo.getDetails();
        details.setBody("SUV");
        carTwo.setDetails(details);

        List<Car> carList = Arrays.asList(carOne, carTwo);
        when(carRepository.findAll()).thenReturn(carList);
        mvc.perform(get("/cars").header("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.*.carList[0].id").value(1))
                .andExpect(jsonPath("$.*.carList[0].condition").value("USED"))
                .andExpect(jsonPath("$.*.carList[0].details.body").value("sedan"))
                .andExpect(jsonPath("$.*.carList[1].id").value(2))
                .andExpect(jsonPath("$.*.carList[1].condition").value("NEW"))
                .andExpect(jsonPath("$.*.carList[1].details.body").value("SUV"));
        verify(carRepository, times(1)).findAll();
        verifyNoMoreInteractions(carRepository);
    }

    /**
     * Tests the read operation for a single car by ID.
     * @throws Exception if the read operation for a single car fails
     */
    @Test
    public void findCar() throws Exception {
        Car carOne = getCar();

        carOne.setId(1L);
        carOne.setCondition(Condition.USED);
        Price price = new Price();
        price.setCurrency("USD");
        price.setPrice(new BigDecimal(12343));
        price.setVehicleId(carOne.getId());



        carOne.getLocation().setZip("84123");
        carOne.getLocation().setState("UT");
        carOne.getLocation().setCity("SLC");
        carOne.getLocation().setAddress("480 W");


        when(priceClient.getPrice(carOne.getId())).thenReturn(price.getPrice()+"");
        when(mapsClient.getAddress(carOne.getLocation())).thenReturn(carOne.getLocation());

        when(carRepository.findById(carOne.getId())).thenReturn(java.util.Optional.of(carOne));
        mvc.perform(get("/cars/1").header("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.condition").value("USED"))
                .andExpect(jsonPath("$.details.body").value("sedan"))
                .andExpect(jsonPath("$.location.zip").value("84123"))
                .andExpect(jsonPath("$.location.address").value("480 W"))
                .andExpect(jsonPath("$.price").value("12343"));
        verify(carRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(carRepository);

    }

    /**
     * Tests the deletion of a single car by ID.
     * @throws Exception if the delete operation of a vehicle fails
     */
    @Test
    public void deleteCar() throws Exception {
        Car carOne = getCar();
        carOne.setId(1L);

        when(carRepository.findById(carOne.getId())).thenReturn(java.util.Optional.of(carOne));
        doNothing().when(carRepository).delete(carOne);

        mvc.perform(
                delete("/cars/{id}", carOne.getId()))
                .andExpect(status().isOk());

        verify(carRepository, times(1)).delete(carOne);
        verify(carRepository, times(1)).findById(1L);
    }

    /**
     * Creates an example Car object for use in testing.
     * @return an example Car object
     */
    private Car getCar() {
        Car car = new Car();
        car.setLocation(new Location(40.730610, -73.935242));
        Details details = new Details();
        Manufacturer manufacturer = new Manufacturer(101, "Chevrolet");
        details.setManufacturer(manufacturer);
        details.setModel("Impala");
        details.setMileage(32280);
        details.setExternalColor("white");
        details.setBody("sedan");
        details.setEngine("3.6L V6");
        details.setFuelType("Gasoline");
        details.setModelYear(2018);
        details.setProductionYear(2018);
        details.setNumberOfDoors(4);
        car.setDetails(details);
        car.setCondition(Condition.USED);
        return car;
    }
}