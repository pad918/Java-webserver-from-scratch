
function httpGetAsync(theUrl, callback)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.responseType = "arraybuffer";
    xmlHttp.onreadystatechange = function() { 
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
            callback(xmlHttp.response);
    }
    xmlHttp.open("GET", theUrl, true); // true for asynchronous 
    xmlHttp.send(null);
}

function responseHandler(data){
    console.log(typeof data);
    var uint8data = new Uint8Array(data);
    var b64Response = Base64.fromUint8Array(uint8data); //btoa(unescape(encodeURIComponent(data)));
    // create an image
    var outputImg = document.createElement('img');
    outputImg.src = 'data:image/png;base64,'+ b64Response;

    // append it to your page
    document.body.appendChild(outputImg)
    console.log("DATA B64: \n" + b64Response);
}


console.log("Sending...")
httpGetAsync("http://localhost/insek.png", responseHandler)
