package se.vegas.tasknz.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import se.vegas.tasknz.model.TransactionType;
import se.vegas.tasknz.service.impl.FinancialOperationImplCredit;
import se.vegas.tasknz.service.impl.FinancialOperationImplDebit;

@Component
@AllArgsConstructor
public class FinancialOperationFactory {
    private final FinancialOperationImplDebit operationDebit;
    private final FinancialOperationImplCredit operationCredit;

    public FinancialOperation getOperation(TransactionType operationType) {
        switch (operationType) {
            case TransactionType.DEBIT:
                return operationDebit;
            case TransactionType.CREDIT:
                return operationCredit;
            default:
                throw new IllegalArgumentException("Unknown operation type: " + operationType);
        }
    }
}