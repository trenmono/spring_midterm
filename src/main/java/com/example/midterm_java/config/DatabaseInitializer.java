package com.example.midterm_java.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Rename 'stock' to 'STAFF' if it exists
        try {
            jdbcTemplate.execute("UPDATE role SET role_name = 'STAFF' WHERE role_name = 'stock' OR role_name = 'STOCK';");
        } catch (Exception e) {
            System.err.println("Failed to update role name stock to STAFF: " + e.getMessage());
        }

        // 2. Ensure ADMIN, STAFF, USER roles are capitalized and present
        try {
            jdbcTemplate.execute("UPDATE role SET role_name = 'ADMIN' WHERE r_id = 0;");
            jdbcTemplate.execute("UPDATE role SET role_name = 'STAFF' WHERE r_id = 1;");
            jdbcTemplate.execute("UPDATE role SET role_name = 'USER' WHERE r_id = 2;");
        } catch (Exception e) {
            System.err.println("Failed to update role IDs 0, 1, 2: " + e.getMessage());
        }

        // 3. Fallback: If roles do not exist at all, insert them manually
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM role", Integer.class);
            if (count == null || count == 0) {
                jdbcTemplate.execute("INSERT INTO role (r_id, role_name) VALUES (0, 'ADMIN');");
                jdbcTemplate.execute("INSERT INTO role (r_id, role_name) VALUES (1, 'STAFF');");
                jdbcTemplate.execute("INSERT INTO role (r_id, role_name) VALUES (2, 'USER');");
            }
        } catch (Exception e) {
            System.err.println("Failed to insert default roles: " + e.getMessage());
        }
    }
}
