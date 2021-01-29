# WhatsWeb
Simple messaging webapp similar to WhatsApp.

## Features
- Login
- Registration
- Gruppennachrichten versenden
- Gruppennachrichten empfangen

## Funktionsweise
Der Client ist in JavaScript mithilfe des Vue.JS Frameworks geschrieben worden. Der Server wurde mit Java und dem [Java WebSocket](https://github.com/TooTallNate/Java-WebSocket) geschrieben. Der Client verbindet sich per WebSocket zum Server, um dort Abfragen in Echtzeit auszuführen. Ebenfalls kann so der Server jederzeit eine Nachricht an den Client senden.
