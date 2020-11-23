<template>
  <div>
    <v-container fluid>
      <div class="d-flex justify-content-between">
        <h2>나의 모임목록</h2>
        <div class="d-flex">
          <v-text-field placeholder="채널 제목" v-model="channel_name"></v-text-field>
          <button @click="createChannel" class="circleScaleBtn"><span>채널 생성</span></button>
        </div>
      </div>
      <v-row>
        <v-col
          v-for="card in channels"
          :key="card.channelId"
          cols="4"
          md="3"
          @click="enterRoom(card.channelId, card.channelName)"
        >
          <v-card class="card-channel">
            <v-img
              :src="getRandomImage(card.channelName)"
              class="white--text align-end"
              gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
              height="300px"
            >
              <v-card-title v-text="card.channelName"></v-card-title>
            </v-img>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>
 
<script>
import "../../assets/css/post.scss";
import constants from "../../lib/constants";
import SockJS from "sockjs-client";
import Stomp from "stomp-websocket";
import axios from "axios";
import http from "../../http-common.js";

export default {
  data: () => ({
    channel_name: "",
    channels: [],
  }),
  created() {
    this.findAllChannel();
  },
  methods: {
    getRandomImage(idString) {
      return `https://picsum.photos/seed/${idString}/200/300`;
    },
    findAllChannel: function () {
      http.get("/board/channels").then((response) => {
        // prevent html, allow json array
        if (Object.prototype.toString.call(response.data) === "[object Array]")
          this.channels = response.data;
      });
    },
    createChannel: function () {
      if ("" === this.channel_name) {
        alert("채널 이름을 입력해 주십시오.");
        return;
      } else {
        var params = new URLSearchParams();
        params.append("channelName", this.channel_name);
        http
          .post("/board/channel", params)
          .then((response) => {
            alert(response.data.channelName + "채널 개설에 성공하였습니다.");
            this.channel_name = "";
            this.findAllChannel();
          })
          .catch((response) => {
            alert("채널 개설에 실패하였습니다.");
          });
      }
    },
    enterRoom: function (channelId, channelName) {
      localStorage.setItem("wsboard.channelId", channelId);
      localStorage.setItem("wsboard.channelName", channelName);
      location.href = "/channel/" + channelId;
    },
  },
};
</script>

<style src="../../assets/css/my-component.css"></style>
<style scoped>
.card-channel {
  cursor: pointer;
  transition: opacity 2s ease-in-out;
}
.card-channel :hover {
  opacity: 0.6;
}
</style>