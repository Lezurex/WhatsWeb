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
      <div id="chat-content">
        {{ chatContent }}
      </div>
      <div id="chat-input-area">
        <label for="chat-input">Nachricht:</label>
        <input type="text" id="chat-input">
        <button id="chat-input-send-btn">Senden</button>
      </div>
      </div>`,
    data() {
        return {}
    },
    computed: {
        chatContent() {
            if (this.group !== null) {
                if (this.group.chat !== undefined) {
                    if (this.group.chat.messages !== null) {
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
                            }
                        });
                    }
                }
            }
        }
    }
})