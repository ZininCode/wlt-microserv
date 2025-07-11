package se.vegas.tasknz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Date: 20.01.2025
 *
 * @author Nikolay Zinin
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class BalanceDto {
    private String walletId;
    private BigDecimal balance;
}
