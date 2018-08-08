var LoadingScreen = function () {
    this.initialized = false;

};

LoadingScreen.prototype.loadStage = function () {
    var stage = new createjs.Stage("loadingScreen");
    stage.canvas.width = this.loadingCanvas.width();
    stage.canvas.height = this.loadingCanvas.height();
    stage.enableMouseOver();
    var self = this;
    // todo centralise that
    createjs.Ticker.addEventListener('tick', function () {
        stage.update();
    });
    return stage;
};

LoadingScreen.prototype.initBoy = function () {

    var pawn = new Player(0, {x: 0, y: 0}, gGameEngine.asset.pawn);
    pawn.animate("right");
    this.stage.addChild(pawn.bmp);
    this.stage.update();
    console.log(this.stage.canvas.width);
    console.log(this.stage.canvas.height);
};

LoadingScreen.prototype.drawBackground = function () {
    var canvasRect = new createjs.Graphics()
        .beginFill("rgba(0, 0, 0, 0.5)")
        .drawRect(0, 0, this.stage.canvas.width, this.stage.canvas.height);

    var background = new createjs.Shape(canvasRect);
    this.stage.addChild(background);
};

LoadingScreen.prototype.initialize = function () {
    if(this.initialized)
        return;
    this.initialized = true;
    this.loadingCanvas = $("#loadingScreen");
    this.stage = this.loadStage();
    this.drawBackground();
    this.initBoy();
    console.log(this.stage.canvas.width);
    console.log(this.stage.canvas.height);
};

LoadingScreen.prototype.activate = function () {
    this.loadingCanvas.css("display", "block");
};

LoadingScreen.prototype.deactivate = function () {
    this.loadingCanvas.css("display", "none");
};

var loadScr = null;