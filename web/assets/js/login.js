class Login {

    static email = $("#login-email");
    static password = $("#login-password");
    static btn = $("#login-btn");

    static queryLogin() {
        $.ajax({
            url: "http://" + window.location.hostname + "/php/login.php",
            data: {
                "email": this.email.val(),
                "password": this.password.val()
            },
            success: function (data, textStatus, xhr) {
                switch (xhr.status) {
                    case 200:
                        window.location.href = "http://" + window.location.hostname + "/app";
                }
            },
            error: function (error) {
                alert("FALSCH!");
            }
        })
    }
}

$(document).ready(function () {
    $(Login.btn.on("click", function () {
        Login.queryLogin();
        Login.password.val(); // Gibt den Wert zurück

    }));
})


