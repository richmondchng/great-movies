package test.richmond.filemgt.config

import org.flywaydb.core.Flyway
import org.springframework.boot.flyway.autoconfigure.FlywayMigrationStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("dev", "test")
class FlywayConfig {

    @Bean
    fun cleanMigrateStrategy(): FlywayMigrationStrategy {
        return FlywayMigrationStrategy { flyway: Flyway ->
            flyway.clean()
            flyway.migrate()
        }
    }
}