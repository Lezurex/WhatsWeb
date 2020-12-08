socket = new WebSocket("ws://localhost:2121");

function send() {
    let message = document.getElementById("message").value
    socket.send(message);
    console.log("OUT: " + message);
}

function status() {
    console.log("Status: " + socket.readyState);
}

socket.onopen = function (ev) {
    console.log("Socket opened");
    socket.send("Login");
}