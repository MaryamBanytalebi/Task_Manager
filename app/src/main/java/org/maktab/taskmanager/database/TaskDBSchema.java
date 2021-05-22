package org.maktab.taskmanager.database;

public class TaskDBSchema {

    public static final String NAME = "task.db";
    public static final Integer VERSION = 1;

    public static final class TaskTable{
        public static final String NAME = "taskTable";

        public static final class Cols{

            public static final String ID = "id";
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DESCRIPTION = "description";
            public static final String DATE = "date";
            public static final String STATE = "state";
        }
    }
    public static final class UserTable {
        public static final String NAME = "UserTable";

        public static final class Cols {
            //this column in only for database
            public static final String ID = "id";
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";
        }
    }
}
