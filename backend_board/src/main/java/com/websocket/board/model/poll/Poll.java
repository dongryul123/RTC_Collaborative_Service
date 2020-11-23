package com.websocket.board.model.poll;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Poll implements Serializable {

    private String left;
    private String top;
    private String question;
    private List<Answer> answers;

    private boolean multipleVotes;
    private int totalVotes;
    private List<String> userVoted;
    private boolean isSetAll;
    private boolean isEnd;

}
