var Bomb = function (id, position) {
    this.id = id;
    var img = gGameEngine.asset.bomb;
    // todo this.strength = strength;

    // if you have got unusual bomb, put its size here
    var size = {
        // now i use this parameters
        w: 28,
        h: 28
    };

    var spriteSheet = new createjs.SpriteSheet({
        images: [img],
        frames: {
            width: size.w,
            height: size.h
        },
        animations: {
            // количество анимаций скорее всего тоже вручную вводить придется
            idle: [0, 4, "idle", 0.2]
        }
    });

    this.bmp = new createjs.Sprite(spriteSheet);

    if(this._properties.getAlign()) {
        this.bmp.regX = (size.w - this._properties.getAlignHeight()) / 2;
        this.bmp.regY = (size.h - this._properties.getAlignHeight()) / 2;
    }

    if(this._properties.getResizeTexture()) {
        this.bmp.scaleX = this._properties.getWidth() / size.w;
        this.bmp.scaleY = this._properties.getHeight()/ size.h;
    }

    this.bmp.x = position.x;
    this.bmp.y = position.y;

    // активация анимации
    this.bmp.gotoAndPlay('idle');
    // here we push to create element at screen
    gGameEngine.stage.addChild(this.bmp);
    // here we push to array of object, to use it in future
    gGameEngine.game.bombs.push(this);
};

Bomb.prototype._properties = new TextureProperty()
    .setAlign(true);

Bomb.prototype.remove = function() {
    gGameEngine.stage.removeChild(this.bmp);
};

Bomb.prototype.update = function () {
  // empty implementation
};
