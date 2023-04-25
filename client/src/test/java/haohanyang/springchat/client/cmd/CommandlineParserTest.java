package haohanyang.springchat.client.cmd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandlineParserTest {

    @Test
    void test_parse_login() throws Exception {
        var input = "login  user1  password1 ";
        var command = CommandlineParser.parse(input);
        assertInstanceOf(LoginCommand.class, command);
        assertEquals("user1", ((LoginCommand) command).username());
        assertEquals("password1",((LoginCommand) command).password());
    }

}