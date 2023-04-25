package com.almostThere.domain.meeting.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class CalculateDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calculate_detail_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meeting_id", foreignKey = @ForeignKey(name = "calculate_detail_meeting_idx"), nullable = false)
    private Meeting meeting;

    @Enumerated(EnumType.STRING)
    private CalculateType type;

    @Column(length = 300)
    private String filePath;

    @Column(length = 300)
    private String fileName;

    @Column(length = 50)
    private String storeName;

    @Column(nullable = false)
    private int price;
}
