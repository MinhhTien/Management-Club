package fcode.backend.management.config;


import fcode.backend.management.model.response.Response;
import sevice.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Response<String> handleUnwantedException(Exception e) {
        logger.error(e.getMessage());
        return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unknown error");
    }

    @ExceptionHandler(ServiceException.class)
    public Response<String> handleCustomException(ServiceException e) {
        logger.error("{}: {}", e.getCode(), e.getMessage());
        return new Response<>(e.getCode().value(), e.getMessage());
    }
}
