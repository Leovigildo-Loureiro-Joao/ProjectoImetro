import com.imetro.config.Env;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;

public class FlywayRepairRunner {
    public static void main(String[] args) {
        String url = Env.get("DB_URL", "jdbc:postgresql://localhost:5432/simulatorbolsastudy");
        String user = Env.get("DB_USER", "simulator");
        String password = Env.get("DB_PASSWORD", "simulator");
        Flyway flyway = Flyway.configure()
            .dataSource(url, user, password)
            .locations("classpath:db/migration")
            .baselineOnMigrate(true)
            .baselineVersion(MigrationVersion.fromVersion("6"))
            .load();
        flyway.repair();
        System.out.println("Flyway repair concluido com sucesso.");
    }
}