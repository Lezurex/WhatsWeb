const sidebarComponent = app.component('chatlistcomponent', {
    name: "ChatListComponent",
    props: {
        chatSidebarElements: {
            type: Array
        }
    },
    template: `<aside id="chats-list-panel">
        <div id="chats-header">
            <h1 class="chat-h1">WhatsWeb</h1>
        </div>
        <ul id="chats-list">
            <li v-for="listElement in chatSidebarElements">{{ listElement.name }}</li>
        </ul>
    </aside>`,
    data() {
        return {
            bool: true,
        }
    },
    methods: {

    },
    mounted() {
        console.log(this.chatSidebarElements);
    }

})