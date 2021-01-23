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
            this.chatSidebarElements.forEach((element, i) => {
                element.selected = false;
            });
            this.chatSidebarElements[index].selected = true;

            let element = this.chatSidebarElements[index];
            this.currentChatElementId = index;
            if (element.type === "group") {
                Group.loadGroup(element.uuid);
            }
        }
    },
    mounted() {
        commandSender.getFriends();
    }
});