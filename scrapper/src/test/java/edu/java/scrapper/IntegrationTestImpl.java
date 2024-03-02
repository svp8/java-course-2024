package edu.java.scrapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IntegrationTestImpl extends IntegrationTest {
    @Test
    @DisplayName("Check if tables from migrations created")
    void testMigrations() throws SQLException {
        try (Connection connection = DriverManager.getConnection(
            POSTGRES.getJdbcUrl(),
            POSTGRES.getUsername(),
            POSTGRES.getPassword()
        )) {
            ResultSet result = connection.prepareStatement("""
                select count(*) FROM information_schema.tables
                                WHERE  table_schema = 'public'
                                 AND    table_name   = 'link' or  table_name   = 'chat'
                                 or  table_name   = 'chat_link'
                                ;
                """).executeQuery();
            while (result.next()) {
                Assertions.assertEquals(3, result.getInt(1));
            }
        }

    }
}
