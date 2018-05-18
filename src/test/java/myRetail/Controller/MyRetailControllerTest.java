package myRetail.Controller;


import myRetail.Exception.ResourceMisMatchException;
import myRetail.Exception.ResourceNotFoundException;
import myRetail.Model.Product;
import myRetail.Service.MyRetailService;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Author: Manju Bhargavi
 * Date : 05/11/2018
 * */


public class MyRetailControllerTest {

    RestTemplate restTemplate = new RestTemplate();

    private MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Mock
    private MyRetailService myRetailService;

    @InjectMocks
    private MyRetailController myRetailController;

    private String productID = "13860428";

    Product product ;

    @Before
    public void adddProductDetails(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(myRetailController).build();
        product = new Product("13860428","The Big Lebowski (Blu-ray)","25","USD");

    }

    /**
     * This is an example of integration test, which requires server to be running
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Ignore
    public void givenProductExists()
            throws ClientProtocolException, IOException {

        HttpUriRequest request = new HttpGet( "http://localhost:8080/products/" + "13860428" );

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

        assertThat(
                httpResponse.getStatusLine().getStatusCode(),equalTo(HttpStatus.SC_OK));
    }

    @Test
    public void retrieveProductDetailsTest() throws Exception{

        product = new Product("13860428","The Big Lebowski (Blu-ray)","25","USD");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/products/"+productID).accept(
                MediaType.APPLICATION_JSON);

        when(myRetailService.getRedSkyData(productID)).thenReturn(product);
        when(myRetailService.setCurrentPriceForProduct(product)).thenReturn(product);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"productId\":\"13860428\",\"prodName\":\"The Big Lebowski (Blu-ray)\",\"currentPrice\":\"25\",\"currencyCode\":\"USD\"}";
        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
    }

    @Test
    public void retrieveProductDetailsInvalidRequestTest() throws Exception {
        String prodId="566";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/products/"+prodId).accept(
                MediaType.APPLICATION_JSON);

        when(myRetailService.getRedSkyData(prodId)).thenReturn(product);
        when(myRetailService.setCurrentPriceForProduct(product)).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void updateProductDetailsTest() throws Exception{

        //Mockito.when(myRetailController.updateProductDetails(product,productID)).thenReturn(");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(
                "/products/"+productID)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"productId\":"+productID+",\"prodName\":\"The Big Lebowski (Blu-ray)\",\"currentPrice\":\"300\",\"currencyCode\":\"USD\"}")
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.SC_OK,result.getResponse().getStatus());
    }
}