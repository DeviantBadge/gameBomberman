var CasualParameters = function () {
    this.playerAmount = 2;
    this.gameType = "casual";
};

CasualParameters.prototype.reset = function (amount) {
    this.playerAmount = amount;
    return this;
};

CasualParameters.prototype.reset = function () {
    this.playerAmount = 2;
    return this;
};


