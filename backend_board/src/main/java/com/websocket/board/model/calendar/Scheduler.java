package com.websocket.board.model.calendar;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.websocket.board.model.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Scheduler implements Serializable {

    @Id
    @Column(nullable = false, name = "scheduler_id")
    private String id;
    @Column(name = "\"left\"")
    private String left;
    private String top;

    @OneToMany(mappedBy = "scheduler")
    @JsonManagedReference
    private List<Event> events = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "channel_id")
    @JsonBackReference
    private Channel channel;
}
