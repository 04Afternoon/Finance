package me.finance.finance.Controller;

import

class DBAccsessor {
    private static final DBAccsessor ourInstance = new DBAccsessor();

    static DBAccsessor getInstance() {
        return ourInstance;
    }

    private DBAccsessor() {


    }
}
