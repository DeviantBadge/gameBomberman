// ***********************************************
// we can init in constructor here, because we
// don`t create an instance of loginWindow while
// page is loading
// ***********************************************

// todo or may be move this code to initialisation?
var LoginWindow = function () {
    this.loginBackground = null;
    this.loginWindow = null;
    this.loginCard = null;
    this.registerCard = null;
    this.disposed = false;
    this.initialized = false;

    this.loginCanvas = null;
    this.loginStage = null;

    this.signInRequest = null;

    this.registerRequest = null;
};

LoginWindow.prototype.initialize = function (clusterSettings) {
    if(this.initialized)
        return;
    this.initialized = true;

    this.loginBackground = $("#login-background");
    this.loginWindow = $("#loginWindow");
    this.loginCard = $("#loginCard");
    this.registerCard = $("#registerCard");

    this.loginCanvas = null;
    this.loginStage = null;
    this.initStage();

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

    this.initWindowAnimation();
};

LoginWindow.prototype.initStage = function () {
    this.loginCanvas = $("#loginCanvas");
    this.loginStage = new createjs.Stage("loginCanvas");
    this.loginStage.canvas.width = this.loginCanvas.width();
    this.loginStage.canvas.height = this.loginCanvas.height();
    this.loginStage.enableMouseOver();
};

LoginWindow.prototype.openRegister = function () {
    console.log("register opened");
    this.loginCard.css("display", "none");
    this.registerCard.css("display", "block");
};

LoginWindow.prototype.openSignIn = function () {
    console.log("sign in opened");
    this.loginCard.css("display", "block");
    this.registerCard.css("display", "none");
};

LoginWindow.prototype.toggleWindow = function () {
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

LoginWindow.prototype.initWindowAnimation = function () {
    var singleIcon = new createjs.Bitmap(textureManager.asset.pawn);
    var pawnIconSize = 48;
    singleIcon.sourceRect = new createjs.Rectangle(0, 0, pawnIconSize, pawnIconSize);
    singleIcon.x = 0;
    singleIcon.y = 0;
    this.loginStage.addChild(singleIcon);
    this.loginStage.update();
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
        GM.playerName = playerName;
        GM.playerPassword = playerPassword;
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
        GM.playerName = playerName;
        GM.playerPassword = playerPassword;
    }).fail(function (jqXHR, textStatus) {
        alert("Failed to Register");
    });
};