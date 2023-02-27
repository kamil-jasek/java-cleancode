package pl.sda.cleancode.order.infra.http;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sda.cleancode.order.service.OrderServiceFacade;

import java.net.URI;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
final class MakeOrderRestEndpoint {

    private final @NonNull OrderServiceFacade facade;

    @PostMapping
    @SneakyThrows
    ResponseEntity<Void> makeOrder(@RequestBody @Valid MakeOrderRequest request) {
        final var orderConfirmation = facade.handle(request.toCommand());
        return ResponseEntity
            .created(new URI("/api/orders/" + orderConfirmation.orderId().id()))
            .build();
    }
}
