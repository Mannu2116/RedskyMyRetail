package myRetail;

import myRetail.Model.Product;
import myRetail.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Created by manju on 5/11/2018.
 */

@SpringBootApplication
@EnableSwagger2
public class MyRetailApp {

    @Autowired
    private ProductRepository productRepository;


    public static void main(String[] args)
    {
        SpringApplication.run(MyRetailApp.class,args);
    }

    @PostConstruct
    public void populateData() {

        Product product = new Product();
        product.setProductId("13860428");
        product.setCurrencyCode("USD");
        product.setCurrentPrice("58");
        productRepository.save(product);    }

    @Bean
    public Docket myRetailApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(regex("/products.*"))
                .build()
                .pathMapping("/")
                .tags(new Tag("Title","MyRetai"),
                        new Tag("Description","API to store and retrieve data from MyRetail"),
                        new Tag("Version","1"),
                        new Tag("TermsofServiceURL","http://terms-of-service.url"),
                        new Tag("ContactName","Manju"))

                .useDefaultResponseMessages(false);
    }
}


