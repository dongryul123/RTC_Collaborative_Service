package com.websocket.board.model.member;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.websocket.board.model.Channel;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDate enterDate;

    private long numOfAttendance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    @JsonBackReference
    private Channel channel;
}
