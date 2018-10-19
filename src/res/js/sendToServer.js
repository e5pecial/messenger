var d = document;
var username = null;
var password = null;

function handler()
{
    console.log(this.status, this.readyState, Date.now());
    if (this.readyState == 4)
    {
        var request = null;
        if (this.status == 200)
        {
            request = new XMLHttpRequest;
            request.open("GET", "users.html", true);
            request.setRequestHeader("Content-Type", "html/text");
            request.send(null);

            // куки на час
            setCookie("username", username, 1);
            setCookie("password", password, 1);

            d.location.pathname = "/users.html";
        }
        else if (this.status != 200)
        {
            location.reload();
        }
    }
}

function registration()
{
    username = d.getElementById("username").value;
    password = d.getElementById("password").value;

    var request = new XMLHttpRequest;
    request.onreadystatechange = handler;
    request.open("POST", "?action=reg", true);
    var message = "username=" + encodeURIComponent(username) + "&" +
            "password=" + encodeURIComponent(password);
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    request.send(message);
}

function auth()
{
    username = d.getElementById("username").value;
    password = d.getElementById("password").value;

    var request = new XMLHttpRequest;
    request.onreadystatechange = handler;
    request.open("POST", "?action=auth", true);
    var message = "username=" + encodeURIComponent(username) + "&" +
    		"password=" + encodeURIComponent(password);
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.send(message);
}

function sendMessage()
{
    var userFrom = getCookie("username");
    var userTo = getCookie("userto");
    var message = null;
    var request = null;
    if (userFrom != null && userTo != null)
    {
        userFrom = encodeURIComponent(userFrom);
        userTo = encodeURIComponent(userTo);

        message = d.getElementById("message").value;
        message = encodeURIComponent(message);

        request = new XMLHttpRequest();
        request.open("POST", "?action=letter", true);
        message = "userfrom=" + userFrom + "&" +
                "userto=" + userTo + "&" +
                "letter=" + message;
        request.send(message);
        d.getElementById("message").value = "";


    }
    else
    {
        alert("Вы не выбрали собеседника");
    }
}

function leaveTalk()
{
    deleteCookie("userto");

    d.location.pathname = "/users.html";
}

function startTalk(userName)
{
    setCookie("userto", userName, 1);
    d.location.pathname = "/chat.html";
}

function setCookie(name, value, time)
{
    var d = null;
    if (typeof time == "number" && time)
    {
        d = new Date();
        d.setTime(d.getTime()  + time * 60 * 60 * 1000);
    }
    var expires = "expires="+d.toUTCString();

    value = encodeURIComponent(value);

    document.cookie = name + "=" + value + ";" + expires;
}

function deleteCookie(name)
{
    setCookie(name, "", -1);
}

function getCookie(_name)
{
    var name = _name + "=";
    var ca = d.cookie.split(';');
    for(var i = 0; i < ca.length; ++i)
    {
        var c = ca[i];
        while (c.charAt(0) == ' ' )
        {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0)
        {
            return c.substring(name.length, c.length);
        }
    }
    return null;
}

function blockChat() {
    if (document.cookie == "") {
        d.location.pathname = "/auth.html";
    }
    else
    {
        d.location.pathname = "/chat.html";
    }
}

function blockProfile() {
    if (document.cookie == "") {
        d.location.pathname = "/auth.html";
    }
    else
    {
        d.location.pathname = "/profile.html";
    }
}

function authExit() {
    deleteCookie("username");
    deleteCookie("password");
    d.location.pathname = "/index.html"

}

function blockUsers(){
    if (document.cookie == "") {
        d.location.pathname = "/auth.html";
    }
    else
    {
        d.location.pathname = "/users.html";
    }

}
