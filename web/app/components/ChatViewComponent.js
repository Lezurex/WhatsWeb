app.component("chat-view", {
    props: {
        group: {
            type: Group
        },
        uuid: {
            type: String
        }
    },
    template: `
      <div v-if="group !== null" id="chat-panel">
      <div id="chat-header">
        <h1 class="chat-h1">{{ group.name }}</h1>
      </div>
      <div id="chat-content" v-html="chatContent">
      </div>
      <div>{{group.chat}}</div>
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
            console.log("Chat content")
            if (this.group !== null) {
                if (this.group.chat !== undefined) {
                    console.log("Group chat not undefined")
                    if (this.group.chat.messages !== null) {
                        console.log("Group chat messages not null")
                        let chatContent = "";
                        chat.messages.forEach(messageElement => {
                            if (messageElement.author !== this.uuid) {
                                let user = SimpleUser.loadUser(messageElement.author);
                                chatContent += `<div class="message message-left">
                                    <div>
                                        <h1>` + user.username + `</h1>
                                    </div>
                                    <div>
                                        ` + messageElement.content + `
                                    </div>
                                </div>`
                            } else {
                                let user = SimpleUser.loadUser(messageElement.author);
                                chatContent += `<div class="message message-right">
                                    <div>
                                        <h1>` + user.username + `</h1>
                                    </div>
                                    <div>
                                        ` + messageElement.content + `
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
    }
})