var mysql = require('mysql');
var db = mysql.createConnection({
//  debug    : ['ComQueryPacket', 'RowDataPacket'],
    host     : 'localhost',
    user     : 'root',
    password : 'ncis',
    database : 'chat',
});
db.query ("SET NAMES 'utf8mb4';");
var response = {action:null,code:0,error:null,data:null};
module.exports = {
    // MESSAGES
    messages: function (socket, user, pass, directId) {
        response.action = 'directs:messages';
        db.query (mysql.format ('SELECT `id` FROM `users` WHERE (LOWER(`user`)=? AND `pass`=?);', [user.toLowerCase(), pass]), function (error, result) {
            if (result != undefined) {
                if (result.length > 0) {
                    var userId = result[0]['id'];
                    db.query (mysql.format ('SELECT * FROM `directs` WHERE (`id`=? AND (`user1_id`=? OR `user2_id`=?));', [directId, userId, userId]), function (error, result) {
                        if (result != undefined) {
                            if (result.length > 0) {
                                db.query (mysql.format ('SELECT `m`.*,`u`.`name` AS `user` FROM `messages` `m` LEFT JOIN `users` `u` ON `m`.`user_id`=`u`.`id` WHERE (`direct_id`=? AND `m`.`deleted`=\'0\');', [directId]), function (error, result) {
                                    if (result != undefined) {
                                        response.code = 1;
                                        response.data = result;
                                        response.error = null;
                                        socket.write(JSON.stringify(response) + '\n');
                                    } else {
                                        response.code = 0;
                                        response.data = null;
                                        response.error = error;
                                        socket.write(JSON.stringify(response) + '\n');
                                    }
                                });
                            } else {
                                response.code = 0;
                                response.data = null;
                                response.error = 'Invalid direct ID or You are not participant of this Direct';
                                socket.write(JSON.stringify(response) + '\n');
                            }
                        } else {
                            response.code = 0;
                            response.data = null;
                            response.error = error;
                            socket.write(JSON.stringify(response) + '\n');
                        }
                    });
                } else {
                    response.code = 0;
                    response.data = null;
                    response.error = 'Invalid username or password';
                    socket.write(JSON.stringify(response) + '\n');
                }
            } else {
                response.code = 0;
                response.data = null;
                response.error = error;
                socket.write(JSON.stringify(response) + '\n');
            }
        });
    },
    // SEND
    send: function (socket, clients, directId, user, pass, body, media) {
        response.action = 'directs:send';
        db.query (mysql.format ('SELECT `id`,`name` FROM `users` WHERE (LOWER(`user`)=? AND `pass`=?);', [user.toLowerCase(), pass]), function (error, result) {
            if (result != undefined) {
                if (result.length > 0) {
                    var userId = result[0]['id'];
                    var userName = result[0]['name'];
                    db.query (mysql.format ('SELECT * FROM `directs` WHERE (`id`=? AND (`user1_id`=? OR `user2_id`=?));', [directId, userId, userId]), function (error, result) {
                        if (result != undefined) {
                            if (result.length > 0) {
                                var partnerId = result[0]['user1_id'] != userId ? result[0]['user1_id'] : result[0]['user2_id'];
                                media = media.trim().length == 0 ? null : media;
                                var ts = Math.floor(new Date().getTime() / 1000);
                                db.query (mysql.format ('INSERT INTO `messages` VALUES (NULL,?,?,?,?,?,\'0\');', [directId, userId, body, media, ts]), function (error, result) {
                                    if (response != undefined) {
                                        response.code = 1;
                                        response.data = {'id':result.insertId,'direct_id':directId,'user_id':userId,'body':body,'media':media,'ts':ts,'user':userName};
                                        response.error = null;
                                        socket.write(JSON.stringify(response) + '\n');
                                        for (i in clients) {
                                            if (clients[i].id == partnerId) {
                                                response.action = 'global:newdirect';
                                                clients[i].write(JSON.stringify(response) + '\n');
                                                break;
                                            }
                                        }
                                    } else {
                                        response.code = 0;
                                        response.data = null;
                                        response.error = error;
                                        socket.write(JSON.stringify(response) + '\n');
                                    }
                                });
                            } else {
                                response.code = 0;
                                response.data = null;
                                response.error = 'Invalid direct ID or You are not participant of this Direct';
                                socket.write(JSON.stringify(response) + '\n');
                            }
                        } else {
                            response.code = 0;
                            response.data = null;
                            response.error = error;
                            socket.write(JSON.stringify(response) + '\n');
                        }
                    });
                } else {
                    response.code = 0;
                    response.data = null;
                    response.error = 'Invalid username or password';
                    socket.write(JSON.stringify(response) + '\n');
                }
            } else {
                response.code = 0;
                response.data = null;
                response.error = error;
                socket.write(JSON.stringify(response) + '\n');
            }
        });
    }
};