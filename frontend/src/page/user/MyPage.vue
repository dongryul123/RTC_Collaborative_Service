<template>
    <div class="user" id="myPage">
        <div class="myBox">
            <div class="middle ">
                <h2>회원 정보 페이지</h2>
                <div class="form-wrap">
                    <img src="https://dummyimage.com/350x350 " alt=""/>
                    <div class="Info">
                        <div class="info-wrap">
                            <label for="email">이메일 : </label>
                            <input id="email" type="text" v-model="this.$store.getters.userData.email" readonly="readonly">
                        </div>

                        <div class="info-wrap">
                            <label for="nickName">닉네임 : </label>
                            <input v-model="this.$store.getters.userData.nickname"
                                id="nickName"
                                type="text" readonly="readonly"
                                />
                        </div>
                        <div class="info-wrap">
                            <label for="realName">이름 : </label>
                            <input v-model="this.$store.getters.userData.name" readonly="readonly"
                                id="realName"
                                type="text"/>
                        </div>
                        <div class="info-wrap">
                             <!-- <button v-on:click="updateUser">회원정보 수정</button> -->
                        </div>
                        <div class="info-wrap">
                            <router-link v-bind:to="{name:constants.URL_TYPE.USER.DELETEUSER}" class="btn--text btn--warn">
                                회원 탈퇴
                            </router-link> 
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
</template>



<script>
    import '../../assets/css/user.scss'
    import constants from '../../lib/constants'
    
    export default {
        components: {
        },
        created(){
        },
        methods: {
            
        },
        computed:{
            
        },
        watch: {
        },
        data: () => {
            return {
                passwordConfirm: '',
                isTerm: false,
                passwordType:"password",
                passwordConfirmType:"password",
                constants
            }
        }

    }

</script>
