package myRetail.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Author: Manju Bhargavi
 * Date : 05/11/2018
 * */

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Product not found")
public class ResourceNotFoundException extends RuntimeException{

}
