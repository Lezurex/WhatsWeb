app.component("chat-view", {
    name: "ChatView",
    props: {
        group: {
            type: Group
        },
        uuid: {
            type: String
        },
        cachedusers: {
            type: Object
        }
    },
    template: `
      <div v-if="group !== null" id="chat-panel">
      <div id="chat-header">
        <h1 class="chat-h1">{{ group.name }}</h1>
      </div>
      <div id="chat-content" v-html="chatContent">
      </div>
      <div id="chat-input-area">
        <label for="chat-input">Nachricht:</label>
        <input type="text" id="chat-input">
        <button id="chat-input-send-btn" @click="sendMessage">Senden</button>
      </div>
      </div>`,
    data() {
        return {}
    },
    methods: {
        sendMessage() {
            let message = document.getElementById("chat-input").value;
            commandSender.sendMessage(this.group.uuid, message);
        }
    },
    computed: {
        chatContent() {
            if (this.group instanceof Object) {
                console.log(this.group)
                if (this.group.chat instanceof Object) {
                    console.log("Group chat not undefined")
                    if (this.group.chat.messages !== null) {
                        let chat = this.group.chat;
                        console.log("Group chat messages not null")
                        let chatContent = "";
                        chat.messages.forEach(messageElement => {
                            let user = messageElement.author;

                            if (messageElement.author.uuid !== this.uuid) {
                                chatContent += `<div class="message message-left">
                                    <div>
                                        <h1>` + user.username + `</h1>
                                        <div>
                                        ` + messageElement.content + `
                                    </div>
                                    </div>
                                </div>`
                            } else {
                                chatContent += `<div class="message message-right">
                                    <div>
                                        <h1>` + user.username + `</h1>
                                        <div>
                                        ` + messageElement.content + `
                                    </div>
                                    </div>
                                </div>`
                            }
                        });
                        return chatContent;
                    }
                }
            }
            return "<span style='display: block; padding-top: 5rem; text-align: center; width: 100%;'>Keine Nachrichten</span>"
        }
    },
    mounted() {

    }
})