const app = Vue.createApp({
    data() {
        return {
            chatSidebarElements: [
                new ChatSidebarElement("Test1", "abc"),
                new ChatSidebarElement("Test2", "abc")
            ],
            name: "Lenny",
        }
    },
    mounted() {
        console.log(this.chatSidebarElements);
    }
});