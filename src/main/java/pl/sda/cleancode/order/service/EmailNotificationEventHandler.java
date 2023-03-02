package pl.sda.cleancode.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import pl.sda.cleancode.order.event.OrderMadeEvent;
import pl.sda.cleancode.order.infra.port.MailPort;

import java.util.List;

@RequiredArgsConstructor
final class EmailNotificationEventHandler {

    private final MailPort mailPort;

    @EventListener
    public void handle(OrderMadeEvent event) {
        // prepare and send email
        mailPort.send("subject", "body", List.of("recipient@test.pl"));
    }
}
