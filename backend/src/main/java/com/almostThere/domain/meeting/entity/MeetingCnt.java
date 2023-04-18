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
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
public class MeetingCnt {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_cnt_id")
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "my_member_id", foreignKey = @ForeignKey(name = "fk_meeting_cnt_member_idx1"), nullable = false)
    private Member myMember;

    @ManyToOne
    @JoinColumn(name = "friend_id", foreignKey = @ForeignKey(name = "fk_meeting_cnt_member_idx2"), nullable = false)
    private Member friend;

    @ColumnDefault("1")
    @Column(nullable = false)
    private int cnt;

}
