package myRetail.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Author: Manju Bhargavi
 * Date : 05/11/2018
 * */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Redsky Target Server is down.")
public class RedskyTargetServerDownException extends RuntimeException {

}
