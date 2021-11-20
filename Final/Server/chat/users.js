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
    // GETCHANNELS
    getchannels: function (socket, user, pass) {
        response.action = 'users:getchannels';
        db.query (mysql.format ('SELECT `id` FROM `users` WHERE (LOWER(`user`)=? AND `pass`=?);', [user.toLowerCase(), pass]), function (error, result) {
            if (result != undefined) {
                if (result.length > 0) {
                    db.query (mysql.format ('SELECT * FROM `channels` WHERE (`id` IN (SELECT `channel_id` FROM `subscriptions` WHERE (`user_id`=? AND `deleted`=\'0\'))) ORDER BY `ts` DESC;', [result[0]['id']]), function (error, result) {
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
    // GETDIRECTS
    getdirects: function (socket, user, pass) {
        response.action = 'users:getdirects';
        db.query (mysql.format ('SELECT `id` FROM `users` WHERE (LOWER(`user`)=? AND `pass`=?);', [user.toLowerCase(), pass]), function (error, result) {
            if (result != undefined) {
                if (result.length > 0) {
                    db.query (mysql.format ('SELECT * FROM `directs` WHERE ((`user1_id`=? OR `user2_id`=?) AND `deleted`=\'0\') ORDER BY `ts` DESC;', [result[0]['id'], result[0]['id']]), function (error, result) {
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
    // GETGROUPS
    getgroups: function (socket, user, pass) {
        response.action = 'users:getgroups';
        db.query (mysql.format ('SELECT `id` FROM `users` WHERE (LOWER(`user`)=? AND `pass`=?);', [user.toLowerCase(), pass]), function (error, result) {
            if (result != undefined) {
                if (result.length > 0) {
                    db.query (mysql.format ('SELECT * FROM `groups` WHERE (`id` IN (SELECT `group_id` FROM `members` WHERE (`user_id`=? AND `deleted`=\'0\'))) ORDER BY `ts` DESC;', [result[0]['id']]), function (error, result) {
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
    // GETNAME
    getname: function (socket, id) {
        response.action = 'users:getname';
        db.query (mysql.format ('SELECT `id`,`name` FROM `users` WHERE (`id`=?);', [id]), function (error, result) {
            if (result != undefined) {
                if (result.length > 0) {
                    response.code = 1;
                    response.error = null;
                    response.data = result[0];
                } else {
                    response.code = 0;
                    response.data = null;
                    response.error = 'Invalid ID';
                }
            } else {
                response.code = 0;
                response.data = null;
                response.error = error;
            }
            socket.write(JSON.stringify(response) + '\n');
        });
    },
    // LOGIN
    login: function (socket, user, pass) {
        response.action = 'users:login';
        db.query (mysql.format ('SELECT `id`,`name` FROM `users` WHERE (LOWER(`user`)=? AND `pass`=?);', [user.toLowerCase(), pass]), function (error, result) {
            if (result != undefined) {
                if (result.length > 0) {
                    socket.id = result[0]['id'];
                    response.code = 1;
                    response.error = null;
                    response.data = result[0];
                } else {
                    response.code = 0;
                    response.data = null;
                    response.error = 'Invalid username or password';
                }
            } else {
                response.code = 0;
                response.data = null;
                response.error = error;
            }
            socket.write(JSON.stringify(response) + '\n');
        });
    },
    // REGISTER
    register: function (socket, name, user, pass) {
        response.action = 'users:register';
        db.query (mysql.format ('SELECT COUNT(*) AS `total` FROM `users` WHERE (LOWER(`user`)=?);', [user.toLowerCase()]), function (error, result) {
            if (result != undefined && result.length > 0) {
                if (result[0]['total'] == 0) {
                    db.query (mysql.format ('INSERT INTO `users` VALUES (NULL,?,?,?,\'0\');', [name, user.toLowerCase(), pass]), function (error, result) {
                        if (result != undefined && result.affectedRows > 0) {
                            response.code = 1;
                            response.error = null;
                            response.data = result.insertId;
                            socket.write(JSON.stringify(response) + '\n');
                        } else {
                            response.code = 0;
                            response.error = error;
                            response.data = null;
                            socket.write(JSON.stringify(response) + '\n');
                        }
                    });
                } else {
                    response.code = 0;
                    response.error = 'Duplicate username';
                    response.data = null;
                    socket.write(JSON.stringify(response) + '\n');
                }
            } else {
                response.error = error;
                socket.write(JSON.stringify(response) + '\n');
            }
        });
    },
};