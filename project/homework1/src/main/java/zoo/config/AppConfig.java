package zoo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zoo.services.Report;
import zoo.services.Vclinic;
import zoo.services.Zoo;

/**
 * Spring configuration for zoo beans.
 */
@Configuration
public class AppConfig {

    @Bean
    public Vclinic vclinic() {
        return new Vclinic();
    }

    @Bean
    public Zoo zoo(Vclinic vclinic) {
        return new Zoo(vclinic);
    }

    /**
     * Report bean with zoo dependency.
     */
    @Bean
    public Report report(Zoo zoo) {
        Report report = new Report();
        report.setZoo(zoo);
        return report;
    }
}