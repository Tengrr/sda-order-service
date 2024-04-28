package edu.cmu.orderservice.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bid {
    private String productName;

    // The price serves as observee's internal status
    private double price;

    // The bidUserList serves as an observer list
    private String bidUserList;
}
