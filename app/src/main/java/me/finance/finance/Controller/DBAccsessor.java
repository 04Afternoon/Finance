package me.finance.finance.Controller;


class DBAccsessor {
    private static final DBAccsessor ourInstance = new DBAccsessor();

    static DBAccsessor getInstance() {
        return ourInstance;
    }

    private DBAccsessor() {


    }
}
