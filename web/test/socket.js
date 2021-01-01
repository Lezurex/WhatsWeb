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
    ResponseHandler.handleResponse(event.data);
}

class ResponseHandler {

    commandMap = {}

    constructor() {
        this.initFunctions()
    }

    handleResponse(message) {

    }

    initFunctions() {
       this.commandMap.add("login", function (message) {
           
       })
    }

}