package usermanagement.exception;

import usermanagement.dto.BaseErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@Slf4j
@RestControllerAdvice
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity<BaseErrorDto> handleBadRequestException(BadRequestException ex) {
        BaseErrorDto badRequestDto = BaseErrorDto.builder()
                .timestamp(new Date())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestDto);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<BaseErrorDto> handleNotFoundException(NotFoundException ex) {
        BaseErrorDto error = BaseErrorDto.builder()
                .timestamp(new Date())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<BaseErrorDto> handleUnprocessableEntity(UnprocessableEntityException ex) {
        BaseErrorDto errorResponse = BaseErrorDto.builder()
                .timestamp(new Date())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

//    @ExceptionHandler(UnauthorizedException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public ResponseEntity<BaseErrorDto> handleUnauthorizedException(UnauthorizedException ex) {
//        BaseErrorDto error = BaseErrorDto.builder()
//                .timestamp(new Date())
//                .message(ex.getMessage())
//                .build();
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
//    }

}
