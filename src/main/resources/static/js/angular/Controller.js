app.controller("Ctrl", function ($scope) {
    var gameField = $("#game");
    gameField.css('width' , gCanvas.getWidthInPixel() + 'px', 'important');
    gameField.css('height', gCanvas.getHeightInPixel() + 'px', 'important');

    // todo init gui here in another function
    loginWin = new LoginWindow(gClusterSettings);
    loginWin.initialize();
});