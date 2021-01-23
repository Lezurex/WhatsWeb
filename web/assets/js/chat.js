class ResponseHandler {

    constructor() {
        this.commandMap = [];
        this.initFunctions()
    }

    handleResponse(message) {
        let json = JSON.parse(message);
        let command;
        if (Object.keys(json)[0] == "data") {
            command = json.data["command"];
            this.commandMap[command](json.data);
        } else if (Object.keys(json)[0] == "error") {
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
                case "getInfo":
                    let group = new Group(
                        message.members,
                        message.uuid,
                        message.chatUuid, // TODO update when server updated
                        message.admin,
                        message.name
                    );
            }
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

    getFriends() {
        this.sendRequest({
            subcommand: "get"
        }, "friends");
    }

    groups = {

    }

}

class SimpleUser {

    uuid;
    username;
    lastSeen;

    constructor(uuid, username, lastSeen) {
        this.uuid = uuid;
        this.username = username;
        this.lastSeen = lastSeen;
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
            socket.send('{"data":{"command":"group","subcommand":"get"}}');
        }
    }


    constructor(members, uuid, chat, admin, name) {
        this.members = members;
        this.uuid = uuid;
        this.chat = chat;
        this.admin = admin;
        this.name = name;
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
            socket.send('{"data":{"command":"chat","subcommand":"get"}}');
        }
    }
}

class ChatElement {

}

class ChatSidebarElement {
    name;
    uuid;

    constructor(name, uuid) {
        this.name = name;
        this.uuid = uuid;
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