var dispatcher = require('httpdispatcher');
var http = require('http');
var log4js = require('log4js');
var logger = log4js.getLogger();
var phantom = require('phantom');
var $ = require("jquery");

var express = require('express');
var app = express();

app.get('/page1', function (req, res) {
    console.log('222222222222222222');
    res.writeHead(200, {'Content-Type': 'text/html'});
    res.end('<html>' +
            '<body>' +
            '<font color="green"><h3>HELLO HTML</h3></font>' +
            '</body>' +
            '</html>');
});


app.get("/getbase64", function (req, res) {
    console.log('2rrrrrr');


    phantom.create().then(function (ph) {
        ph.createPage().then(function (page) {
            page.open('http://localhost:3000/page1').then(function (status) {
                console.log(status);

                page.includeJs('http://localhost:3000/html2canvas.js', function () {
                    console.log('succesfully img1111 ');


                    page.evaluate(function () {
                        /*html2canvas(document.body, {
                         onrendered: function (canvas) {
                         console.log('succesfully img');
                         
                         }
                         });*/
                        console.log(document.getElementsByTagName('html')[0].innerHTML);
                        //page.close();

                        res.writeHead(200, {'Content-Type': 'text/html'});
                        res.end('<html>' +
                                '<body>' +
                                '<font color="green"><h3>ALL IS OK</h3></font>' +
                                '</body>' +
                                '</html>');

                    });

                });
                ph.exit();

            });
        });

    });

});
app.use(express.static(__dirname + '/../../../../resources/static'));

//app.use(__dirname +express.static('static'));

app.listen(3000, function () {
    console.log('Example app listening on port 3000!');
});
