package com.websocket.board.model.kanban;

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
public class Kanban implements Serializable {

    @Id
    private String id;
    private String kanbanName;

    @OneToMany(mappedBy = "kanban")
    @JsonManagedReference
    private List<State> states = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "channel_id")
    @JsonBackReference
    private Channel channel;
}
