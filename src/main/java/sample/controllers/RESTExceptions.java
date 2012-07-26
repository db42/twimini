package sample.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created with IntelliJ IDEA.
 * User: dushyant
 * Date: 25/7/12
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
final class ResourceNotFoundException extends RuntimeException {
}

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
final class NotAuthorisedException extends RuntimeException{

}
