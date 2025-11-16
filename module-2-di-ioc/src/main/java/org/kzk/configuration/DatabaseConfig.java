package org.kzk.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
@EnableR2dbcRepositories
public class DatabaseConfig {
    @Bean
    public TransactionalOperator reactiveTx(ReactiveTransactionManager tm) {
        return TransactionalOperator.create(tm);
    }
}
