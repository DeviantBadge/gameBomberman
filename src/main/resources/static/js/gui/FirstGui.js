var FirstGui = function () {
    // gui list init

};

FirstGui.prototype.loadStage = function () {
    // gui list elements init
    var canvas = $("#loginCanvas");
    var stage = new createjs.Stage("loginCanvas");
    // todo hahahhahahahhah :(
    stage.canvas.width = canvas.width();
    stage.canvas.height = canvas.height();
    stage.enableMouseOver();
    return stage;
};

FirstGui.prototype.load = function () {
    this.stage = FirstGui.prototype.loadStage();

    var singleIcon = new createjs.Bitmap(gGameEngine.asset.pawn);
    var pawnIconSize = 48;
    singleIcon.sourceRect = new createjs.Rectangle(0, 0, pawnIconSize, pawnIconSize);
    singleIcon.x = 0;
    singleIcon.y = 0;
    this.stage.addChild(singleIcon);
    this.stage.update();

    loadScr = new LoadingScreen();
    loadScr.initialize();
    loadScr.deactivate();
};