package org.bdabos.controllers.mocks;

import org.bdabos.dtos.ExchangeRateDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Random;

@RestController
@RequestMapping("/exchange-rate")
public class ExchangeRateController {

    Random random = new Random();

    @PostMapping()
    public BigDecimal getExchangeRate(@RequestBody ExchangeRateDto exchangeRateDto) {
        // Mock method
        return BigDecimal.valueOf(random.nextDouble());
    }
}
