const app = Vue.createApp({
    data() {
        return {
            chatSidebarElements: [
                new ChatSidebarElement("Test123", "uid"),
                new ChatSidebarElement("Test1234", "uid")
            ],
            appTitle: "WhatsWeb"
        }
    },
    mounted() {
        commandSender.getFriends();
    }
});