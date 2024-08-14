package org.bdabos.services;

import lombok.extern.slf4j.Slf4j;
import org.bdabos.config.MyLittleExchangeProperties;
import org.bdabos.dtos.ExchangeRateDto;
import org.bdabos.exceptions.ExchangeRateUnavailableException;
import org.bdabos.model.Account;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

@Slf4j
@Service
public class ExchangeService {

    private final WebClient webClient;

    public ExchangeService(WebClient.Builder webClientBuilder, MyLittleExchangeProperties myLittleExchangeProperties) {
        this.webClient = webClientBuilder.baseUrl(myLittleExchangeProperties.getBaseUrl()).build();
    }

    public BigDecimal getFinalAmount(Account from, Account to, BigDecimal amount) {
        if (from.getCurrency().equals(to.getCurrency())) {
            log.info("Origin account currency=[{}] is the same as destination account currency, no exchange rate applied", from.getCurrency());
            return amount;
        }

        log.info("Origin account currency=[{}] is different than destination account currency=[{}], applying exchange rate", from.getCurrency(), to.getCurrency());
        BigDecimal exchangeRate = getExchangeRate(from.getCurrency(), to.getCurrency());

        BigDecimal finalAmount = amount.multiply(exchangeRate);
        log.info("The transfer of {} {} to {} with applied exchange rate {} resulted in {} {}", amount, from.getCurrency(), to.getCurrency(), exchangeRate, finalAmount, to.getCurrency());

        return finalAmount;
    }

    private BigDecimal getExchangeRate(String from, String to) {
        // Distant call
        BigDecimal exchangeRate = webClient.post().uri("/exchange-rate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ExchangeRateDto.builder().currencyFrom(from).currencyTo(to).build())
                .retrieve()
                .bodyToMono(BigDecimal.class)
                .onErrorMap(throwable -> new ExchangeRateUnavailableException("Exchange rate unavailable for " + from + " to " + to, throwable))
                .block();

        if (exchangeRate == null) {
            throw new ExchangeRateUnavailableException("Exchange rate unavailable for " + from + " to " + to);
        }

        return exchangeRate;
    }
}
