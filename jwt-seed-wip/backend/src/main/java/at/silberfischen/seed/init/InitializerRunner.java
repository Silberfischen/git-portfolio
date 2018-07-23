package at.silberfischen.seed.init;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@DependsOn("liquibase")
public class InitializerRunner {

    private final List<Initializer> initializers;

    public InitializerRunner(List<Initializer> initializers) {
        this.initializers = initializers;
    }

    @PostConstruct
    public void init() {
        initializers.forEach(Initializer::execute);
    }

}
