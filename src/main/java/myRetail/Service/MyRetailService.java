package myRetail.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import myRetail.Exception.DatabaseServerException;
import myRetail.Exception.ResourceMisMatchException;
import myRetail.Exception.ResourceNotFoundException;
import myRetail.Model.Product;
import myRetail.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.management.ServiceNotFoundException;
import java.util.Arrays;
import java.util.Map;

/**
         * Author: Manju Bhargavi
         * Date : 05/11/2018
 * */
@Service
public class MyRetailService {

    @Autowired
    private ProductRepository productRepo;

    /**
     * Retrieves product information from Redsky
     * @param productId
     * @return
     */
    public Product getRedSkyData(String productId) {

        final String URL = "https://redsky.target.com/v2/pdp/tcin/" + productId + "?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        Product product = new Product();
        ObjectMapper objMapper = new ObjectMapper();

        Map<String, Map> productDetails;
        Map<String, Map> productInfo;
        Map<String, Map> itemInfo;
        Map<String, String> prodDescr;


        try{
            ResponseEntity<String> result = restTemplate.exchange(URL, HttpMethod.GET, entity, String.class);
            productDetails = objMapper.readValue(result.getBody(), Map.class);

            product.productId = productId;
            productInfo = productDetails.get("product");
            itemInfo = productInfo.get("item");
            prodDescr = itemInfo.get(("product_description"));
            product.prodName = prodDescr.get("title");

        }

        catch (Exception e) {
            throw new ResourceNotFoundException();
        }

        return product;
    }

    /**
     * Retrieves product price from database and updates product information
     * @param product
     * @return
     * @throws DatabaseServerException
     */
    public Product setCurrentPriceForProduct(Product product) throws DatabaseServerException {
        Product productInfoFromRepo;
        try {
            productInfoFromRepo = productRepo.findByProductId(product.productId);
        } catch (Exception e) {
            throw new ResourceNotFoundException();
        }
        product.currentPrice = productInfoFromRepo.currentPrice;
        product.currencyCode = productInfoFromRepo.currencyCode;

        return product;
    }

    /**
     * Validates that productId in passed Product object and parameter are same.
     * It updated product name only if the current name is null
     * It only updates product price
     * @param product
     * @param productId
     * @return
     * @throws ResourceMisMatchException
     * @throws DatabaseServerException
     */
    public Product putProductDetails(Product product, String productId) throws ResourceMisMatchException, DatabaseServerException
    {
        String originalProductId = product.getProductId();
        if (originalProductId.equalsIgnoreCase(productId) == false) {
            throw new ResourceMisMatchException();
        }

        Product currentProduct = productRepo.findByProductId(productId);
        if (currentProduct.getProdName() != null) {
            product.setProdName(currentProduct.getProdName());
        }
        try {
            product = productRepo.save(product);
        } catch (Exception e) {
            throw new DatabaseServerException();
        }
        return product;
    }

    public Product saveProduct(Product product) throws DatabaseServerException{
        Product newProduct;
        try {
            newProduct = productRepo.save(product);
        } catch (Exception e) {
            throw new DatabaseServerException();
        }
        return newProduct;
    }

}