package se.vegas.tasknz.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Date: 20.01.2025
 *
 * @author Nikolay Zinin
 */
@Getter
@AllArgsConstructor
public class WalletException extends Exception {
    private final String exceptionMessage;
}
