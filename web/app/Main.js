const app = Vue.createApp({
    data() {
        return {
            chatSidebarElements: [
                new ChatSidebarElement("Test123", "uid", "group"),
                new ChatSidebarElement("Test1234", "uid", "private")
            ],
            appTitle: "WhatsWeb",
            currentChatElementId: null
        }
    },
    methods: {
        loadChat(index) {
            this.chatSidebarElements[index].selected = true;
            this.chatSidebarElements.forEach((element, i) => {
                if (i !== index) {
                    element.selected = false;
                }
            });
            let element = this.chatSidebarElements[index];
            this.currentChatElementId = index;
            if (element.type === "group") {

            }
        }
    },
    mounted() {
        commandSender.getFriends();
    }
});