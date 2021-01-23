const sidebarComponent = app.component('chatlistcomponent', {
    name: "ChatListComponent",
    props: {
        chatsidebarelements: {
            type: Array
        },
        apptitle: {
            type: String
        }
    },
    template: `<aside id="chats-list-panel">
        <div id="chats-header">
            <h1 class="chat-h1">{{ apptitle }}</h1>
        </div>
        <ul id="chats-list">
            <li v-for="listElement in chatsidebarelements" :key="listElement.uuid">{{ listElement.name }}</li>
        </ul>
    </aside>`,
    data() {
        return {
            bool: true,
        }
    },
    methods: {

    },
})