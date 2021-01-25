<?php

require "database/DatabaseAdapter.php";

session_start();

$db = new DatabaseAdapter();

$username = $_POST['username'];
$password = $_POST['password'];
$email = $_POST['email'];

if (
    $db->containsEntry("users",
        new Key("username", $username)) or
    $db->containsEntry("users",
        new Key("email", $email))) {
    echo "400";
    exit();
}

$uuid = gen_uuid();
$token = gen_uuid();
$hash = password_hash($password, PASSWORD_DEFAULT);

$db->insertIntoTable("users",
    new Insert("uuid", $uuid),
    new Insert("username", $username),
    new Insert("password", $hash),
    new Insert("email", $email),
    new Insert("friends", "[]"),
    new Insert("token", $token)
);

$_SESSION['uuid'] = $uuid;
echo "200";

function gen_uuid()
{
    return sprintf('%04x%04x-%04x-%04x-%04x-%04x%04x%04x',
        // 32 bits for "time_low"
        mt_rand(0, 0xffff), mt_rand(0, 0xffff),

        // 16 bits for "time_mid"
        mt_rand(0, 0xffff),

        // 16 bits for "time_hi_and_version",
        // four most significant bits holds version number 4
        mt_rand(0, 0x0fff) | 0x4000,

        // 16 bits, 8 bits for "clk_seq_hi_res",
        // 8 bits for "clk_seq_low",
        // two most significant bits holds zero and one for variant DCE1.1
        mt_rand(0, 0x3fff) | 0x8000,

        // 48 bits for "node"
        mt_rand(0, 0xffff), mt_rand(0, 0xffff), mt_rand(0, 0xffff)
    );
}