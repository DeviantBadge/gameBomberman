var LoadingScreen = function () {
    this.initialized = false;
    this.disposed = false;
    this.loadingCanvas = null;
    this.loadingStage = null;
};

LoadingScreen.prototype.initialize = function () {
    if(this.initialized)
        return;
    this.initialized = true;

    this.initStage();
    this.drawBackground();
    this.initBoy();
    this.activateListener(this.loadingStage);
    this.toggleWindow();
};

LoadingScreen.prototype.initStage = function () {
    this.loadingCanvas = $("#loadingScreen");
    this.loadingStage = new createjs.Stage("loadingScreen");
    this.loadingStage.canvas.width = this.loadingCanvas.width();
    this.loadingStage.canvas.height = this.loadingCanvas.height();
    this.loadingStage.enableMouseOver();
};

LoadingScreen.prototype.activateListener = function (stage) {
    // todo centralise that
    createjs.Ticker.addEventListener('tick', function () {
        stage.update();
    });
};

LoadingScreen.prototype.toggleWindow = function () {
    if(this.disposed) {
        this.loadingCanvas.css("display", "block");
        this.disposed = false;
    } else {
        this.loadingCanvas.css("display", "none");
        this.disposed = true;
    }
};

LoadingScreen.prototype.initBoy = function () {
    var pawn = new Player(0, {x: 0, y: 0}, textureManager.asset.pawn);
    pawn.animate("right");
    this.loadingStage.addChild(pawn.bmp);
    this.loadingStage.update();
    console.log(this.loadingStage.canvas.width);
    console.log(this.loadingStage.canvas.height);
};

LoadingScreen.prototype.drawBackground = function () {
    var canvasRect = new createjs.Graphics()
        .beginFill("rgba(0, 0, 0, 0.5)")
        .drawRect(0, 0, this.loadingStage.canvas.width, this.loadingStage.canvas.height);

    var background = new createjs.Shape(canvasRect);
    this.loadingStage.addChild(background);
};