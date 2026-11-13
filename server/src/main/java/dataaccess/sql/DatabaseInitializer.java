package dataaccess.sql;

import service.exceptions.DataAccessException;
import dataaccess.DatabaseManager;
import java.sql.SQLException;

public class DatabaseInitializer {

    private static final String[] CREATE_STATEMENTS = {
            """
            TODO: create table statements here
            """
    };

    public static void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        //TODO: connect to database
    }
}
