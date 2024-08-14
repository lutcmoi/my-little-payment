package org.bdabos.services;

import okhttp3.mockwebserver.MockWebServer;
import org.bdabos.config.MyLittleExchangeProperties;
import org.bdabos.exceptions.ExchangeRateUnavailableException;
import org.bdabos.model.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class ExchangeServiceTest {

    private static MockWebServer mockWebServer;

    private ExchangeService exchangeService;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        MyLittleExchangeProperties myLittleExchangeProperties = new MyLittleExchangeProperties();
        myLittleExchangeProperties.setBaseUrl(mockWebServer.url("/").toString());

        exchangeService = new ExchangeService(WebClient.builder(), myLittleExchangeProperties);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("Same currency should return the same amount")
    void sameCurrency() {
        BigDecimal result = exchangeService.getFinalAmount(Account.builder().currency("USD").build(), Account.builder().currency("USD").build(), BigDecimal.TEN);
        assertEquals(BigDecimal.TEN, result);
    }

    @Test
    @DisplayName("No exchange rate retrieved")
    void noExchangeRate() {
        mockWebServer.enqueue(new okhttp3.mockwebserver.MockResponse().setResponseCode(200).setBody(""));
        assertThrows(ExchangeRateUnavailableException.class, () -> exchangeService.getFinalAmount(Account.builder().currency("USD").build(), Account.builder().currency("EUR").build(), BigDecimal.TEN));
    }

    @Test
    @DisplayName("Exchange rate service not available")
    void exchangeRateServiceNotAvailable() throws IOException {
        mockWebServer.shutdown();
        assertThrows(ExchangeRateUnavailableException.class, () -> exchangeService.getFinalAmount(Account.builder().currency("USD").build(), Account.builder().currency("EUR").build(), BigDecimal.TEN));
    }

    @Test
    @DisplayName("Exchange rate applied")
    void exchangeRateApplied() {
        mockWebServer.enqueue(new okhttp3.mockwebserver.MockResponse().setResponseCode(200).setHeader("content-type", "application/json").setBody("1.5"));

        BigDecimal result = exchangeService.getFinalAmount(Account.builder().currency("USD").build(), Account.builder().currency("EUR").build(), BigDecimal.TEN);
        assertEquals(new BigDecimal("15.0"), result);
    }
}
