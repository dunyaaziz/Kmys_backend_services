package com.hoaxify.ws.model;

import jdk.jfr.DataAmount;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String token;

    private Long expiredAt;

    @ManyToOne
    private User user;
}
