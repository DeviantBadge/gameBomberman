var CanvasSettings = function () {
    this.tileSize = 32;
    this.tiles = {
        w: 27,
        h: 17
    };
};

CanvasSettings.prototype.getWidthInPixel = function () {
    return this.tiles.w * this.tileSize;
};

CanvasSettings.prototype.getWidthInTiles = function () {
    return this.tiles.w;
};

CanvasSettings.prototype.getHeightInPixel = function () {
    return this.tiles.h * this.tileSize;
};

CanvasSettings.prototype.getHeightInTiles = function () {
    return this.tiles.h;
};

gCanvas = new CanvasSettings();
