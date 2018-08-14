// ***********************************************
// we can init in constructor here, because we
// don`t create an instance of loginWindow while
// page is loading
// ***********************************************

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

    this.bomb = null;
    this.fire = null;
    this.blown = true;
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

    this.toggleBomb();
};

LoginWindow.prototype.initStage = function () {
    this.loginCanvas = $("#loginCanvas");
    this.loginStage = new createjs.Stage("loginCanvas");
    this.updateStageSize();
    this.loginStage.enableMouseOver();

    var self = this;
    createjs.Ticker.addEventListener('tick', function (event) {
        self.update(event);
    });
};

LoginWindow.prototype.updateStageSize = function() {
    this.loginStage.canvas.width = this.loginCanvas.width();
    this.loginStage.canvas.height = this.loginCanvas.height();
};

LoginWindow.prototype.update = function(event) {
    this.loginStage.update();
};

LoginWindow.prototype.openRegister = function () {
    console.log("register opened");
    this.loginCard.css("display", "none");
    this.registerCard.css("display", "block");
    this.updateStageSize();

};

LoginWindow.prototype.openSignIn = function () {
    console.log("sign in opened");
    this.loginCard.css("display", "block");
    this.registerCard.css("display", "none");
    this.updateStageSize();
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

LoginWindow.prototype.toggleBomb = function () {
    if(this.bomb === undefined || this.bomb === null) {
        this.bomb = new Bomb(0, {x: this.loginWindow.width() / 2, y: 64}, textureManager.asset.bomb);
        this.bomb.bmp.regX += GM.getTileSize() / 2;
        this.bomb.bmp.regY += GM.getTileSize() / 2;
    }
    if (this.fire === null || this.fire === undefined) {
        this.fire = new Fire(0, {x: this.loginWindow.width() / 2, y: 64}, textureManager.asset.fire);
        this.fire.bmp.regX += GM.getTileSize() / 2;
        this.fire.bmp.regY += GM.getTileSize() / 2;
    }
    if(this.blown) {
        this.loginStage.addChild(this.bomb.bmp);
        this.loginStage.update();
        this.blown = false;
    } else {
        this.loginStage.removeChild(this.bomb.bmp);
        this.fire.animate();
        this.loginStage.addChild(this.fire.bmp);
        this.loginStage.update();
        this.blown = true;
    }
};

LoginWindow.prototype.signIn = function () {
    this.toggleBomb();
    GUI.loadingScreen.toggleWindow();

    // fire request
    console.log("signing in");
    var playerName = $("#inputUsername").val();
    var playerPassword = $("#inputPassword").val();
    var userData = {
        name: playerName,
        password: playerPassword
    };

    this.signInRequest.data = JSON.stringify(userData);
    var self = this;
    $.ajax(this.signInRequest).done(function(id) {
        console.log("signed In");
        GM.playerName = playerName;
        GM.playerPassword = playerPassword;
        self.toggleBomb();
        GUI.loadingScreen.toggleWindow();
    }).fail(function (jqXHR, textStatus) {
        alert("Failed to Sign In");
        self.toggleBomb();
        GUI.loadingScreen.toggleWindow();
    });
};

LoginWindow.prototype.register = function () {
    this.toggleBomb();
    GUI.loadingScreen.toggleWindow();

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
    var self = this;
    $.ajax(this.registerRequest).done(function(id) {
        console.log("This lobby id - " + id);
        GM.playerName = playerName;
        GM.playerPassword = playerPassword;
        self.toggleBomb();
        GUI.loadingScreen.toggleWindow();
    }).fail(function (jqXHR, textStatus) {
        alert("Failed to Register");
        self.toggleBomb();
        GUI.loadingScreen.toggleWindow();
    });
};