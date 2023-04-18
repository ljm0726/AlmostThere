package com.almostThere.domain.meeting.entity;

import com.almostThere.domain.user.entity.Member;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "host_id", foreignKey = @ForeignKey(name = "fk_meeting_member_idx"), nullable = false)
    private Member host;

    @Column(length = 30, nullable = false)
    private String meetingName;

    @Column(nullable = false)
    private LocalDateTime meetingTime;

    @Column(length = 100, nullable = false)
    private String meetingPlace;

    @Column
    private double meetingLat;

    @Column
    private double meetingLng;

    @Column
    private int lateAmount;

    @CreationTimestamp // DB에 insert할 때 현재시간 자동 지정
    @Column(nullable = false)
    private LocalDateTime regdate;

    @Column(nullable = false)
    private int roomCode;

}
