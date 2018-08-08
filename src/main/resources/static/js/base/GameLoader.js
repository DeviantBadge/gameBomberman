var GameLoader = function () {

};

GameLoader.prototype.loadAll = function () {
    textureManager.load();
};

gameLoader = new GameLoader();