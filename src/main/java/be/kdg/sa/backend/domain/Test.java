package be.kdg.sa.backend.domain;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class Test {
    public UUID testId;
    public String testName;
    public String testDescription;

    public Test(String testName, String testDescription) {
        this.testId = UUID.randomUUID();
        this.testName = testName;
        this.testDescription = testDescription;
    }

}
