package be.kdg.sa.backend.api;

import be.kdg.sa.backend.application.TestService;
import be.kdg.sa.backend.domain.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/tests")
public class TestRestController {

    private TestService testService;

    public TestRestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping({"/",""})
    public Collection<Test> getAllTests() {
        return testService.getAllTests();
    }
}
