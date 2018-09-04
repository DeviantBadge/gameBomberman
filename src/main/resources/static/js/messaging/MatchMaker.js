var MatchMaker = function (clusterSetting) {
    this.clusterSettings = clusterSetting;
};

MatchMaker.prototype.getSettings = function (parameters) {
    return {
        url: this.clusterSettings.matchMakerUrl() + parameters.gameType,
        method: "POST",
        crossDomain: true,
        async: false
    };
};

MatchMaker.prototype.getSessionId = function (parameters) {
    switch (parameters.gameType) {
        case "casual":
        case "ranked":
            break;
        default:
            console.error("Unknown game type - " + parameters.gameType);
            return;
    }
    GM.playerName = Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
    var settings = this.getSettings(parameters);
    settings.data = "name=" + GM.playerName;

    var sessionId = -1;
    $.ajax(settings).done(function(id) {
        sessionId = id;
        console.log("This lobby id - " + id);
    }).fail(function (jqXHR, textStatus) {
        alert("Matchmaker request failed");
    });

    return sessionId;
};

gMatchMaker = new MatchMaker(gClusterSettings);