package pl.sda.refactoring.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sda.refactoring.dto.OrderRequest;
import pl.sda.refactoring.service.OrderService;

import java.net.URI;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final @NonNull OrderService orderService;

    @PostMapping
    @SneakyThrows
    ResponseEntity<Void> makeOrder(@RequestBody @Valid OrderRequest request) {
        var orderId = orderService.handle(request.toCommand());
        return ResponseEntity.created(new URI("/api/orders/" + orderId.id())).build();
    }
}
