var TextureProperty = function () {
    this._align = false;
    this._alignWidth = gCanvas.tileSize;
    this._alignHeight = gCanvas.tileSize;

    this._resizeTexture = false;
    this._width = gCanvas.tileSize;
    this._height = gCanvas.tileSize;
};

TextureProperty.prototype.setAlign = function (bool) {
    this._align = bool;
    return this;
};
TextureProperty.prototype.getAlign = function () {
    return this._align;
};

TextureProperty.prototype.setResizeTexture = function (bool) {
    this._resizeTexture = bool;
    return this;
};
TextureProperty.prototype.getResizeTexture = function () {
    return this._resizeTexture;
};

TextureProperty.prototype.setAlignWidth = function (integer) {
    this._alignWidth = integer;
    return this;
};
TextureProperty.prototype.getAlignWidth = function () {
    return this._alignWidth;
};

TextureProperty.prototype.setAlignHeight = function (integer) {
    this._alignHeight = integer;
    return this;
};
TextureProperty.prototype.getAlignHeight = function () {
    return this._alignHeight;
};

TextureProperty.prototype.setWidth = function (integer) {
    this._width = integer;
    return this;
};
TextureProperty.prototype.getWidth = function () {
    return this._width;
};

TextureProperty.prototype.setHeight = function (integer) {
    this._height = integer;
    return this;
};
TextureProperty.prototype.getHeight = function () {
    return this._height;
};