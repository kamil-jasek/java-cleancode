package pl.sda.cleancode.order.infra.port.adapter.mail;

import pl.sda.cleancode.order.infra.port.MailPort;

import java.util.List;

final class DummyMailPortAdapter implements MailPort {

    @Override
    public void send(String subject, String body, List<String> recipients) {
        // send email via SMTP
    }
}
