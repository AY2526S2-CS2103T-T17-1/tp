package seedu.address.logic.parser;

import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;

public class ArgumentMultimapTest {

    @Test
    public void verifyNoDuplicatePrefixesFor_duplicatePrefix_throwsParseException() {
        ArgumentMultimap map = new ArgumentMultimap();
        Prefix p = new Prefix("p/");
        map.put(p, "one");
        map.put(p, "two");

        assertThrows(ParseException.class, () -> map.verifyNoDuplicatePrefixesFor(p));
    }

    @Test
    public void verifyNoDuplicatePrefixesFor_noDuplicates_doesNotThrow() throws Exception {
        ArgumentMultimap map = new ArgumentMultimap();
        Prefix p = new Prefix("p/");
        Prefix q = new Prefix("q/");
        map.put(p, "one");
        map.put(q, "two");

        map.verifyNoDuplicatePrefixesFor(p, q);
    }
}
