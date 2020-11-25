package com.banking.test.mapper;

import com.banking.test.dto.TransactionDto;
import com.banking.test.enums.TransactionTypeEnum;
import com.banking.test.model.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class TransactionMapperTest {

    private List<TransactionDto> transactionDtos;
    private List< Transaction > transactions;

    @Before
    public void setUp() throws Exception {
        LocalDateTime now = LocalDateTime.now();

        transactionDtos = Collections.singletonList(TransactionDto
                .builder()
                .transactionType(TransactionTypeEnum.DEPOSIT)
                .dateTime(now)
                .amount(5000D)
                .balance(10000D)
                .build());

        transactions = Collections.singletonList(Transaction
                .builder()
                .id(1L)
                .username("username")
                .transactionType(TransactionTypeEnum.DEPOSIT)
                .dateTime(now)
                .amount(5000D)
                .balance(10000D)
                .build());
    }

    @Test
    public void transactionTotransactionDto() {

        List<TransactionDto> transactionDtosResult = TransactionMapper.INSTANCE.transactionTotransactionDto(transactions);

        assertEquals(transactions.size(), transactionDtosResult.size());

        TransactionDto transactionDtoResult = transactionDtosResult.get(0);
        TransactionDto transactionDto = transactionDtos.get(0);

        assertEquals(transactionDto.getTransactionType(), transactionDtoResult.getTransactionType());
        assertEquals(transactionDto.getDateTime(), transactionDtoResult.getDateTime());
        assertEquals(transactionDto.getAmount(), transactionDtoResult.getAmount());
        assertEquals(transactionDto.getBalance(), transactionDtoResult.getBalance());
    }
}