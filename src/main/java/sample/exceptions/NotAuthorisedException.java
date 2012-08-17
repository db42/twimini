package sample.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created with IntelliJ IDEA.
 * User: dushyant
 * Date: 17/8/12
 * Time: 10:59 AM
 * To change this template use File | Settings | File Templates.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class NotAuthorisedException extends RuntimeException{

}
