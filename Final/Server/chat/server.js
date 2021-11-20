var net = require('net');
var port = 5050;
var clients = [];

var directs = require('./directs.js');
var users = require('./users.js');

// Start a TCP Server
net.createServer(function (socket) {

    // We have a connection - a socket object is assigned to the connection automatically
    socket.key = socket.remoteAddress + ':' + socket.remotePort;
    socket.id = null;
    clients.push(socket);
    console.log('CONNECTED: ' + socket.key);

    // Add a 'data' event handler to this instance of socket
    socket.on('data', function (data) {
        var commandParts = ('' + data).trim().split(':');
        var response = {action:null,code:0,error:null,data:null};
        if (commandParts.length > 1) {
            var controller = commandParts[0].toLowerCase();
            var action = commandParts[1].toLowerCase();
            response.action = controller + ':' + action;
            switch (controller) {
                // CHANNELS
                case 'channels':
                    break;
                // DIRECTS
                case 'directs':
                    switch (action) {
                        // DIRECTS:MESSAGES
                        case 'messages':
                            if (commandParts.length > 4) {
                                directs.messages(socket, commandParts[2], commandParts[3], commandParts[4]);
                            } else {
                                response.error = 'Insufficient parameters';
                                socket.write(JSON.stringify(response) + '\n');
                            }
                            break;
                        // DIRECTS:SEND
                        case 'send':
                            if (commandParts.length > 6) {
                                directs.send(socket, clients, commandParts[2], commandParts[3], commandParts[4], commandParts[5], commandParts[6]);
                            } else {
                                response.error = 'Insufficient parameters';
                                socket.write(JSON.stringify(response) + '\n');
                            }
                            break;
                        default:
                            response.error = 'Invalid Action';
                            socket.write(JSON.stringify(response) + '\n');
                            break;
                    }
                    break;
                // GROUPS
                case 'groups':
                    break;
                // USERS
                case 'users':
                    switch (action) {
                        // USERS:GETCHANNELS
                        case 'getchannels':
                            if (commandParts.length > 3) {
                                users.getchannels(socket, commandParts[2], commandParts[3]);
                            } else {
                                response.error = 'Insufficient parameters';
                                socket.write(JSON.stringify(response) + '\n');
                            }
                            break;
                        // USERS:GETDIRECTS
                        case 'getdirects':
                            if (commandParts.length > 3) {
                                users.getdirects(socket, commandParts[2], commandParts[3]);
                            } else {
                                response.error = 'Insufficient parameters';
                                socket.write(JSON.stringify(response) + '\n');
                            }
                            break;
                        // USERS:GETGROUPS
                        case 'getgroups':
                            if (commandParts.length > 3) {
                                users.getgroups(socket, commandParts[2], commandParts[3]);
                            } else {
                                response.error = 'Insufficient parameters';
                                socket.write(JSON.stringify(response) + '\n');
                            }
                            break;
                        // USERS:GETNAME
                        case 'getname':
                            if (commandParts.length > 2) {
                                users.getname(socket, commandParts[2]);
                            } else {
                                response.error = 'Insufficient parameters';
                                socket.write(JSON.stringify(response) + '\n');
                            }
                            break;
                        // USERS:LOGIN
                        case 'login':
                            if (commandParts.length > 3) {
                                users.login(socket, commandParts[2], commandParts[3]);
                            } else {
                                response.error = 'Insufficient parameters';
                                socket.write(JSON.stringify(response) + '\n');
                            }
                            break;
                        // USERS:REGISTER
                        case 'register':
                            if (commandParts.length > 4) {
                                users.register(socket, commandParts[2], commandParts[3], commandParts[4]);
                            } else {
                                response.error = 'Insufficient parameters';
                                socket.write(JSON.stringify(response) + '\n');
                            }
                            break;
                        // OTHER ACTIONS
                        default:
                            response.error = 'Invalid Action';
                            socket.write(JSON.stringify(response) + '\n');
                            break;
                    }
                    break;
                // OTHER CONTROLLERS
                default:
                    response.error = 'Invalid Controller';
                    socket.write(JSON.stringify(response) + '\n');
                    break;
            }
        } else {
            response.error = 'Insufficient parameters';
            socket.write(JSON.stringify(response) + '\n');
        }
    });

    // Add a 'close' event handler to this instance of socket
    socket.on('close', function (data) {
        console.log('CLOSED: ' + socket.key);
        var index = -1;
        for (i in clients) {
            if (clients[i].key == socket.key) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            clients.splice(index, 1);
        }
    });

    // Add an 'error' event handler to this instance of socket
    socket.on('error', function (data) {
        console.log('ERROR: ' + socket.key + ' (' + data + ')');
        var index = -1;
        for (i in clients) {
            if (clients[i].key == socket.key) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            clients.splice(index, 1);
        }
    });
}).listen(port);

// Put a friendly message on the terminal of the server
console.log('Server is running at port ' + port + '\n');