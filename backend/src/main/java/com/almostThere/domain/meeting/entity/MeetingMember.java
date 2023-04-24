package com.almostThere.domain.meeting.entity;

import com.almostThere.domain.user.entity.Member;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
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
    private Double spentMoney;

    public MeetingMember (Member member, Meeting meeting, StateType state){
        this.member = member;
        this.meeting = meeting;
        this.state = state;
    }
    @Builder
    public MeetingMember (Member member, Meeting meeting, String startPlace, String startAddress
        ,Double startLat, Double startLng, StateType state, Double spentMoney) {
        this.member = member;
        this.meeting = meeting;
        this.startPlace = startPlace;
        this.startAddress = startAddress;
        this.startLat = startLat;
        this.startLng = startLng;
        this.state = state;
        this.spentMoney = spentMoney;
    }
}
