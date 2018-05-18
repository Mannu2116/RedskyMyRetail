package myRetail.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Author: Manju Bhargavi
 * Date : 05/11/2018
 * */


@ResponseStatus(value = HttpStatus.CONFLICT, reason = "ProductId already Exists")
public class ResourceConflictException extends RuntimeException{
}
