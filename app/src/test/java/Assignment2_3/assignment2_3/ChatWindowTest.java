package Assignment2_3.assignment2_3;

import junit.framework.TestCase;

import org.junit.Test;

public class ChatWindowTest extends TestCase {
    @Test
    public void testValidateMessage() {
    assertTrue(ChatWindow.validateMessage(""));
    }
}