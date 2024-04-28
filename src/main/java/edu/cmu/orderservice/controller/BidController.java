package edu.cmu.orderservice.controller;

import edu.cmu.orderservice.entity.Bid;
import edu.cmu.orderservice.entity.Order;
import edu.cmu.orderservice.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Controller
@RequestMapping("/bid")
public class BidController {
    @Autowired
    private WebSocketService webSocketService;

    @PostMapping
    public @ResponseBody Bid broadCastBid(@RequestBody Bid bid) {
        String message = "The highest bid for " + bid.getProductName()
                + " was updated to $" + bid.getPrice()
                + ", please bid for the product again!";

        // Every time the status(highest bid) is changed, the observee broadcast a message
        // to its observer's(users who have bid for this product)
        // to inform them of this update
        // using websocket message
        String[] userList = bid.getBidUserList().split(",");
        String currUser = userList[userList.length - 1];
        Set<String> userSet = new HashSet<>();
        Collections.addAll(userSet, userList);
        userSet.remove(currUser);

        for (String userId : userSet) {
            webSocketService.send(message, userId);
            System.out.println("sent to " + userId);
        }

        return bid;
    }
}
