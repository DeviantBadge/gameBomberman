var LoginWindow = function (clusterSettings) {
    this.loginBackground = $("#login-background");
    this.loginWindow = $("#loginWindow");
    this.disposed = false;
    this.initialized = false;

    this.signInRequest = {
        url: clusterSettings.signInUrl(),
        method: "POST",
        contentType: 'application/json',
        crossDomain: true,
        async: true
    };

    this.registerRequest = {
        url: clusterSettings.registerUrl(),
        method: "POST",
        contentType: 'application/json',
        crossDomain: true,
        async: true
    };
};

LoginWindow.prototype.openRegister = function () {
    console.log("register opened");
    $("#loginCard").css("display", "none");
    $("#registerCard").css("display", "block");
};

LoginWindow.prototype.openSignIn = function () {
    console.log("sign in opened");
    $("#loginCard").css("display", "block");
    $("#registerCard").css("display", "none");
};

LoginWindow.prototype.openWin = function () {
    if(this.disposed) {
        this.loginBackground.appendTo('#game');
        this.loginBackground.toggleClass("transparent");
        this.loginWindow.toggleClass('disposed-top');
        this.disposed = false;
    } else {
        this.disposed = true;
        this.loginBackground.toggleClass("transparent");
        this.loginWindow.toggleClass('disposed-top');
    }
};

LoginWindow.prototype.initialize = function () {
    if(this.initialized)
        return;
    this.initialized = true;
};

LoginWindow.prototype.signIn = function () {
    // fire request

    console.log("signing in");
    var playerName = $("#inputUsername").val();
    var playerPassword = $("#inputPassword").val();
    var userData = {
        name: playerName,
        password: playerPassword
    };

    this.signInRequest.data = JSON.stringify(userData);
    console.log(this.signInRequest);

    $.ajax(this.signInRequest).done(function(id) {
        console.log("signed In");
        gGameEngine.playerName = playerName;
        gGameEngine.playerPassword = playerPassword;
    }).fail(function (jqXHR, textStatus) {
        alert("Failed to Sign In");
    });
};

LoginWindow.prototype.register = function () {
    // fire request

    console.log("registering");
    var playerName = $("#newUsername").val();
    var playerPassword = $("#newUserPassword").val();
    var passwordCopy = $("#newUserPasswordCopy").val();
    var userData = {
        name: playerName,
        password: playerPassword,
        passwordCopy: passwordCopy
    };

    this.registerRequest.data = JSON.stringify(userData);
    console.log(this.registerRequest);

    $.ajax(this.registerRequest).done(function(id) {
        console.log("This lobby id - " + id);
        gGameEngine.playerName = playerName;
        gGameEngine.playerPassword = playerPassword;
    }).fail(function (jqXHR, textStatus) {
        alert("Failed to Register");
    });
};


var loginWin = null;