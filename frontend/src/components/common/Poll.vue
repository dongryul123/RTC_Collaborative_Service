<template>
  <div class="Pollx MoveableBox shadow-lg">
    <div class="poll-container" v-if="!poll.isSetAll & !poll.isEnd ">
      <div>
        <div class="poll-title" >
          <div class="d-flex">
            <v-icon large class="mr-2">mdi-vote</v-icon>
            <v-text-field single-line height=36 placeholder="투표 제목" v-model="poll.question" style="font-size:1.5rem"></v-text-field>
          </div>
        </div>
        <div class="poll-contents">
          <label class="label-25" for="poll-content">투표 항목</label>
          <div
            v-for="(answer, index) in poll.answers"
            :key="index"
            class="answer"
            :style="{zIndex: poll.answers.length - index}"
          >
          
            <div class="poll-content d-flex justify-content-between form-control">
              <input
                placeholder="항목 입력"
                v-model="poll.answers[index].answer"
                type="text"
              />
              <div class="d-flex" @click="deleteInput(index)">
                <v-icon style="cursor: pointer">mdi-minus-circle-outline</v-icon>
              </div>
            </div>
          </div>
          <button class="pressDownButton" @click="createNewInput()">
            <v-icon>mdi-plus-circle-outline</v-icon>
            <h3>항목 추가</h3>
          </button><hr>
        </div>
        <div class="poll-footer">
          <button class="circleScaleBtn" @click="reset"><span>리셋</span></button>
          <button class="circleScaleBtn" @click="save"><span>저장</span></button>
        </div>
      </div>
    </div>

    <div class="poll-container" v-if="poll.isSetAll & !poll.isEnd">
      <div>
        <div class="poll-title">
          <strong>Q. {{poll.question}} </strong>
          <hr />
        </div>
        <div class="poll-contents">
          
          <v-radio-group v-model="voted" v-if="!didYou">
            <v-radio
              v-for="(answer, index) in poll.answers"
              :key="index"
              :label="`${answer.answer}`"
              :value="index"
              class="form-control poll-content"
            ></v-radio>
          </v-radio-group>  

          <v-radio-group v-model="voted" v-if="didYou">
            <v-radio
              v-for="(answer, index) in poll.answers"
              :key="index"
              :label="`${answer.answer}`"
              :value="index"
              :disabled=true
              class="form-control"
            ></v-radio>
          </v-radio-group>  

          <hr>
        </div>
        <div class="poll-footer">
          <button class="circleScaleBtn" @click="vote"><span>투표</span></button>
          <button class="circleScaleBtn" @click="end"><span>투표종료</span></button>
        </div>
      </div>
    </div>


    <div class="poll-container" v-if="poll.isSetAll & poll.isEnd">
      <div>
        <div class="poll-title">
          <strong>Q.{{ poll.question }}</strong><hr />
        </div>
        <div class="poll-contents">
          <div
            v-for="(answer, index) in poll.answers"
            :key="index"
            class="answer form-control"
            :style="{zIndex: poll.answers.length - index}"
            style="margin-bottom: 10px;"
          >
            <div class="d-flex justify-content-between">
              <div>{{ answer.answer }}</div>
              <div>
                <v-icon>mdi-account</v-icon>
                {{ answer.voted }}
              </div>
            </div>
          </div>
        </div><hr>
        <div class="poll-footer">
          <h3 v-if="result.length == 0">No Result</h3>

          <h3 v-else> 
            <strong>A. </strong> 
            <h3 v-for="(answer, index) in result" 
            :key="index">
              <strong>{{answer.answer}} </strong>
            </h3>
          </h3>
          
        </div>
      </div>
    </div> 
  </div>
</template>

<script>
export default {
  props:{
    
  },
  data() {
    return {
      poll: this.$store.state.poll,
      test: [],
      voted: '',
      didYou: false,
      result: [],
    };
  },
  methods: {
    createNewInput() {
      if (this.poll.answers.length > 10) {
        alert('투표 문항은 10개까지 생성 가능합니다.')
      } else this.poll.answers.push({answer: "", voted: 0});
    },
    deleteInput(index) {
      if (index > 0 || this.poll.answers.length > 1) {
        this.poll.answers.splice(index, 1);
      }
    },
    reset() {
      this.$store.state.poll.quest = ""
      this.$store.state.poll.answers = [{answer: "", voted: 0}]
    },
    save() {
      //서버로 보내서 투표 저장
      this.poll.isSetAll = true;
    },
    vote() {
      if(this.didYou) {return;}
      this.poll.answers[this.voted].voted++;
      this.poll.userVoted.push({user: this.$store.state.email, voted:this.voted});
      // console.log(this.$store.state.email);
      this.didYou = true;
    },
    end(){
      this.poll.isEnd = true;
      let base = 0;
      let list = this.poll.answers

      for(var i=0; i<list.length; i++) {
        if(list[i].voted > base) {
          base = list[i].voted;
          this.result.push(list[i]); 
        }
      }

    },
  },
};
</script>
<style src="../../assets/css/my-component.css"></style>
<style scoped>

.Pollx{
  display: inline-block;
  position: absolute;
  background-color: white;
  padding: 15px;    
  box-shadow: .5rem 1rem 2rem rgba(0,0,0,.4)!important;
  border-radius: 5px;
}

.poll-container {
  width: 400px;
  font-size: 20px;
  padding: 5px;
}

.poll-title{
  font-size: 30px;
  padding: 10px;
}

.poll-contents{
  padding: 10px;
  /* border: 1px solid gray;
  border-radius: 2px;
  margin-bottom: 10px;
  padding: 3px; */
}
.poll-content {
  margin-bottom: 10px;
  transition: transform 250ms ease-in-out;
  height: 44px;
}
.poll-content:hover {
  transform: scale(1.05);
  border: 1px solid hsl(243, 80%, 62%);
}
.poll-footer {
  text-align: center;
  margin-bottom: 15px;
}

.poll-footer > button{
  /* margin-left: 5%;
  margin-right: 5%; */
  margin: 5px 15px;
}

h3{
  display: inline;
}

.label-25 {
  font-size: 25px; 
  margin-bottom: 5px;
}
</style>