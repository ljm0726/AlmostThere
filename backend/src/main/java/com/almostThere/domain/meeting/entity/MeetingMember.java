package com.almostThere.domain.meeting.entity;

import com.almostThere.domain.user.entity.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MeetingMember {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "meeting_member_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_meeting_member_member_idx"), nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "meeting_id", foreignKey = @ForeignKey(name = "fk_meeting_member_meeting_idx"), nullable = false)
    private Meeting meeting;

    @Column(length = 50)
    private String startPlace;

    @Column
    private double startLat;

    @Column
    private double startLng;

    @Column(nullable = false, length = 50)
    private StateType state;

    @Column
    private double spentMoney;
}
