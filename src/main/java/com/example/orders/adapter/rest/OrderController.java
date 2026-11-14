package com.example.orders.adapter.rest;

import com.example.orders.domain.service.PlaceOrder;
import com.example.orders.domain.model.InvalidOrderException;
import com.example.orders.domain.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Driving adapter: translates HTTP into calls on the PlaceOrder port. */
@RestController
@RequestMapping("/orders")
public final class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final PlaceOrder placeOrder;

    public OrderController(final PlaceOrder placeOrder) {
        this.placeOrder = placeOrder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@RequestBody final PlaceOrderRequest request) {
        final Order order = placeOrder.place(request.product(), request.quantity());
        log.debug("Placed order {}", order.id());
        return toOrderResponse(order);
    }

    private OrderResponse toOrderResponse(final Order order) {
        return new OrderResponse(order.id(), order.product(), order.quantity());
    }

    @ExceptionHandler(InvalidOrderException.class)
    public ProblemDetail handleInvalidOrder(final InvalidOrderException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
