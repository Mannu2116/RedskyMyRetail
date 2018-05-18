package myRetail.Repository;

import myRetail.Model.Product;
import org.springframework.data.repository.CrudRepository;

/**
        * Author: Manju Bhargavi
        * Date : 05/11/2018
        * */

public interface ProductRepository extends CrudRepository<Product, Long> {
    public Product findByProductId(String productId);
}
