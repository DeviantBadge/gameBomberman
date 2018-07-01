var Bonus = function (id, position, type) {
    this.id = id;
    var img;

    switch (type) {
        case 'SPEED':
            img = gGameEngine.asset.bonus.speed;
            break;
        case 'BOMBS':
            img = gGameEngine.asset.bonus.bombs;
            break;
        case 'RANGE':
            img = gGameEngine.asset.bonus.explosion;
            break;
        default:
            console.error('Smth was wrong with bonus types!');
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
    gGameEngine.game.bonuses.push(this);
};

Bonus.prototype._properties = new TextureProperty()
    .setAlign(true);

Bonus.prototype.remove = function() {
    gGameEngine.stage.removeChild(this.bmp);
};

Bonus.prototype.update = function() {
    // empty implementation
};
