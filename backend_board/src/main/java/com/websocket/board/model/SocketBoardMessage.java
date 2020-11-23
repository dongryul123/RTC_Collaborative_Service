package com.websocket.board.model;

import com.websocket.board.model.calendar.Scheduler;
import com.websocket.board.model.crud.CRUDModule;
import com.websocket.board.model.kanban.Kanban;
import com.websocket.board.model.poll.Poll;
import com.websocket.board.model.postit.Postit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocketBoardMessage implements Serializable {

    // 채널 자체 정보
    private String channelId;
    private long userCount;
    //private String channelName;
    //private String sender;

    // 보드 전달 모듈 관련 인자
    private long idCount;

    private List<Postit> postitList; // 포스트잇 리스트
    private Boolean isKanban;
    private Kanban kanban;
    private Scheduler scheduler;
    private Poll poll;

    private CRUDModule crudModule;
    private Boolean isDelete;

    // List<Attendance> attendenceList; // 출석부 리스트
    // List<Attendance> voteList; // 출석부 리스트

    // 멤버, 부착물 관련 인자
    //private final int MAX_FIXTURES = 100; // 최대 부착물 갯수
    //private final int MAX_MEMBERS = 50; // 최대 멤버 수
    //private long userCount; // 멤버 수
    //private long numOfFixtures; //부착물 갯수
    //private boolean overMaxMembers; // 최대 멤버 수를 넘었는지
    //private boolean overMaxFixtures; // 최대 부착물 수를 넘었는지

}
