import models.LogMessage;
import org.junit.*;
import play.test.*;

public class BasicTest extends UnitTest {

    @Test
    public void LogMessageTest() {
        long num = LogMessage.count();
        LogMessage lm = new LogMessage("a test message", 3, "TEST");
        lm.save();
        Assert.assertEquals("One more message should be present", num + 1, LogMessage.count());
    }

    @Test
    public void LogMessageinvalidVal() {
        LogMessage lm = new LogMessage("a test message", 8, "TEST");        
        Assert.assertFalse("value is neg, it should not pass validation", lm.validateAndSave());
    }

    @Test
    public void LogMessageinvalidName() {
        LogMessage lm = new LogMessage("a test message", 8, null);
        Assert.assertFalse("null proj name", lm.validateAndSave());
    }

    @Test
    public void messTooLong() {
        LogMessage lm = new LogMessage("a test message taht is way too long", 8, "More");
        Assert.assertFalse("mess too long", lm.validateAndSave());
    }

    @Test
    public void projTooLong() {
        LogMessage lm = new LogMessage("a test message", 8, "More TestMore TestMore TestMore Test");
        Assert.assertFalse("proj name too long", lm.validateAndSave());
    }

    @Test
    public void levelNeg() {
        LogMessage lm = new LogMessage("a message", -8, "More TestMore");
        Assert.assertFalse("level is negative", lm.validateAndSave());
    }

}
