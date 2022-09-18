package fcode.backend.management.config;

import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Response<String> handleUnwantedException(Exception e) {
        logger.error("{}: {}",e.getMessage(),e.getClass());
        return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unknown error");
    }

    @ExceptionHandler(ServiceException.class)
    public Response<String> handleCustomException(ServiceException e) {
        logger.error("{}: {}", e.getCode(), e.getMessage());
        return new Response<>(e.getCode().value(), e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("{}: {}", HttpStatus.BAD_REQUEST.value(), "Wrong input field type from request");
        return new Response<>(HttpStatus.BAD_REQUEST.value(), "Wrong input field type");
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public Response<String> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException e) {
        logger.error("{}: {}", HttpStatus.BAD_REQUEST.value(), "Missing input field from request");
        return new Response<>(HttpStatus.BAD_REQUEST.value(), "Missing input field");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Response<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        logger.error("{}: {}", HttpStatus.BAD_REQUEST.value(), "Missing request parameter from request");
        return new Response<>(HttpStatus.BAD_REQUEST.value(), "Missing request parameter");
    }
}
