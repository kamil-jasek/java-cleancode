package pl.sda.refactoring.service;

import pl.sda.refactoring.service.OrderSettings.DiscountSettings;
import pl.sda.refactoring.service.port.DiscountPort;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class DiscountPriceCalculatorTest {

    private final DiscountPort discountPort = mock(DiscountPort.class);
    private final Clock fixed = Clock.fixed(Instant.parse("2022-03-04T10:00:00Z"), UTC);
    private final DiscountPriceCalculator calculator = new DiscountPriceCalculator(
        discountPort,
        new OrderSettings(
            new DiscountSettings(List.of(LocalDate.now(fixed))),
            List.of()
        ),
        fixed);


}