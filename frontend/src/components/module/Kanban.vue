<template>
  <kanban class="kanban">
    <!-- <div class="kanban MoveableBox"> -->
    <div class="flex justify-center">
      <div class="min-h-screen d-flex overflow-x-scroll py-12">

        <div
          v-for="column in this.$store.state.Kanban.columns"
          :key="column.columnTitle"
          class="bg-gray-100 rounded-lg px-3 py-3 column-width rounded mr-4"
        >
          <p class="text-gray-700 font-semibold font-sans tracking-wide text-sm">{{column.columnTitle}}</p>
          
          <draggable
            :list="column.tasks"
            :animation="200"
            style="margin-right:0"
            ghost-class="ghost-card"
            group="tasks"
          >
            <!-- Each element from here will be draggable and animated. Note :key is very important here to be unique both for draggable and animations to be smooth & consistent. -->

            <div v-for="(task,idx) in column.tasks"
             :key="idx" class="mt-3 cursor-move">
              <div class="bg-white shadow rounded px-3 pt-3 pb-5 border border-white">
                <div class="d-flex justify-space-between">
                  <p
                    class="text-truncate text-gray-700 font-semibold font-sans text-sm word-break:keep-all;"
                    style="word-break:keep-all; "
                    
                  >
                  {{task.taskTitle}}
                  {{idx}}
                  </p>
                 

                  <div>
                    <v-icon>mdi-pencil</v-icon>
                    <v-icon v-on:click="deleteTask(column.columnTitle,task)">mdi-delete</v-icon>
                  </div>
                </div>
              </div>
            </div>

          </draggable>
          <div v-on:click="addTask(column.columnTitle)">+</div>
        </div>
      </div>
    </div>
  </kanban>
</template>

<script>
import draggable from "vuedraggable";

export default {
  name: "App",
  components: {
    draggable,
  },
  props:{kanban:Object}
  ,
  data() {
    return {
      task: {
        taskTitle:"",
        taskContents:"",
        taskAssigner:"",
      },
      columns: [
        {
          columnTitle: "할 일",
          tasks: [],
        },
        {
          columnTitle: "진행중",
          tasks: [],
        },
        {
          columnTitle: "완료",
          tasks: [],
        },
      ],
    };
  },
  methods: {
    addTask(columnTitle) {
      const taskTitle = prompt(columnTitle, "");
      const taskContents = prompt(columnTitle, "");
      
      const newTask = {
                    taskTitle : taskTitle,
                    taskContents : taskContents,
                    taskAssigner : ""
             
                }
      this.columns.find((column) => column.columnTitle === columnTitle).tasks.push(newTask);
      this.$store.state.Kanban.columns.find((column) => column.columnTitle === columnTitle).tasks.push(newTask);
    },
    deleteTask(columnTitle, task) {
      
      var index = this.columns
        .find((column) => column.columnTitle === columnTitle)
        .tasks.indexOf(task);

      // var index = tasks.indexOf(task);

      this.columns
        .find((column) => column.columnTitle === columnTitle)
        .tasks.splice(index, 1);
      this.$store.state.Kanban.columns
        .find((column) => column.columnTitle === columnTitle)
        .tasks.splice(index, 1);
    },
  },
};
</script>

<style scoped>
.column-width {
  min-width: 320px;
  width: 320px;
  margin-right: 0;
}
/* Unfortunately @apply cannot be setup in codesandbox, 
but you'd use "@apply border opacity-50 border-blue-500 bg-gray-200" here */
.ghost-card {
  opacity: 0.5;
  background: #f7fafc;
  border: 1px solid #4299e1;
}
.kanban {
  border: 1px solid;
}
</style>
