/**
 * Node js service 
 * render html template into png image
 * @type base64 data png image
 */
var phantom = require('phantom');

var jq = require("jquery");

var express = require('express');

var app = express();

var mu = require('mu2');

//sample of page
app.get('/welcome', function (req, res) {
    var stream = mu.compileAndRender('welcome.html', {});
    stream.pipe(res);
});

//view template
app.get("/template", function (req, res) {
    var name = req.query.name;
    var data = decodeURIComponent(req.query.data);
    var stream = mu.compileAndRender(name + '.html', JSON.parse(data));
    stream.pipe(res);
});

//getting BASE64 encoded image
app.get("/getbase64", function (req, res) {
    generate(200, res);
    /*var phInstance;
     * 
     * 
     
     var location = 'http://localhost:3000/';
     
     var sitepage = null;
     
     var templateName = req.query.templateName;
     
     var width = req.query.width;
     
     var height = req.query.height;
     
     var data = decodeURIComponent(req.query.data);
     
     if (!templateName || !width || !height || !data) {
     res.writeHead(500, {'Content-Type': 'application/json'});
     res.end("{\"res\":\"err\", \"err\": \"not all params present. \"}");
     return;
     }
     phantom.create().then(function (instance) {
     phInstance = instance;
     return instance.createPage();
     }).then(function (page) {
     sitepage = page;
     console.log(data);
     return page.open(location + 'template?name=' + templateName + '&data=' + encodeURIComponent(data));
     })
     .then(function (status) {
     
     if (status !== 'success') {
     res.writeHead(500, {'Content-Type': 'application/json'});
     res.end("{\"res\":\"err\", \"err\": \"can not open template\"}");
     sitepage.close();
     return;
     }
     
     sitepage.property('onConsoleMessage', function (msg) {
     console.log('get MESSAGE ' + msg);
     });
     
     try {
     sitepage.property('viewportSize', {width: width, height: height});
     sitepage.property('clipRect', {top: 0, left: 0, width: width, height: height});
     
     
     sitepage.render('/home/master/templates/singleTemplate' + JSON.parse(data).subicon + '.PNG', 10).then(function () {
     res.writeHead(200, {'Content-Type': 'text/plain'});
     res.end("ok");
     sitepage.close();
     phInstance.exit();
     })
     } catch (re) {
     console.log(re);
     }
     });*/
});
//where statiic 
app.use(express.static(__dirname + '/../resources/static'));

//specify dir with templates
mu.root = __dirname + '/../resources/templates';

//start app
app.listen(3000, function () {
    console.log('Example app listening on port 3000!');
});

function generate(cnt, res) {
    try {
        if (cnt <= 0) {
            res.writeHead(200, {'Content-Type': 'text/plain'});
            res.end("ok");
            return;
        }
        else {
            var phInstance;

            var location = 'http://localhost:3000/';

            var sitepage = null;


            var width = 475;

            var height = 250;

            phantom.create().then(function (instance) {
                phInstance = instance;
                return instance.createPage();
            }).then(function (page) {
                sitepage = page;
                return page.open(location + 'template?name=template&data=' + encodeURIComponent("{\"subicon\":" + cnt + "}"));
            })
                    .then(function (status) {
                        console.log(status);
                        if (status !== 'success') {
                            return;
                        }

                        sitepage.property('onConsoleMessage', function (msg) {
                            console.log('get MESSAGE ' + msg);
                        });

                        try {
                            sitepage.property('viewportSize', {width: width, height: height});
                            sitepage.property('clipRect', {top: 0, left: 0, width: width, height: height});

                            sitepage.render('/home/master/templates111/singleTemplate' + cnt + '.PNG', {format: "PNG", quality:10}).then(function () {
                                sitepage.close();
                                phInstance.exit();
                                generate(cnt - 1);
                            })
                        } catch (re) {
                            console.log(re);
                        }
                    });


        }
    } catch (eee) {
        console.log(eee);

    }
}