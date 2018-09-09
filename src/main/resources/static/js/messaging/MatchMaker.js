var MatchMaker = function (clusterSetting) {
    this.clusterSettings = clusterSetting;
};

MatchMaker.prototype.getSettings = function (parameters) {
    return {
        url: this.clusterSettings.matchMakerUrl() + parameters.gameType,
        method: "POST",
        contentType: 'application/json',
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
    parameters.credentials = GM.credentials;
    var settings = this.getSettings(parameters);
    settings.data = JSON.stringify(parameters);

    var sessionId = null;
    $.ajax(settings).done(function(response) {
        sessionId = response.successMessage;
        console.log("MM response - " + response);
    }).fail(function (jqXHR, textStatus) {
        var response = jqXHR.responseJSON;
        if(response === undefined) {
            response = jqXHR.responseText;
            if(response === undefined) {
                alert("Matchmaker request failed.");
            } else {
                alert(response)
            }
        } else {
            alert("Matchmaker request failed.\n" +
                "Error message: " + response.errorMessage + "\n" +
                "Probable solution: " + response.solution);
        }
    });

    return sessionId;
};

gMatchMaker = new MatchMaker(gClusterSettings);