package test;


import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        CardTest.class,
        DeckTest.class,
        PackTest.class,
        PlayerTest.class
})
public class TestSuite { }