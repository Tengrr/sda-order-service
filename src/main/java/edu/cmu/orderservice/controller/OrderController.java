package edu.cmu.orderservice.controller;

import edu.cmu.orderservice.entity.Order;
import edu.cmu.orderservice.repository.OrderRepository;
import edu.cmu.orderservice.service.MessageSender;
import edu.cmu.orderservice.service.WebSocketService;
import edu.cmu.orderservice.util.OrderConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("/orders")
public class OrderController implements OrderConstant {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private WebSocketService webSocketService;


    @PostMapping
    public @ResponseBody Order addOrder(@RequestBody Order order) {
        Timestamp nowTime = new Timestamp(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(nowTime.getTime());
        cal.add(Calendar.MINUTE, 30);
        Timestamp expireTime = new Timestamp(cal.getTimeInMillis());

        order.setCreateTime(nowTime);
        order.setExpireTime(expireTime);
        order.setStatus(0);

        Order response =  orderRepository.save(order);
        messageSender.sendToQueue("cyber-mall-orders", order);

        return response;
    }

    @GetMapping
    public @ResponseBody List<Map<String, Object>> getOrders(@RequestParam(required = false) Long userId) {
        List<Map<String, Object>> response = new ArrayList<>();

        List<Order> orders = null;
        if (userId != null) {
            orders = orderRepository.findByUserId(userId);
        } else {
            orders = (List<Order>) orderRepository.findAll();
        }

        for (int i = 0; i < orders.size(); i++) {
            Map<String, Object> currMap = new HashMap<>();
            Order order = orders.get(i);
            currMap.put("id", order.getId());
            currMap.put("productId", order.getProductId().toString());
            currMap.put("userId", order.getUserId().toString());
            currMap.put("price", order.getPrice());
            currMap.put("createTime", order.getCreateTime());
            currMap.put("expireTime", order.getExpireTime());
            currMap.put("status", order.getStatus());
            response.add(currMap);
        }
        return response;
    }

    // Temporarily only support updating status
    @PostMapping("/{id}")
    public @ResponseBody Order updateOrder(@PathVariable Integer id,
                                           @RequestBody Order newOrder) {
        Optional<Order> orderOptional = orderRepository.findById(id);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(newOrder.getStatus());
            return orderRepository.save(order);
        }

        return null;
    }

    @GetMapping("/{id}")
    public @ResponseBody Order getOrderById(@PathVariable int id) {
        return orderRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable int id) {
        orderRepository.deleteById(id);
    }

}
