<template ref="outline">
    <div class="post" id="board">
        <button @click="test">Add Button</button>
        <div class="container" ref="whiteBoard">

            <Moveable
            class="moveable"
            v-bind="moveable"
            @drag="handleDrag"
            @resize="handleResize"
            @scale="handleScale"
            @rotate="handleRotate"
            @warp="handleWarp"
            >

              <div  @dblclick="clickEv" ref="contentTextArea" name="" id="asdfasdf" cols="30" rows="3">
              11
              </div>
            </Moveable>

            <!-- <Moveable
            class="moveable"
            v-bind="moveable"
            @drag="handleDrag"
            @resize="handleResize"
            @scale="handleScale"
            @rotate="handleRotate"
            @warp="handleWarp"
            >
              <textarea  @dblclick="clickEv" ref="contentTextArea" name="" id="asdfasdf" cols="30" rows="3">
                asdf
              </textarea> 
            </Moveable> -->
        </div>
    </div>
</template>

<script>
import Moveable from 'vue-moveable';

const postItDiv = `<div class="moveable" style="left: 365px; top: 113px;"><textarea name="" id="asdfasdf" cols="30" rows="3"> asdfa </textarea></div>`
const pI = `<div data-scena-element="true" data-scena-element-id="scena23050442" style="top: 22.5px; left: -114px; position: absolute; width: 498px; height: 279px; background-color: rgb(68, 170, 255); transform: translate(-483px, -254px) rotate(0deg) scale(1, 1);"></div>`

export default {
  name: 'app',
  components: {
    Moveable,
  },
  created() {
  },
  data: () => ({
    moveable: {
      draggable: true,
      throttleDrag: 1,
      resizable: true,
      throttleResize: 1,
      keepRatio: false,
      scalable: false,
      throttleScale: 0,
      rotatable: true,
      throttleRotate: 0,
      origin: false
    },
    tt: 'aasdf',
    // imageSourse:{
    //     [
    //         {thumnail: ''}
    //     ]
    // }

  }),
  methods: {
    handleDrag({ target, left, top }) {
    //   console.log('onDrag left, top', left, top);
      target.style.left = `${left}px`;
      target.style.top = `${top}px`;
    },
    handleResize({
      target, width, height, delta,
    }) {
      console.log('onResize', width, height , delta);
      delta[0] && (target.style.width = `${width}px`);
      delta[1] && (target.style.height = `${height}px`);
    },
    handleScale({ target, transform, scale }) {
    //   console.log('onScale scale', scale);
      target.style.transform = transform;
    },
    handleRotate({ target, dist, transform }) {
    //   console.log('onRotate', dist);
      target.style.transform = transform;
    },
    handleWarp({ target, transform }) {
    //   console.log('onWarp', target);
      target.style.transform = transform;
    },  
    clickEv({ target, transform }){
        console.log('click!')
        console.log(target.innerHTML);
        target.focus();
    },
    lining({}){
      
    },
    test(){
      this.$refs.whiteBoard.innerHTML += pI;
      // console.log(this.$refs.whiteBoard.innerHTML += pI)
    }
  },
}
</script>

<style>
.moveable {
  font-family: "Roboto", sans-serif;
  position: relative;
  width: 300px;
  height: 200px;
  text-align: center;
  font-size: 40px;
  margin: 0 auto;
  font-weight: 100;
  letter-spacing: 1px;
}

.moveable span {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  white-space: nowrap;
}

.container {
  position: relative;
  height: 500px;
  width: 80vw;
  /* transform: translate(-50%, -50%); */
  border: solid 1px;
}

img{
    height: 300px;
    width: 300px;
}

textarea {
    height: 100%;
    width: 100%;
    background-color: yellow;
}
</style>