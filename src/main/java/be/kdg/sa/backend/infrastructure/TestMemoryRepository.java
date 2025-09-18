package be.kdg.sa.backend.infrastructure;

import be.kdg.sa.backend.domain.Test;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class TestMemoryRepository implements be.kdg.sa.backend.domain.TestRepository {
    @Override
    public Collection<Test> getAllTests() {
        List<Test> testList = new ArrayList<>();

        testList.add(new Test("Math Test", "Basic algebra and geometry"));
        testList.add(new Test("Science Test", "Physics and chemistry fundamentals"));
        testList.add(new Test("History Test", "World history overview"));
        testList.add(new Test("Geography Test", "Countries and capitals"));
        testList.add(new Test("English Test", "Grammar and vocabulary"));
        testList.add(new Test("Computer Science Test", "Programming basics"));
        testList.add(new Test("Art Test", "Famous paintings and artists"));
        testList.add(new Test("Music Test", "Classical and modern music theory"));
        testList.add(new Test("Sports Test", "Rules and history of popular sports"));
        testList.add(new Test("Language Test", "Basic phrases in multiple languages"));
        return testList;

    }
}
