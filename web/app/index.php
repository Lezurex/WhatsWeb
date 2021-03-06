<?php

require "../php/database/DatabaseAdapter.php";

session_start();
if (!isset($_SESSION['uuid'])) {
    header("Location: ../");
    exit();
}

$db = new DatabaseAdapter();
$token = $db->getStringFromTable("users", "token", new Key("uuid", $_SESSION['uuid']));

$json = json_encode(array(
    "token" => $token,
    "uuid" => $_SESSION['uuid']
), JSON_UNESCAPED_UNICODE);

?>

<!DOCTYPE html>
<html lang="de">
<head>
    <data id="login-data">
        <?=$json;?>
    </data>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" type="text/css" href="../assets/style/chat_style.css">
    <script src="../assets/js/chat.js"></script>
    <script src="https://unpkg.com/vue@next"></script>
    <title>Whatsweb</title>
</head>
<body>

<main id="main">
    <chatlistcomponent
            :apptitle="appTitle"
            :chatsidebarelements="chatSidebarElements" @loadchat="loadChat"></chatlistcomponent>
    <chat-view
            :group="currentGroupObject"
            :uuid="uuid"
            :key="updateKey" :cachedusers="cachedusers"></chat-view>
</main>
<script src="Main.js"></script>
<script src="components/ChatListComponent.js"></script>
<script src="components/ChatViewComponent.js"></script>

<script>const mountedApp = app.mount("#main")</script>

</body>
</html>