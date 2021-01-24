class ResponseHandler {

    constructor() {
        this.commandMap = [];
        this.initFunctions()
    }

    handleResponse(message) {
        let json = JSON.parse(message);
        let command;
        if (Object.keys(json)[0] === "data") {
            command = json.data["command"];
            this.commandMap[command](json.data);
        } else if (Object.keys(json)[0] === "error") {
            console.log(json.error);
        }
    }

    initFunctions() {
        this.commandMap["login"] = function (message) {

        }
        this.commandMap["friends"] = function (message) {
            switch (message.subcommand) {
                case "get":
                    message['friends'].forEach((item) => {

                    })
            }
        }
        this.commandMap['group'] = function (message) {
            switch (message.subcommand) {
                case "getByID":
                    let group = new Group(
                        message.members,
                        message.uuid,
                        message.chatuuid,
                        message.admin,
                        message.name
                    );
                    mountedApp.currentGroupObject = group;
                    break;
                case "getGroups":
                    message['groups'].forEach(group => {
                        let simpleGroup = new SimpleGroup(group.uuid, group.name, group.lastMessage);
                        mountedApp.chatSidebarElements.push(new ChatSidebarElement(simpleGroup.name, simpleGroup.uuid, "group"));
                    })

            }
        }
        this.commandMap['chat'] = function (message) {
            switch (message.subcommand) {
                case "getChatWithRange":
                    let chat = new Chat(message.uuid)
                    message.messages.forEach(messageElement => {
                        let chatElement = new ChatElement(
                            messageElement.author,
                            messageElement.content,
                            messageElement.timestamp,
                            messageElement.uuid
                        );
                        chat.addElement(chatElement);
                    });
                    let group = Group.loadGroup(chat.uuid);
                    group.chat = chat;
                    mountedApp.currentGroupObject = group;
                    break;
            }
        }
        this.commandMap['user'] = function (message) {
            switch (message.subcommand) {
                case "get":
                    let simpleUser = new SimpleUser(message.uuid, message.username, message.lastSeen);
                    SimpleUser.loadedUsers[simpleUser.uuid] = simpleUser;
            }
        }
        this.commandMap['update'] = function (message) {
            let group = Group.loadGroup(message.uuid);
            group.chat.addElement(new ChatElement(message.message.author, message.message.content, message.message.timestamp, message.message.uuid));
            mountedApp.currentGroupObject = group;
        }
    }
}

class CommandSender {

    sendRequest(data, command) {
        let obj = {
            data: {
                command: command
            }
        };
        for (const [key, value] of Object.entries(data)) {
            obj.data[key] = value;
        }
        send(JSON.stringify(obj));
    }

    getGroups() {
        this.sendRequest({
            subcommand: "getGroups"
        }, "group");
    }

    getGroup(uuid) {
        this.sendRequest({
            subcommand: "getByID",
            uuid: uuid
        }, "group");
    }

    getChat(uuid, range) {
        this.sendRequest({
            subcommand: "getChatWithRange",
            uuid: uuid,
            range: range
        }, "chat")
    }

    getFriends() {
        this.sendRequest({
            subcommand: "get"
        }, "friends");
    }

    getUser() {
        this.sendRequest({
            subcommand: "get"
        }, "user");
    }

    sendMessage(groupUUID, message) {
        this.sendRequest({
            groupUUID: groupUUID,
            message: message,
            subcommand: "sendMessage"
        }, "group");
    }


}

class SimpleUser {

    static loadedUsers = {};

    uuid;
    username;
    lastSeen;

    constructor(uuid, username, lastSeen) {
        this.uuid = uuid;
        this.username = username;
        this.lastSeen = lastSeen;
    }

    static loadUser(uuid) {
        if (this.loadedUsers[uuid] !== undefined) {
            return this.loadedUsers[uuid];
        } else {
            commandSender.getUser(uuid);
        }
    }
}

class SimpleGroup {
    uuid;
    name;
    lastMessage;

    constructor(uuid, name, lastMessage) {
        this.uuid = uuid;
        this.name = name;
        this.lastMessage = lastMessage;
    }
}

class Group {

    static loadedGroups = {};

    members = [];
    uuid;
    chat; // Chat Object
    admin; // SimpleUser
    name;

    static loadGroup(uuid) {
        if (this.loadedGroups[uuid] !== undefined) {
            return this.loadedGroups[uuid];
        } else {
            commandSender.getGroup(uuid);
        }
    }

    constructor(members, uuid, chatUuid, admin, name) {
        this.members = members;
        this.uuid = uuid;
        this.admin = admin;
        this.name = name;

        this.chat = Chat.loadChat(chatUuid);

        Group.loadedGroups[this.uuid] = this;
    }
}

class Chat {
    static loadedChats = {};

    messages = []; // List with ChatElements
    uuid;

    static loadChat(uuid) {
        if (this.loadedChats[uuid] !== undefined) {
            return this.loadedChats[uuid];
        } else {
            commandSender.getChat(uuid, 100);
        }
    }

    constructor(uuid) {
        Chat.loadedChats[uuid] = this;
        this.uuid = uuid;
        console.log("New chat " + uuid);
    }

    addElement(chatElement) {
        this.messages.push(chatElement);
        mountedApp.currentGroupObject = Group.loadGroup(this.uuid);
    }
}

class ChatElement {
    author;
    content;
    timestamp;
    uuid;

    constructor(author, content, timestamp, uuid) {
        this.author = new SimpleUser();
        this.content = content;
        this.timestamp = timestamp;
        this.uuid = uuid;
    }
}

class ChatSidebarElement {
    name;
    uuid;
    type;
    selected = false;

    constructor(name, uuid, type) {
        this.name = name;
        this.uuid = uuid;
        this.type = type;
    }
}

socket = new WebSocket("ws://localhost:2121");
let responseHandler = new ResponseHandler();
let commandSender = new CommandSender();

function send(message) {
    socket.send(message);
    console.log("OUT: " + message);
}

function status() {
    console.log("Status: " + socket.readyState);
}

socket.onopen = function (event) {
    console.log("Socket opened");
    let message = {
        "data": {
            "command": "login",
            "uuid": "b0d43c22-6392-4237-8619-e75b5ed41f9f",
            "token": "token"
        }
    }
    socket.send(JSON.stringify(message));
    console.log("Sent login")
}

socket.onmessage = function (event) {
    responseHandler.handleResponse(event.data);
}

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+ d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

setCookie("uuid", "b0d43c22-6392-4237-8619-e75b5ed41f9f", 0)