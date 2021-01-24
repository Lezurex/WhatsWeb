const app = Vue.createApp({
    data() {
        return {
            chatSidebarElements: [],
            appTitle: "WhatsWeb",
            currentChatElementId: null,
            currentGroupObject: null,
            uuid: data.uuid,
            updateKey: 0,
            cachedusers: {}
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
    computed: {

    },
    mounted() {
        commandSender.getFriends();
        commandSender.getGroups();
    }
});