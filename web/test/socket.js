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
                        console.log(item);
                    })
            }
        }
    }

}

socket = new WebSocket("ws://localhost:2121");
let responseHandler = new ResponseHandler();

function send() {
    let message = document.getElementById("message").value
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