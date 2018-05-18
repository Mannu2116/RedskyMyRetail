package myRetail.Controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import myRetail.Exception.DatabaseServerException;
import myRetail.Exception.ResourceMisMatchException;
import myRetail.Exception.ResourceNotFoundException;
import myRetail.Model.Product;
import myRetail.Repository.ProductRepository;
import myRetail.Service.MyRetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
         * Author: Manju Bhargavi
         * Date : 05/11/2018
 * */

@Api(value="MyRetail", description = "Retrieves & Modifies Product details")
@RestController
public class MyRetailController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private MyRetailService service;


    /**
     * Endpoint to respond to HTTP GET request at /products/{id}
     * Gets Product details from Redsky Service
     * Gets Price details of product from MySQL
     * @param productId - to get details about the product based on product ID
     * @return
     */
    @ApiOperation(
            value = "Get product details",
            notes = "Get product and price information by product id"
    )
    @ApiResponses(
            value = {@ApiResponse(code = 200, message = "Product details retrieved successfully"),
                    @ApiResponse(code = 404, message = "Product Id not found in the data store"),
                    @ApiResponse(code = 500, message = "Unable retrieve data from database")})

    @RequestMapping(path="/products/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getProductById(@PathVariable("id") String productId) throws IOException {

        Product product;
        try{

            product = service.getRedSkyData(productId);
            product = service.setCurrentPriceForProduct(product);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No product found with productId : "+ productId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to retrieve product information for product: "+productId);
        }
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }
    /**
     * Endpoint to respond to HTTP PUT request at /products/{id}
     * Update Product details in MySQL based on productId
     * @param productId - Id of product that need to be updated.
     * @return
             */

    @ApiOperation(value = "Updates pricing details")
    @ApiResponses(
            value = {@ApiResponse(code = 201, message = "Product details updated"),
                    @ApiResponse(code = 400, message = "Product details cannot be updated, request body json should have matching id with path variable"),
                    @ApiResponse(code = 500, message = "Unable retrieve data from database")})
    @RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity updateProductDetails(@RequestBody Product product, @PathVariable ("id") String productId) {

        Product updatedProduct;
        try {
            updatedProduct = service.putProductDetails(product, productId);
        } catch (ResourceMisMatchException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ProductId's doesn't Match");
        } catch (DatabaseServerException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to retrieve product information for product: "+productId);
        }
        return new ResponseEntity<Product>(updatedProduct, HttpStatus.OK);
    }

    /**
     * Endpoint to respond to HTTP POST request at /products/createNewProduct
     * Creates new product and stores it in MySQL
     * @param product
     * @return
     */

    @ApiOperation(value = "Creates new Product ")
    @ApiResponses(
            value = {@ApiResponse(code = 201, message = "Product Created"),
                     @ApiResponse(code = 500, message = "Unable to save product")
            })
    @PostMapping(value ="/products/createNewProduct")
    public ResponseEntity createProduct(@Valid @RequestBody Product product){
        Product newProduct;
        try {
            newProduct = service.saveProduct(product);
        } catch (DatabaseServerException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to save the product. Please try again later");
        }
        return new ResponseEntity<Product>(newProduct, HttpStatus.CREATED);
    }
}