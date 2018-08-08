var CanvasSettings = function () {
    this.tileSize = 32;
    this._tiles = {
        w: 27,
        h: 17
    };
};

CanvasSettings.prototype.getWidthInPixel = function () {
    return this._tiles.w * this.tileSize;
};

CanvasSettings.prototype.getWidthInTiles = function () {
    return this._tiles.w;
};

CanvasSettings.prototype.getHeightInPixel = function () {
    return this._tiles.h * this.tileSize;
};

CanvasSettings.prototype.getHeightInTiles = function () {
    return this._tiles.h;
};

gCanvas = new CanvasSettings();
