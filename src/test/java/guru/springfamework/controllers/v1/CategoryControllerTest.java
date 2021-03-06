package guru.springfamework.controllers.v1;

import guru.springfamework.api.v1.model.CategoryDTO;
import guru.springfamework.services.CategoryService;
import guru.springfamework.customExceptions.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class CategoryControllerTest {
    @Mock
    CategoryService categoryService;

    @InjectMocks
    CategoryController categoryController;

    private static final Long ID = 1L;
    private static final String NAME = "Test Category";

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                //have to set this so all controller will know about ControllerAdvice
                .setControllerAdvice(RestResponseEntityExceptionHandler.class)
                .build();
    }

    @Test
    public void getAllCategories() throws Exception {
        //arrange
        List<CategoryDTO> categoryDTOList = Arrays.asList(new CategoryDTO(), new CategoryDTO());

        when(categoryService.getAllCategories()). thenReturn(categoryDTOList);

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders.get(CategoryController.getBaseUrl()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //using jsonPath to examine the JSON object.
                //$ indicates the root... so saying does categories have a size of 2?
                .andExpect(jsonPath("$.categories", hasSize(2)));
    }

    @Test
    public void getCategoryByName() throws Exception {
        //arrange
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(ID);
        categoryDTO.setName(NAME);

        when(categoryService.getCategoryByName(anyString())).thenReturn(categoryDTO);

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders.get(CategoryController.getBaseUrl() + "/" + NAME).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //using jsonPath to examine the JSON object.
                //$ indicates the root... so saying is "name" property on JSON object equal to NAME constant?
                .andExpect(jsonPath("$.name", equalTo(NAME)));
    }

    @Test
    public void testBadUrlReturnsNotFoundException() throws Exception {
        //remember to setControllerAdvice in setup

        when(categoryService.getCategoryByName(anyString())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get(CategoryController.getBaseUrl() + "/foo")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}