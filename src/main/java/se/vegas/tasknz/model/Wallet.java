package se.vegas.tasknz.model;

import lombok.*;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

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
@Table(name ="wallet")
public class Wallet {
    @Id
    @Column (name = "wallet_id", nullable = false)
    private String walletId;
    @Column
    private BigDecimal balance;
    @OneToMany(mappedBy = "wallet", targetEntity = WalletTransaction.class)
    private List<WalletTransaction> walletTransactions;
}
