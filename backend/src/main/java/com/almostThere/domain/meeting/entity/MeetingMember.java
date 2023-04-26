package com.almostThere.domain.meeting.entity;

import com.almostThere.domain.user.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(length = 100)
    private String startPlace;

    @Column(length = 200)
    private String startAddress;

    @Column
    private Double startLat;

    @Column
    private Double startLng;

    @Enumerated(EnumType.STRING)
    private StateType state;

    @Column
    private double spentMoney;
}
