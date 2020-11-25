package com.banking.test.mapper;

import com.banking.test.dto.TransactionDto;
import com.banking.test.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    List<TransactionDto> transactionTotransactionDto(List<Transaction> transaction);
}
