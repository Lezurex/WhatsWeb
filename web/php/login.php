<?php

require "database/DatabaseAdapter.php";

session_start();

$db = new DatabaseAdapter();

$email = $_POST['email'];

if ($db->containsEntry("users", new Key("email", $email))) {
    $hashed_password = $db->getStringFromTable("users", "password", new Key("email", $email));
    $password = $_POST['password'];
    if (password_verify($password, $hashed_password)) {
        $uuid = $db->getStringFromTable("users", "uuid", new Key("email", $email));
        $_SESSION['uuid'] = $uuid;
        $token = gen_uuid();
        $db->updateValue("users", "token", $token, new Key("uuid", $uuid));
        echo "200";
    } else {
        echo "401";
    }
} else {
    echo "404";
}

function gen_uuid() {
    return sprintf( '%04x%04x-%04x-%04x-%04x-%04x%04x%04x',
        // 32 bits for "time_low"
        mt_rand( 0, 0xffff ), mt_rand( 0, 0xffff ),

        // 16 bits for "time_mid"
        mt_rand( 0, 0xffff ),

        // 16 bits for "time_hi_and_version",
        // four most significant bits holds version number 4
        mt_rand( 0, 0x0fff ) | 0x4000,

        // 16 bits, 8 bits for "clk_seq_hi_res",
        // 8 bits for "clk_seq_low",
        // two most significant bits holds zero and one for variant DCE1.1
        mt_rand( 0, 0x3fff ) | 0x8000,

        // 48 bits for "node"
        mt_rand( 0, 0xffff ), mt_rand( 0, 0xffff ), mt_rand( 0, 0xffff )
    );
}