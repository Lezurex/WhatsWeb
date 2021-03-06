<?php

session_start();

if (isset($_SESSION['uuid'])) {
    header("Location: ./app");
    exit();
}

?>

<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" type="text/css" href="assets/style/style.css">
    <title>WhatsWeb</title>
    <!-- JQUERY -->
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"
            integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
</head>
<body>
<h1>WhatsWeb</h1>
<div class="form-area" id="login">
    <div class="form-card">
        <h2>Login</h2>
        <label for="login-email">E-Mail</label>
        <input type="email" id="login-email">
        <label for="login-password">Passwort</label>
        <input type="password" id="login-password">
        <button id="login-btn">Anmelden</button>
        <button onclick="togglePasswordReset()">Passwort vergessen?</button>
        <br>
        <button onclick="toggleRegistration()">registriere dich hier</button>
    </div>
</div>
<div class="form-area" id="registration">
    <div class="form-card">
        <h2>Registrieren</h2>
        <label for="registration-name">Benutzername</label>
        <input type="text" id="registration-name">
        <label for="registration-email">E-Mail</label>
        <input type="email" id="registration-email">
        <label for="registration-password">Passwort</label>
        <input type="password" id="registration-password">
        <label for="registration-password-repeat">Passwort wiederholen</label>
        <input type="password" id="registration-password-repeat">
        <button id="registration-btn">Registrieren</button>
    </div>
</div>
<div class="form-area" id="forgot">
    <div class="form-card">
        <h2>Passwort vergessen</h2>
        <p>Gib deine E-Mailadresse ein</p>
        <label for="login-email">E-Mail</label>
        <input type="email" id="forgotpassword-email">
        <input type="submit" value="senden" onclick="togglePasswordReset()"/>
    </div>
</div>
<script src="assets/js/login.js"></script>
</body>
</html>