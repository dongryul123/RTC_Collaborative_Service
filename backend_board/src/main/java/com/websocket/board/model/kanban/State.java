package com.websocket.board.model.kanban;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class State implements Serializable {

    @Id
    @Column(nullable = false, name = "state_id")
    private String id;
    private String columnTitle;

    @OneToMany(mappedBy = "state")
    @JsonManagedReference
    private List<Task> tasks = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kanban_id")
    @JsonBackReference
    private Kanban kanban;
}
