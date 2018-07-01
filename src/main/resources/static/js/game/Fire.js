var Fire = function (id, position) {
    this.id = id;
    var img = gGameEngine.asset.fire;


    // if you have got unusual fire, put its size here
    var size = {
        // now i use this parameters
        w: 38,
        h: 38
    };

    var spriteSheet = new createjs.SpriteSheet({
        images: [img],
        frames: {
            width: size.w,
            height: size.h
        },
        animations: {
            // количество анимаций скорее всего тоже вручную вводить придется
            idle: [0, 5, 'idle', 0.4]
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
    gGameEngine.game.fires.push(this);

    // удаление элемента после анимации
    var self = this;
    this.bmp.addEventListener('animationend', function() {
        self.remove();
    });
};

Fire.prototype._properties = new TextureProperty()
    .setAlign(true);

Fire.prototype.remove = function () {
    gGameEngine.stage.removeChild(this.bmp);
};

Fire.prototype.update = function() {
    // empty implementation
};
