package se.vegas.tasknz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import se.vegas.tasknz.dto.WalletExceptionResponse;

import java.util.Date;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Date: 20.01.2025
 *
 * @author Nikolay Zinin
 */

//todo change repeated pattern to something
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<WalletExceptionResponse> walletNotFoundException(WalletNotFoundException ex, WebRequest request) {
        WalletExceptionResponse response = WalletExceptionResponse
                .builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(new Date())
                .path(request.getDescription(false))
                .descriptionMessage(ex.getExceptionMessage())
                .build();
        return new ResponseEntity<>(response, NOT_FOUND);
    }

    @ExceptionHandler(NotEnoughCreditException.class)
    public ResponseEntity<WalletExceptionResponse> notEnoughCreditException(NotEnoughCreditException ex, WebRequest request) {
        WalletExceptionResponse response = WalletExceptionResponse
                .builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(new Date())
                .path(request.getDescription(false))
                .descriptionMessage(ex.getExceptionMessage())
                .build();
        return new ResponseEntity<>(response, NOT_FOUND);
    }

    @ExceptionHandler(TransactionIdRedundantException.class)
    public ResponseEntity<WalletExceptionResponse> transactionIdRedundantException(TransactionIdRedundantException ex, WebRequest request) {
        WalletExceptionResponse response = WalletExceptionResponse
                .builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(new Date())
                .path(request.getDescription(false))
                .descriptionMessage(ex.getExceptionMessage())
                .build();
        return new ResponseEntity<>(response, NOT_FOUND);
    }

    @ExceptionHandler(WalletIdRedundantException.class)
    public ResponseEntity<WalletExceptionResponse> walletIdRedundantException(WalletIdRedundantException ex, WebRequest request) {
        WalletExceptionResponse response = WalletExceptionResponse
                .builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(new Date())
                .path(request.getDescription(false))
                .descriptionMessage(ex.getExceptionMessage())
                .build();
        return new ResponseEntity<>(response, NOT_FOUND);
    }
}
