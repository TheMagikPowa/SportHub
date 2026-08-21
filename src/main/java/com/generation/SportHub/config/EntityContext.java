package com.generation.SportHub.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.generation.SportHub.entity.Address;
import com.generation.SportHub.entity.Buyer;
import com.generation.SportHub.entity.Cart;
import com.generation.SportHub.entity.CartHasProducts;
import com.generation.SportHub.entity.Event;
import com.generation.SportHub.entity.EventAnswer;
import com.generation.SportHub.entity.MessageQA;
import com.generation.SportHub.entity.Order;
import com.generation.SportHub.entity.OrderHasProducts;
import com.generation.SportHub.entity.Person;
import com.generation.SportHub.entity.Product;
import com.generation.SportHub.entity.QuestionQA;
import com.generation.SportHub.entity.User;

@Configuration
public class EntityContext {

    @Bean
    @Scope("prototype")
    public User user(Map<String, String> map) {
        User u= new User();
        u.fromMap(map);
        return u;
    }

    @Bean
    @Scope("prototype")
    public Person person(Map<String,String> map) {
        Person p= new Person();
        p.fromMap(map);
        return p;
    }

    @Bean
    @Scope("prototype")
    public Buyer buyer(Map<String, String> map) {
        Buyer b= new Buyer();
        b.fromMap(map);
        return b;
    }

    @Bean
    @Scope("prototype")
    public QuestionQA questionQA(Map<String, String> map) {
        QuestionQA q= new QuestionQA();
        q.fromMap(map);
        return q;
    }

    @Bean
    @Scope("prototype")
    public MessageQA messageQA(Map<String, String> map) {
        MessageQA q= new MessageQA();
        q.fromMap(map);
        return q;
    }

    @Bean
    @Scope("prototype")
    public Event event(Map<String, String> map) {
        Event q= new Event();
        q.fromMap(map);
        return q;
    }

    @Bean
    @Scope("prototype")
    public EventAnswer eventAnswer(Map<String, String> map) {
        EventAnswer q= new EventAnswer();
        q.fromMap(map);
        return q;
    }

    @Bean
    @Scope("prototype")
    public Address Address(Map<String, String> map) {
        Address q= new Address();
        q.fromMap(map);
        return q;
    }

    @Bean
    @Scope("prototype")
    public Cart cart(Map<String, String> map) {
        Cart q= new Cart();
        q.fromMap(map);
        return q;
    }

    @Bean
    @Scope("prototype")
    public CartHasProducts cartHasProducts(Map<String, String> map) {
        CartHasProducts q= new CartHasProducts();
        q.fromMap(map);
        return q;
    }

    @Bean
    @Scope("prototype")
    public Product product(Map<String, String> map) {
        Product q= new Product();
        q.fromMap(map);
        return q;
    }

    @Bean
    @Scope("prototype")
    public Order order(Map<String, String> map) {
        Order q= new Order();
        q.fromMap(map);
        return q;
    }

    @Bean
    @Scope("prototype")
    public OrderHasProducts orderHasProducts(Map<String, String> map) {
        OrderHasProducts q= new OrderHasProducts();
        q.fromMap(map);
        return q;
    }
    
}
