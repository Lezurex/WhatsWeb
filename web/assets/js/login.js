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
                        window.location.href = "http://" + window.location.hostname + "/app";
                        break;
                    default:
                        alert("FALSCH!");
                }
            },
            error: function (error) {
                alert("FALSCH!");
            }
        })
    }
}

class Registration {
    static email = $("#registration-email");
    static username = $("#registration-name");
    static password = $("#registration-password");
    static passwordRepeat = $("#registration-password-repeat");
    static btn = $("#registration-btn");

}

$(document).ready(function () {
    $(Login.btn.on("click", function () {
        Login.queryLogin();
        Login.password.val(); // Gibt den Wert zurück

    }));
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