package com.banking.test.dto;

import com.banking.test.enums.TransactionTypeEnum;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TransactionDto {

    private TransactionTypeEnum transactionType;
    private LocalDateTime dateTime;
    private double amount;
    private double balance;

}
