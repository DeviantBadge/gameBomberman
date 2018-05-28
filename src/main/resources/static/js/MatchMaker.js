var MatchMaker = function (clusterSetting) {
    this.settings = {
        url: clusterSetting.matchMakerUrl(),
        method: "POST",
        crossDomain: true,
        async: false
    };
};

MatchMaker.prototype.getSessionId = function () {
    gGameEngine.playerName = Math.floor((1 + Math.random()) * 0x10000)
        .toString(16)
        .substring(1);
    console.log(gGameEngine.playerName);
    var name = "name=" + gGameEngine.playerName;

    this.settings.data = name;
    var sessionId = -1;
    $.ajax(this.settings).done(function(id) {
        sessionId = id;
        console.log("This lobby id - " + id);
    }).fail(function (jqXHR, textStatus) {
        alert("Matchmaker request failed");
    });

    return sessionId;
};

gMatchMaker = new MatchMaker(gClusterSettings);