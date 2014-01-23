package net.thucydides.core.reports;

import net.thucydides.core.annotations.Feature;
import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestStepFactory;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class TestOutcomesBuilder {

    public final static DateTime EARLY_DATE = new DateTime(2013,1,1,0,0);
    public final static DateTime LATE_DATE = new DateTime(2013,1,2,0,0);

    @Feature
    class WidgetFeature {
        class PurchaseNewWidget{}
    }

    public TestOutcomes getDefaultResults() {
        List<TestOutcome> testOutcomeList = new ArrayList<TestOutcome>();

        Story story = Story.from(WidgetFeature.PurchaseNewWidget.class);
        testOutcomeList.add(thatSucceedsFor(story, 10));
        testOutcomeList.add(thatSucceedsFor(story, 20));
        testOutcomeList.add(thatIsFailingFor(story, 30));
        testOutcomeList.add(thatIsPendingFor(story, 0));
        testOutcomeList.add(thatIsPendingFor(story, 0));
        testOutcomeList.add(thatIsPendingFor(story, 0));

        testOutcomeList.add(thatIsFailingFor(story, 10));
        testOutcomeList.add(thatIsFailingFor(story, 20));
        testOutcomeList.add(thatIsFailingFor(story, 30));
        testOutcomeList.add(thatIsIgnoredFor(story, 10));
        testOutcomeList.add(thatIsPendingFor(story, 0));
        testOutcomeList.add(thatIsPendingFor(story, 0));

        return TestOutcomes.of(testOutcomeList);
    }


    public TestOutcome thatSucceedsFor(Story story, int stepCount) {
        TestOutcome testOutcome = TestOutcome.forTestInStory("a test", story);
        for(int i = 1; i <= stepCount; i++ ){
            testOutcome.recordStep(TestStepFactory.forASuccessfulTestStepCalled("Step " + i));
        }
        testOutcome.setStartTime(EARLY_DATE);
        return testOutcome;
    }

    public TestOutcome thatIsPendingFor(Story story, int stepCount) {
        TestOutcome testOutcome = TestOutcome.forTestInStory("a test", story);
        for(int i = 1; i <= stepCount; i++ ){
            testOutcome.recordStep(TestStepFactory.forAPendingTestStepCalled("Step " + i));
        }
        testOutcome.setStartTime(LATE_DATE);
        return testOutcome;
    }

    public TestOutcome thatIsIgnoredFor(Story story, int stepCount) {
        TestOutcome testOutcome = TestOutcome.forTestInStory("a test", story);
        for(int i = 1; i <= stepCount; i++ ){
            testOutcome.recordStep(TestStepFactory.forAnIgnoredTestStepCalled("Step " + i));
        }
        return testOutcome;
    }

    public TestOutcome thatIsFailingFor(Story story, int stepCount) {
        TestOutcome testOutcome = TestOutcome.forTestInStory("a test", story);
        for(int i = 1; i <= stepCount; i++ ){
            testOutcome.recordStep(TestStepFactory.forABrokenTestStepCalled("Step " + i, new AssertionError()));
        }
        testOutcome.setStartTime(LATE_DATE);
        return testOutcome;
    }
}