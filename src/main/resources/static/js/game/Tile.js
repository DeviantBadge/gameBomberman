var Tile = function (id, position, material) {
    this.id = id;
    var img;

    switch (material) {
        case 'Wall':
            img = gGameEngine.asset.tile.wall;
            break;
        case 'Wood':
            img = gGameEngine.asset.tile.wood;
            break;
        default:
            break;
    }

    this.bmp = new createjs.Bitmap(img);

    if(this._properties.getAlign()) {
        this.bmp.regX = (img.width - this._properties.getAlignHeight()) / 2;
        this.bmp.regY = (img.height - this._properties.getAlignHeight()) / 2;
    }

    if(this._properties.getResizeTexture()) {
        this.bmp.scaleX = this._properties.getWidth() / img.width;
        this.bmp.scaleY = this._properties.getHeight()/ img.height;
    }

    this.bmp.x = position.x;
    this.bmp.y = position.y;

    gGameEngine.stage.addChild(this.bmp);
    gGameEngine.game.tiles.push(this);
};

Tile.prototype._properties = new TextureProperty()
    .setAlign(true);

Tile.prototype.remove = function () {
    gGameEngine.stage.removeChild(this.bmp);
};

Tile.prototype.update = function() {
    // empty implementation
};

// todo это касается всех объектов, можно попробовать их все таки инициализировать красиво, не дублируя везде код