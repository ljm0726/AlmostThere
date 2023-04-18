package com.almostThere.domain.user.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String memberEmail;

    @Column(length = 300)
    private String memberProfileImg;

    @Column(nullable = false, length = 30)
    private String memberNickname;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime regdate;


}
