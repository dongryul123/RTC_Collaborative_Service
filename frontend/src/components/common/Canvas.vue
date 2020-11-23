<template>
  <div :id="counter" class="canvas MoveableBox">
    <button @click="clicked">button</button>
  </div>
</template>

<script src="https://www.webrtc-experiment.com/Canvas-Designer/canvas-designer-widget.js"></script>
<script>
var designer = new CanvasDesigner();
export default {
  name: 'canvas',

  data() {
    return {
      is_canvas: false,
      counter: 1000
    }
  },
  created() {
    designer.widgetHtmlURL =
      "https://www.webrtc-experiment.com/Canvas-Designer/widget.html"; // you can place this file anywhere
    designer.widgetJsURL =
      "https://www.webrtc-experiment.com/Canvas-Designer/widget.js";
  },
  methods: {
    clicked() {
      if(!this.is_canvas) {
        this.is_canvas = true
        let a = document.getElementById(this.counter);
        console.log(a);
        a.style.background = 'skyblue';
        designer.appendTo(a);
        this.counter++;
      }
    }
  }
}
</script>

<style>
.canvas{
  height: 250px;
}
</style>