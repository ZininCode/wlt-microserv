package se.vegas.tasknz.model;

import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Date: 10.01.2025
 *
 * @author Nikolay Zinin
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="wallet_transaction")
public class WalletTransaction {
    @Id
    @Column(nullable = false)
    private String id ;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType transactionType;
    @Column
    private BigDecimal amount;
    @ManyToOne(targetEntity = Wallet.class)
    @JoinColumn(name = "wallet_id", referencedColumnName = "wallet_id", nullable = false)
    private Wallet wallet;
    @Column(name = "time")
    private Instant transactionTime;
}

