package pl.sda.refactoring.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    public void send(String subj, String body, List<String> recipients) {
        throw new InfrastructureException("email service failure");
    }
}
