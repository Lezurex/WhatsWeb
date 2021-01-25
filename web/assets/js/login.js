class Login {

    static email = $("#login-email");
    static password = $("#login-password");
    static btn = $("#login-btn");

    static queryLogin() {
        $.ajax({
            method: "post",
            url: "http://" + window.location.hostname + "/php/login.php",
            data: {
                "email": this.email.val(),
                "password": this.password.val()
            },
            success: function (data, textStatus, xhr) {
                switch (data) {
                    case "200":
                        window.location.href = window.location.origin + "/app";
                        break;
                    default:
                        alert("FALSCH!");
                }
            },
            error: function (error) {
                alert("FALSCH!");
            }
        });
    }
}

class Registration {
    static email = $("#registration-email");
    static username = $("#registration-name");
    static password = $("#registration-password");
    static passwordRepeat = $("#registration-password-repeat");
    static btn = $("#registration-btn");

    static queryRegistration() {
        $.ajax({
            method: "post",
            url: "http://" + window.location.hostname + "/php/register.php",
            data: {
                "email": this.email.val(),
                "username": this.username.val(),
                "password": this.password.val()
            },
            success: function (data, textStatus, xhr) {
                switch (data) {
                    case "200":
                        window.location.href = window.location.origin + "/app";
                        break;
                    default:
                        alert("Es ist ein Fehler aufgetreten!");
                }
            },
            error: function (error) {
                alert("Es ist ein Fehler aufgetreten!");
            }
        });
    }

    static allFieldsFilled() {
        let count = 0;
        if (this.email.val() !== "")
            count++;
        if (this.username.val() !== "")
            count++;
        if (this.password.val() !== "")
            count++;
        if (this.passwordRepeat.val() !== "")
            count++;

        if (count === 4) {
            return true;
        }
        return false;
    }

}

$(document).ready(function () {
    Login.btn.on("click", function () {
        Login.queryLogin();
        Login.password.val(); // Gibt den Wert zurück

    });

    Registration.btn.on("click", function () {
        if (Registration.allFieldsFilled()) {
            if (Registration.password.val() === Registration.passwordRepeat.val()) {
                Registration.queryRegistration();
            } else
                alert("Die Passwörter stimmen nicht überein!");
        } else
            alert("Es müssen alle Felder ausgefüllt sein!");
    })
})

    let x = document.getElementById("forgot");
    x.style.display = "none";

    x = document.getElementById("registration");
    x.style.display = "none";
function toggleRegistration() {
    let x = document.getElementById("registration");
    if (x.style.display === "none") {
        x.style.display = "block";
        x = document.getElementById("login");
        x.style.display = "none";
    } else {
        x.style.display = "none";
        x = document.getElementById("login");
        x.style.display = "block";
    }
}
function togglePasswordReset() {
    let x = document.getElementById("forgot");
    if (x.style.display === "none") {
        x.style.display = "block";
        x = document.getElementById("login");
        x.style.display = "none";
    } else {
        x.style.display = "none";
        x = document.getElementById("login");
        x.style.display = "block";
    }
}