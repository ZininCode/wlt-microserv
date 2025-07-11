
package se.vegas.tasknz.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import se.vegas.tasknz.dto.TransactionDTO;
import se.vegas.tasknz.model.Wallet;
import se.vegas.tasknz.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.web.client.HttpClientErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.fail;
/**
 * Date: 25.01.2025
 *
 * @author Nikolay Zinin
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")

public class AcceptanceTest {

    @LocalServerPort
    int randomServerPort;
    private RestTemplate restTemplate;
    private String url;

    @Autowired
    private WalletRepository walletRepository;
    private String creditTransactionId;
    private String debitTransactionId;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        Wallet walletCredit = walletRepository.findByWalletId("walletId1")
                .orElseGet(() ->
                        Wallet.builder()
                                .walletId("walletId1")
                                .balance(new BigDecimal("0.00"))
                                .build()
                );
        Wallet walletDebit = walletRepository.findByWalletId("walletId2")
                .orElseGet(() ->
                        Wallet.builder()
                                .walletId("walletId2")
                                .balance(new BigDecimal("0.00"))
                                .build()
                );
        walletCredit.setBalance(BigDecimal.valueOf(1000.00));
        walletDebit.setBalance(BigDecimal.valueOf(1000.00));
        walletRepository.save(walletCredit);
        walletRepository.save(walletDebit);
        creditTransactionId = String.valueOf(UUID.randomUUID());
        debitTransactionId = String.valueOf(UUID.randomUUID());
    }

    @Test
    void testCredit()  {
        url = "http://localhost:" + randomServerPort + "/api/wallet/credit";
        BigDecimal balanceBeforeTransaction = walletRepository.findByWalletId("walletId1").orElseThrow()
                .getBalance();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(creditTransactionId)
                .walletId("walletId1")
                .amount(BigDecimal.valueOf(100.00))
                .build();

        ResponseEntity responseEntity = restTemplate.postForEntity(url, transactionDTO, Object.class);

        BigDecimal balanceAfterTransaction = walletRepository.findByWalletId("walletId1").orElseThrow()
                .getBalance();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(balanceAfterTransaction, balanceBeforeTransaction.add(BigDecimal.valueOf(100.00)));
    }

    @Test
    void testDebit() {
      url = "http://localhost:" + randomServerPort + "/api/wallet/debit";
      BigDecimal balanceBeforeTransaction = walletRepository.findByWalletId("walletId2").orElseThrow()
                .getBalance();
      TransactionDTO transactionDTO = TransactionDTO.builder()
              .transactionId(debitTransactionId)
              .walletId("walletId2")
              .amount(BigDecimal.valueOf(100.00))
              .build();

      ResponseEntity responseEntity = restTemplate.postForEntity(url, transactionDTO, Object.class);

      BigDecimal balanceAfterTransaction = walletRepository.findByWalletId("walletId2").orElseThrow()
              .getBalance();
      assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
      assertEquals(balanceAfterTransaction.add(BigDecimal.valueOf(100.00)), balanceBeforeTransaction);
    }

    @Test
    void testDebitWithNotEnoughMoneyOnAccountThrowException() throws JsonProcessingException {
        url = "http://localhost:" + randomServerPort + "/api/wallet/debit";
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(debitTransactionId)
                .walletId("walletId2")
                .amount(BigDecimal.valueOf(10000.00))
                .build();
        try {
            // Attempt to make the POST request
            restTemplate.postForEntity(url, transactionDTO, Object.class);
            fail("Expected HttpClientErrorException to be thrown");

        } catch (HttpClientErrorException e) {
            // Assert that the status code is 404 NOT_FOUND09
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());

            // Parse the response body as a Map
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseBody = objectMapper.readValue(e.getResponseBodyAsString(), Map.class);

            // Assert that the descriptionMessage is "Not enough money on balance"
            assertEquals("Not enough money on balance", responseBody.get("descriptionMessage"));//todo use enum instead of string
        }
    }
}

