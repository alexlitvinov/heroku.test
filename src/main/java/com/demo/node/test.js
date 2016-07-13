/**
 * 
 * @type type
 */
var phantom = require('phantom');

var jq = require("jquery");

var express = require('express');

var app = express();

var jqueryd = require('jquery-deferred');

var Spooky = require('spooky');

//sample of page
app.get('/main', function (req, res) {
    res.writeHead(200, {'Content-Type': 'text/html'});
    res.end('<html>' +
            '<body>' +
            '<font color="green"><h3>Service for making png image from html DOM</h3></font>' +
            '</body>' +
            '</html>');
});

//getting BASE64 encoded image
app.get("/getbase64", function (req, res) {

    var sitepage = null;
    var phInstance = null;    

    function waitFor(testFx, onReady, timeOutMillis) {
        var maxtimeOutMillis = timeOutMillis ? timeOutMillis : 3000, 
                start = new Date().getTime(),
                condition = false,
                interval = setInterval(function () {
                    if ((new Date().getTime() - start < maxtimeOutMillis) && !condition) {                        
                        condition = (typeof (testFx) === "string" ? eval(testFx) : testFx());
                    } else {
                        if (!condition) {
                        
                            console.log("'waitFor()' timeout");
                            phantom.exit(1);
                        } else {                        
                            console.log("'waitFor()' finished in " + (new Date().getTime() - start) + "ms.");
                            typeof (onReady) === "string" ? eval(onReady) : onReady(); 
                            clearInterval(interval); 
                        }
                    }
                }, 250);
    }
    ;

    phantom.create()
            .then(function (instance) {
                phInstance = instance;
                return instance.createPage();
            })
            .then(function (page) {
                sitepage = page;
                return page.open('http://localhost:3000/template.html');
            })
            .then(function (status) {
                
                sitepage.property('onConsoleMessage', function (msg) {
                    console.log('get MESSAGE ' + msg);
                });

                waitFor(function () {
                    return sitepage.evaluate(function () {
                        return $("#canvacontent").is(":visible");
                    });
                }, function () {
                    var data = sitepage.evaluate(function () {
                        var text = $("#canvacontent").html();

                        return text;
                    }).then(function (text) {
                        sitepage.close();
                        phInstance.exit();
                        res.writeHead(200, {'Content-Type': 'text/plain'});
                        res.end(text);
                    });
                });
            });
});

app.use(express.static(__dirname + '/../../../../resources/static'));

app.listen(3000, function () {
    console.log('Example app listening on port 3000!');
});
