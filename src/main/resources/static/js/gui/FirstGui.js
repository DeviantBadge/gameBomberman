var FirstGui = function () {
    this.loginWindow = null;
    this.loadingScreen = null;
};

FirstGui.prototype.load = function () {
    this.loginWindow = new LoginWindow();
    this.loginWindow.initialize(gClusterSettings);

    this.loadingScreen = new LoadingScreen();
    this.loadingScreen.initialize();
};

GUI = new FirstGui();