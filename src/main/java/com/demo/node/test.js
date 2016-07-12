var dispatcher = require('httpdispatcher');
var http = require('http');
var log4js = require('log4js');
var logger = log4js.getLogger();
var phantom = require('phantom');
var express=require('express');

dispatcher.onGet("/page1", function (req, res) {
    console.log('222222222222222222');
    res.writeHead(200, {'Content-Type': 'text/html'});
    res.end('<html>' +
            '<body>' +
            '<font color="green"><h3>HELLO HTML</h3></font>' +
            '</body>' +
            '</html>');
});

dispatcher.onGet("/getbase64", function (req, res) {
    console.log('2rrrrrr');


    phantom.create().then(function (ph) {
        ph.createPage().then(function (page) {
            page.open('http://localhost:1337/page1').then(function (status) {
                console.log(status);
                
                //page.includeJs('http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js', function(err) {
      
                page.property('content').then(function (content) {
                    console.log(content);
                    page.close();
                    ph.exit();
                    res.writeHead(200, {'Content-Type': 'text/html'});
                    res.end('<html>' +
                            '<body>' +
                            '<font color="green"><h3>ALL IS OK</h3></font>' +
                            '</body>' +
                            '</html>');
                });
            });
        });
    });

});

dispatcher.onError(function (req, res) {
    res.writeHead(404);
});

http.createServer(function (req, res) {
    dispatcher.dispatch(req, res);
}).listen(1337, '127.0.0.1');