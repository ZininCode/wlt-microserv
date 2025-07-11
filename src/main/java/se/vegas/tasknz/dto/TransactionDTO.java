package se.vegas.tasknz.dto;

import lombok.*;

import java.math.BigDecimal;

/**
 * Date: 20.01.2025
 *
 * @author Nikolay Zinin
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private String transactionId;
    private String walletId;
    private BigDecimal amount;
}
