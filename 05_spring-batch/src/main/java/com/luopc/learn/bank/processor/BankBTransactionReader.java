package com.luopc.learn.bank.processor;

import com.luopc.learn.bank.model.BankTransaction;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class BankBTransactionReader extends JdbcCursorItemReader<BankTransaction> {

    private static final String SQL =
            "SELECT id, transaction_id, bank_name, account_number, amount, transaction_date, description " +
                    "FROM bank_transaction " +
                    "WHERE bank_name = 'BankB'";

    public BankBTransactionReader(DataSource dataSource) {
        setDataSource(dataSource);
        setSql(SQL);
        setRowMapper(new BankTransactionRowMapper());
    }

    private static class BankTransactionRowMapper implements RowMapper<BankTransaction> {
        @Override
        public BankTransaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            BankTransaction transaction = new BankTransaction();
            transaction.setId(rs.getLong("id"));
            transaction.setTransactionId(rs.getString("transaction_id"));
            transaction.setBankName(rs.getString("bank_name"));
            transaction.setAccountNumber(rs.getString("account_number"));
            transaction.setAmount(rs.getBigDecimal("amount"));
            transaction.setTransactionDate(rs.getDate("transaction_date").toLocalDate());
            transaction.setDescription(rs.getString("description"));
            return transaction;
        }
    }
}
