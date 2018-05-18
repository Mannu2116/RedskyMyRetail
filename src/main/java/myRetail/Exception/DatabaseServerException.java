package myRetail.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by manju on 5/18/2018.
 */

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Product details not updated.")
public class DatabaseServerException  extends RuntimeException{


}
