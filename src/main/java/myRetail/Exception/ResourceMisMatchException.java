package myRetail.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Author: Manju Bhargavi
 * Date : 05/11/2018
 * */

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Product price cannot be updated, request body json should have matching id with path variable ...ProductId in request header and body doesn't match.")
public class ResourceMisMatchException extends RuntimeException{
}
