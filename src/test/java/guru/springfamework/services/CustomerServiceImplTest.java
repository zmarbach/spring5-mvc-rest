package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;

    private static final Long ID = 1L;

    private CustomerServiceImpl customerService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        customerService = new CustomerServiceImpl(customerRepository, CustomerMapper.INSTANCE);
    }

    @Test
    public void getAllCustomers() {
        //arrange
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer());
        customers.add(new Customer());
        when(customerRepository.findAll()).thenReturn(customers);

        //act
        List<CustomerDTO> customerDTOS = customerService.getAllCustomers();

        //assert
        assertEquals(2, customerDTOS.size());
    }

    @Test
    public void getCustomerById() {
        Customer customer = new Customer();
        customer.setId(ID);
        customer.setFirstName("Zach");
        when(customerRepository.findById(anyLong())).thenReturn(java.util.Optional.of(customer));

        //act
        CustomerDTO customerDTO = customerService.getCustomerById(ID);

        //assert
        assertEquals("Zach", customerDTO.getFirstName());

    }

    @Test
    public void postNewCustomer(){
        //arrange
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("Jimmy");

        Customer savedCustomer = new Customer();
        savedCustomer.setFirstName(customerDTO.getFirstName());

        when(customerRepository.save(any())).thenReturn(savedCustomer);

        //act
        CustomerDTO savedCustomerDTO = customerService.createNewCustomer(new CustomerDTO());

        //assert
        assertEquals("Jimmy", savedCustomerDTO.getFirstName());

    }

    @Test
    public void deleteCustomer(){
        //arrange

        //act
        customerService.deleteCustomerById(1L);

        //assert
        verify(customerRepository, times(1)).deleteById(anyLong());
    }
}