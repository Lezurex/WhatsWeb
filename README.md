# WhatsWeb
Einfache Messagin-Webapp

## Features
- Login
- Registration
- Gruppennachrichten versenden
- Gruppennachrichten empfangen

## Funktionsweise
Der Client ist in JavaScript mithilfe des [Vue.js Frameworks](https://github.com/vuejs/vue) geschrieben worden. Der Server wurde mit Java und dem [Java WebSocket](https://github.com/TooTallNate/Java-WebSocket) geschrieben. Der Client verbindet sich per WebSocket zum Server, um dort Abfragen in Echtzeit auszuführen. Ebenfalls kann so der Server jederzeit eine Nachricht an den Client senden.
