<?php


class Database {
    public static $host = "localhost";
    public static $port = 3306;
    public static $database = "whatsweb";
    public static $con;

    /*
     * Connects to the database with credentials from database.json
     */
    public static function connect() {
        self::$con = mysqli_connect(self::$host, "root", "", self::$database);
        self::$con->set_charset("utf8");
    }

    public static function disconnect() {
        if(self::isConnected()) {
            mysqli_close(self::$con);
        }
    }

    public static function isConnected() {
        return self::$con != null;
    }

    /**
     * @return mysqli connection
     */
    public static function getConnection() {
        return self::$con;
    }

}