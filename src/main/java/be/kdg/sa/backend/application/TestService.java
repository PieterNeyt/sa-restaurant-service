package be.kdg.sa.backend.application;

import be.kdg.sa.backend.domain.Test;
import be.kdg.sa.backend.domain.TestRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TestService {

    private TestRepository testRepository;

    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public Collection<Test> getAllTests() {
        return testRepository.getAllTests();
    }
}
