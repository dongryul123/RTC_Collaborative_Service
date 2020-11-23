<template>
  <div id="app" v-cloak @click="cloakMoveable">
    <div class="toolbar">
      <v-snackbar
        app
        top
        v-model="snackbar.isPresent"
        :timeout="snackbar.timeout"
        :color="snackbar.color"
      >{{ snackbar.text }}</v-snackbar>
      <div>
        <h4>
          {{channelName}}
          <span class="badge badge-info badge-pill">{{userCount}}</span>

        <v-toolbar class="toolBox">
      <v-btn icon color="orange" @click="pleaseDrag" draggable="true" @dragenter="dragging=true" @dragend="moduleDragEnd('postit', $event)">
        <v-icon>mdi-message</v-icon>
      </v-btn>
      <v-btn icon color="orange" @click="pleaseDrag" draggable="true" @dragend="moduleDragEnd('kanban', $event)">
        <v-icon>mdi-clipboard-list-outline</v-icon>
      </v-btn>
      <!-- <v-btn icon color="orange" @click="createMap">
        <v-icon>mdi-map</v-icon>
      </v-btn> -->
      <v-btn icon color="orange" @click="pleaseDrag" draggable="true" @dragend="moduleDragEnd('scheduler', $event)">
        <v-icon>mdi-calendar</v-icon>
      </v-btn>
      <v-btn icon color="orange" @click="pleaseDrag" draggable="true" @dragend="moduleDragEnd('poll', $event)">
        <v-icon>mdi-vote</v-icon>
      </v-btn>
    	</v-toolbar>

		</h4>
        <br />
        
      </div>
    </div>

    

    <v-responsive class="vueBox text-center ma-3">
      
    </v-responsive>

    <v-responsive>
      <v-responsive
        class="userListBadge badge-info text-center lighten-2 rounded-circle d-inline-flex align-center justify-center ma-3"
        @mouseover="testIn"
        @mouseout="testOut"
      >

        <v-img src="@/assets/img/user.png">
          {{userCount}}
        </v-img>

      
        <!-- <v-hover v-slot:default="{ hover }">
            <v-img src="@/assets/img/user.png">
              {{userCount}}
              <v-expand-transition>
                <div
                  v-if="hover"
                  class="d-flex transition-fast-in-fast-out orange darken-2 v-card--reveal display-3 white--text"
                  style="height: 100%;"
                >
                </div>
              </v-expand-transition>
              
            </v-img>
            
        </v-hover> -->
            
          


      </v-responsive>
      
      <transition name="slide-fade">
        <v-responsive id="memberList" class="text-center ma-3 badge-info" 
        v-if="memberView"
        align="center"
        justify="center">
        김강현, 배민규, 정용우, 최문경, 김동률, 배재원
        </v-responsive>
      </transition>
    </v-responsive>
    

    <Moveable
      ref="moveable"
      class="moveable"
      v-bind="moveable"
      @drag="handleDrag"
      @dragEnd="handleDragEnd"
      @resize="handleResize"
      @scale="handleScale"
      @rotate="handleRotate"
      @warp="handleWarp"
      style="display: none;"
    ></Moveable>

    <div class="bodyBox" ref="whiteBoard" 
    @dblclick="focusAction" 
    @click="changeTargetAction"
    @wheel="wheelEvent"
    style="height: 100%; width: 100%;">
      
      
      <div class="MoveableBox realBoard" >

            <div class="postit"
      v-for="(pi, idx) in this.board.postitList"
      :key="pi.frontPostitId"
      @click.right="deleteTargetAction(idx, $event)">
          <Postit
          :id="pi.frontPostitId"
          :postit="pi"
          :style="{left: pi.left, top: pi.top}"
          />

      </div>

      <div class="kanban" v-if="board.isKanban" @click.right="deleteKanban" >
        <Kanban/>
      
 
      </div>
      <div class="map" @click.right="deleteAction">
        <Map v-if="map.isPresent"/>
      </div>

      <div class="Scheduler" @click.right="deleteAction">
        <Scheduler v-if="!!board.scheduler" 
        :style="{left:board.scheduler.left, top:board.scheduler.top}"/>
      </div>

      <div class="Poll" @click.right="deleteAction">
        <Poll v-if="isPoll" 
        :style = "{left: board.poll.left, top: board.poll.top}"
        />
      </div>
     
    
     {{ board }}
      

      </div>
       
 
      <!-- <Postit :id="pi.id" :postit="pi" style="position: relative; display: inline-block"/> -->
     </div>
 <Chat/>
  </div>
</template>

<script>
import SockJS from "sockjs-client";
import Stomp from "stomp-websocket";
import http from "../../http-common.js";
import Moveable from "vue-moveable";
import Postit from "../../components/module/Postit";
import Map from "../../components/module/Map";
import Scheduler from "../../components/module/Scheduler";
import Chat from "../../components/common/Chat";
import Poll from "../../components/common/Poll"
import Kanban from "../../components/module/Kanban";

const boardLength = 10000;
export default {
  props:[

  ]
  ,
  data() {
    return {
      ws: null,
      channelName: "",
      // 소켓 서버 전송
      board: {
        channelId: "",
        idCount: 1,
        crudModule: {
          modulType: '',
          crudType: '',
          moduleObject: Object,
        },
        postitList: [],
        isKanban: false,
        kanban: this.$store.state.Kanban,
        scheduler: {},
        poll: {},
        isDelete: false,
        delete: {
          moduleName: '',
          id: -1,
        },
      },
      token: "",
      userCount: 0,
      moveable: {
        target: "",
        draggable: true,
        throttleDrag: 1,
        resizable: false,
        throttleResize: 1,
        keepRatio: false,
        scalable: true,
        throttleScale: 0,
        rotatable: true,
        throttleRotate: 0,
        origin: false,
      },
      
      map: {
        isPresent: false,
        left: "",
        top: "",
        lat: 0,
        lng: 0,
      },
      snackbar: {
        isPresent: false,
        text: "",
        timeout: 1000,
      },
      boardScale: 1,
      boardX: boardLength/2,
      boardY: boardLength/2,

      memberView:false,
      idc: 0,
      isPoll: false,
    };
  },
  created() {
    this.init();
    window.oncontextmenu = function () {
      // 우클릭 default이벤트 차단
      return false;
    };
    this.initRecv();
  },
  mounted() {
    document.querySelector('.realBoard').style.height = boardLength+"px";
    document.querySelector('.realBoard').style.width = boardLength+"px";

    console.log((boardLength/2) - (window.innerWidth * 0.4));
    document.querySelector('.realBoard').style.left = 
      -(boardLength/2) + (window.innerWidth * 0.4) + "px";
      
    document.querySelector('.realBoard').style.top = 
      -(boardLength/2) + (window.innerHeight * 0.4) + "px";
  },
  methods: {
    init() {
      // var BASE_URL =  "http://i3a510.p.ssafy.io/api"
      var BASE_URL = "http://localhost:8080";
      // var BASE_URL = "http://218.146.39.122:8080";
      var sock = new SockJS(BASE_URL + "/ws-stomp");
      var ws = Stomp.over(sock);
      this.ws = ws;

      this.board.channelId = localStorage.getItem("wsboard.channelId");
      this.channelName = localStorage.getItem("wsboard.channelName");
      var _this = this;
      http.get("/board/user").then((response) => {
        _this.token = response.data.token;
        ws.connect(
          { token: _this.token },
          function (frame) {
            ws.subscribe("/sub/board/channel/" + _this.board.channelId, function (
              message
            ) {
              var recv = JSON.parse(message.body);
              _this.recvMessage(recv);
            });
          },
          function (error) {
            alert("서버 연결에 실패 하였습니다. 다시 접속해 주십시요.");
            location.href = "/";
          }
        );
      });
    },
    initRecv() {
      // 접속시 처음 값을 받아오도록 하기
      http
        .get(`/board/${this.board.channelId}`)
        .then((response) => {
          console.log(response.data)
          // this.board.postitList = response.data.postitList;
          // this.board.idCount = response.data.idCount;
          this.board = response.data;
          this.board.delete = { moduleName:'', id: -1}
        })
        .catch((e) => {
        });
      this.createSnackbar(
        `'${this.channelName}' 채널에 입장하였습니다!`,
        3000,
        "info"
      );
    },
    sendMessage: function (type) {
      this.ws.send(
        "/pub/board/message",
        { token: this.token },
        JSON.stringify(this.board)
      );
      this.createSnackbar("수정되었습니다", 1000, "warning");
    },
    recvMessage: function (recv) {
      console.log("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2")
      console.log(recv.poll);
      this.userCount = recv.userCount;
      this.board.idCount = recv.idCount;
      this.board.postitList = recv.postitList;
      this.board.isDelete = false;
      if (!!recv.scheduler) this.board.scheduler = recv.scheduler;
      this.board.poll = recv.poll;
      this.$store.state.scheduler.events = recv.scheduler.events;
      //crudModule 초기화
      this.board.crudModule = {
        modulType: '',
        crudType: '',
        moduleObject: Object,
      };
    },
    createPostit(left='500px', top='170px') {
      if(this.board.postitList.length > 20) {
        this.createSnackbar("포스트잇이 너무 많습니다!", 3000, "error")
        return
      }
      event.stopPropagation();
      const idc = this.board.idCount++;
      // postitList에 새로운 포스트잇 더하기
      var newPostit = {
        frontPostitId: idc,
        left: (this.boardX - 120) + "px",
        top: (this.boardY - 120) + "px",
        title: "",
        contents: "",
        channel: this.board.channelId,
      }
      this.board.postitList.push(newPostit);
      this.crudMethod('POSTIT', 'CREATE', newPostit);
      this.sendMessage();
      this.crudMethod('', '', null);
      // snackbar
      this.createSnackbar("포스트잇이 생성되었습니다!", 1500, "success");
    },

    createKanban(event) {
      if(this.board.isKanban==true){
        this.createSnackbar("보드가 이미 생성되어 있습니다", 3000, "error")
        return
      }
  
      this.createSnackbar("보드가 생성되었습니다", 1500, "success")
      this.board.isKanban=true
      this.board.kanban = this.$store.state.Kanban
    },

    deleteKanban({target}) {
      if(confirm("요소를 삭제하시겠습니까?") === true) {
        target.remove();
        this.cloakMoveable();
        this.board.isKanban=false;
        this.$store.state.Kanban.columns=[ {
                  columnTitle: '할 일',
                  tasks: [],
                },
                {
                  columnTitle: "진행중",
                  tasks: [],
                },
                {
                  columnTitle: "완료",
                  tasks: [],
                },]
        
      }
    },





    createMap(event) {
      if (this.map.isPresent) {
        this.createSnackbar("이미 카카오맵이 있습니다!", 3000, "error");
      } else {
        this.map.isPresent = true;
      }
    },    
    createScheduler(left='600px', top='270px') {
      if (!!this.board.scheduler){
        this.createSnackbar("이미 달력이 있습니다!", 3000, "error");
      } else {
        this.board.scheduler = {
          left: left,
          top: top,
          events: this.$store.state.scheduler.events,
        }
        console.log("create Scheduler");
        console.log(this.board.scheduler);
        this.sendMessage();
        // snackbar
        this.createSnackbar("달력이 생성되었습니다!", 1500, "success");
      }
    },
    
    createPoll(left='500px', top='170px') {
      if (this.isPoll){
        this.createSnackbar("이미 투표가 있습니다!", 3000, "error");
      } else {
        this.isPoll = true;
        const idc = this.board.idCount++;
        this.board.poll = this.$store.state.poll;
        this.board.poll.left = left;
        this.board.poll.top = top;
        this.crudMethod('POLL', 'CREATE', this.board.poll);
        this.sendMessage();
        // snackbar
        this.createSnackbar("투표가 생성되었습니다!", 1500, "success");
      }
    },

    createSnackbar(text, timeout, color) {
      this.snackbar.isPresent = true;
      this.snackbar.text = text;
      this.snackbar.timeout = timeout;
      this.snackbar.color = color;
    },
    handleDrag({ target, left, top }) {
      target.style.top = `${top}px`;
      target.style.left = `${left}px`;
      if (target.getAttribute("class") != null) {
        var clas = target.getAttribute("class").split(" ");
        for (var cla in clas) {
          if (clas[cla] == "paper") {
              this.board.postitList.map((postit) => {
              if (postit.frontPostitId == target.id) {
                (postit.left = `${left}px`), (postit.top = `${top}px`);
              }
              return {
                ...postit,
              };
            });
          } else if(clas[cla] == "scheduler") {
            this.board.scheduler.left = `${left}px`
            this.board.scheduler.top = `${top}px`
          } else if(clas[cla] == "Pollx") {
            // this.board.poll.left = `${left}px`
            // this.board.poll.top = `${top}px`
          }
          else if(clas[cla] == 'realBoard'){
            let lp = target.style.left.replace("px", "");
            let tp = target.style.top.replace("px", "");
            // console.log(lp, " , ", tp);

            this.boardX = (lp*-1) + window.innerWidth * 0.4;
            this.boardY = (tp*-1) + window.innerHeight * 0.365;

            var limitUnit = (this.boardScale / 0.05) * 250 - (boardLength/2);   

            console.log(lp - limitUnit);         
            if(lp > limitUnit) {
              document.querySelector('.bodyBox').style.borderLeft = "red 3px solid";
              target.style.left = limitUnit+'px'
            }
            else if((lp) < (-boardLength + (window.innerWidth)) - limitUnit) {
              document.querySelector('.bodyBox').style.borderRight = "red 3px solid";
              target.style.left = (-boardLength + (window.innerWidth) - limitUnit) +'px';
            } 
            else {
              document.querySelector('.bodyBox').style.borderRight = "1px pink solid";
              document.querySelector('.bodyBox').style.borderLeft = "1px pink solid";
            }

            if(tp > limitUnit) {
              target.style.top = limitUnit+'px'
              document.querySelector('.bodyBox').style.borderTop = "red 3px solid";
            }
            else if (tp < (-boardLength + (window.innerHeight)) - limitUnit) {
              target.style.top = (-boardLength + (window.innerHeight)) - limitUnit +'px';
              document.querySelector('.bodyBox').style.borderBottom = "red 3px solid";
            }
            else {  
              document.querySelector('.bodyBox').style.borderTop = "1px pink solid";
              document.querySelector('.bodyBox').style.borderBottom = "1px pink solid";
            }

            return ;
          }
        }
      }
    },
    handleDragEnd({target}) {
      console.log(target);
      var moduleObj = null;
      switch(target.nodeName) {
        case 'POSTIT':
          moduleObj = this.board.postitList.find(postit => postit.frontPostitId == target.id);
          break;
        case 'SCHEDULER':
          break;
      }
      this.crudMethod(target.nodeName, 'UPDATE', moduleObj);
      this.sendMessage();
      this.crudMethod('','',null);
    },
    handleResize({ target, width, height, delta }) {
      delta[0] && (target.style.width = `${width}px`);
      delta[1] && (target.style.height = `${height}px`);
    },
    handleScale({ target, transform, scale }) {
      target.style.transform = transform;
    },
    handleRotate({ target, dist, transform }) {
      target.style.transform = transform;
    },
    handleWarp({ target, transform }) {
      target.style.transform = transform;
    },
    focusAction({ target, transform }) {
      target.focus();
    },
    changeTargetAction({ target }) {
       this.blockMoveable();

      if(target.getAttribute('class') != null){
        var clas = target.getAttribute('class').split(' ');
      
        for(var cla in clas){
          // console.log(clas[cla]);
          if(clas[cla] == 'MoveableBox'){
            event.stopPropagation();
            target.blur();
            this.$refs.moveable.moveable.target = target;
          }

          if(clas[cla] == 'realBoard' || clas[cla] == 'bodyBox'){
            // event.stopPropagation();
            // target.blur();
            // this.$refs.moveable.moveable.target = target;
            this.cloakMoveable(); 
          }
        }
      }
    },
    deleteTargetAction(idx, { target }) {
      if (confirm("요소를 삭제하시겠습니까?") === true) {
        target.remove();
        this.board.isDelete = true;
        this.board.delete.moduleName = 'postit';
        this.board.delete.id = this.board.postitList[idx].frontPostitId;
        this.crudMethod('POSTIT', 'CREATE', this.board.postitList[idx])
        this.board.postitList.splice(idx, 1);
        this.sendMessage();
        this.cloakMoveable();
      }
    },
    wheelEvent: function(event) {
      if (event.deltaY < 0) { 
        console.log("up!"); 
        this.boardScale += 0.05;

        if(this.boardScale > 1.3) this.boardScale = 1.3;

        console.log(this.boardScale);
      }
      else if (event.deltaY > 0) {
        this.boardScale -= 0.05;

        if(this.boardScale < 0.3) this.boardScale = 0.3;

        console.log(this.boardScale);
         console.log("down!"); 
      }

      document.querySelector(".realBoard").style.transform = `scale(${this.boardScale})`;
      
    },
    deleteAction({target}) {
      if(confirm("요소를 삭제하시겠습니까?") === true) {
        if (target.getAttribute("class") != null) {
          var clas = target.getAttribute("class").split(" ");
          console.log(clas);
          for (var cla in clas) {
            if (clas[cla] == "scheduler") {
              this.board.scheduler = { "left": null, "top": null, "events": [{ "name": "오프라인", "content": "hello", "start": "2020-08-05T12:30:00", "end": "2020-08-05T18:00:00" }] }
              console.log(this.board.scheduler);
            } else if (clas == "Poll") {
              this.board.poll = {}
              this.isPoll = false;
            }
          }
        }
        target.remove();
        this.sendMessage();
        this.cloakMoveable();
      }
    },
    blockMoveable() {
      document.querySelector(".moveable-control-box").style.display = "block";
    },
    cloakMoveable() {
      document.querySelector(".moveable-control-box").style.display = "none";
    },
    moduleDragEnd(moduleName, {pageX, pageY}) {
      switch(moduleName) {
        case 'postit':
          this.createPostit(`${pageX}px`, `${pageY}px`);
          break;
        case 'scheduler':
          this.createScheduler(`${pageX}px`, `${pageY}px`);
          break;
        case 'poll':
          this.createPoll(`${pageX}px`, `${pageY}px`);
          break;
      }
    },
    pleaseDrag() {
      this.createSnackbar('생성하고자 하는 위치로 드래그 해주세요!', 3000, 'default')
    },
    crudMethod(moduleType, crudType, moduleObject) {
      this.board.crudModule =  {
        modulType: moduleType,
        crudType: crudType,
        moduleObject: moduleObject,
      }
    },

    testIn(){
      if(!this.memberView){
        this.memberView = true;
        console.log("hover!");
      }
    },
    testOut(){
      if(this.memberView){
        this.memberView = false;
        console.log("out!");
      }
    }
  },
  components: {
    Moveable,
    Postit,
    Map,
    Scheduler,
    Chat,
    Kanban,
    Poll,
  },
};
</script>

<style scoped>
.moveable {
  font-family: "Roboto", sans-serif;
  position: relative;
  width: 300px;
  height: 100px;
  text-align: center;
  font-size: 40px;
  margin: 0 auto;
  font-weight: 100;
  letter-spacing: 1px;
  /* background-color: yellow; */
}

.bodyBox {
  position: relative;
  height: 75vh;
  margin: 0;
  width: 100vw;
  /* transform: translate(-50%, -50%); */
  /* border: solid 1px; */
  background-color: rgb(255, 255, 255);
  overflow: hidden;
}

.realBoard {
  /* boardLength와 동일해야함! */
  /* height: 2000px;
  width: 2000px; */
  /* left: -680px;
  top: -680px; */
  border: 1px solid pink;
  background: rgb(247, 236, 236);

  background-image:     
     linear-gradient(
        0deg, transparent 0%, 
        transparent 0px, rgba(104, 104, 104, 0.1) 0px,rgba(104, 104, 104, 0.1) 1px, transparent 1px,
        transparent 49px, rgba(104, 104, 104, 0.1) 49px,rgba(104, 104, 104, 0.1) 50px, transparent 1px, 
        transparent 99px, rgba(104, 104, 104, 0.1) 99px,rgba(104, 104, 104, 0.1) 100px, transparent 1px, 
        transparent 149px, rgba(104, 104, 104, 0.1) 149px,rgba(104, 104, 104, 0.1) 150px, transparent 1px, 
        transparent 199px, rgba(104, 104, 104, 0.1) 199px,rgba(104, 104, 104, 0.1) 200px, transparent 1px, 
        transparent 249px, rgba(104, 104, 104, 0.3) 249px,rgba(104, 104, 104, 0.3) 250px, transparent 1px),

    linear-gradient(
        -90deg, transparent 0%, 
        transparent 0px, rgba(104, 104, 104, 0.1) 0px, rgba(104, 104, 104, 0.1) 1px, transparent 1px,
        transparent 49px, rgba(104, 104, 104, 0.1) 49px,rgba(104, 104, 104, 0.1) 50px, transparent 1px,
        transparent 99px, rgba(104, 104, 104, 0.1) 99px,rgba(104, 104, 104, 0.1) 100px, transparent 1px,
        transparent 149px, rgba(104, 104, 104, 0.1) 149px,rgba(104, 104, 104, 0.1) 150px, transparent 1px, 
        transparent 199px, rgba(104, 104, 104, 0.1) 199px,rgba(104, 104, 104, 0.1) 200px, transparent 1px, 
        transparent 249px, rgba(104, 104, 104, 0.3) 249px,rgba(104, 104, 104, 0.3) 250px, transparent 1px);
               
    background-size: 250px 250px;
  }


.moveable-control-box{
    display:none;
  }


.toolBox{
  font-family: "Roboto", sans-serif;
  /* position: relative; */
  position: fixed;
  z-index: 3;
  width: 64px;
  /* height: 1%; */
  /* transform: rotate(90deg); */
  bottom: 50%;
  left: 2%;
  /* left: -50px;; */
}

.toolbar{
  position: fixed;
  z-index: 3;
}

.userListBadge{
  position: fixed;
  z-index: 3;
  bottom: 1%;
  left: 20px;

  /* background-color: white; */
  /* border-radius: 50%; */
  width: 64px;
  height: 64px;
}

.vueBox{
  background-color: white;
  position: fixed;
  z-index: 3;
  right: 10%;
  bottom: 1%;
  width: 200px;
  height: 200px;
}

#memberList{
  width: auto;
  height: 40px;
  position: fixed;
  z-index: 3;
  bottom: 1%;
  left: 120px;
  /* padding: 5%; */
  /* vertical-align: middle; */
  /* background-color: olive; */
}



.slide-fade-enter-active {
  transition: all .4s ease;
}
.slide-fade-leave-active {
  transition: all .4s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}
.slide-fade-enter, .slide-fade-leave-to
/* .slide-fade-leave-active below version 2.1.8 */ {
  transform: translateX(-10px);
  opacity: 0;
}
</style>
