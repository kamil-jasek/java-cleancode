package pl.sda.cleancode.order.infra.port;

import java.util.List;

public interface MailPort {
    void send(String subject, String body, List<String> recipients);
}
