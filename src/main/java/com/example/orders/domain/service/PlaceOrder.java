package com.example.orders.domain.service;

import com.example.orders.domain.model.Order;

/** Driving (inbound) port: what the outside world may ask the domain to do. */
public interface PlaceOrder {

    Order place(String product, int quantity);
}
