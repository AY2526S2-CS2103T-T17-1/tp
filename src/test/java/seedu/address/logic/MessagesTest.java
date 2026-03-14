package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Trainer;
import seedu.address.model.tag.Tag;

public class MessagesTest {

    @Test
    public void getErrorMessageForDuplicatePrefixes_containsAllPrefixes() {
        String message = Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_NAME, CliSyntax.PREFIX_PHONE);
        assertTrue(message.startsWith(Messages.MESSAGE_DUPLICATE_FIELDS));
        assertTrue(message.contains(CliSyntax.PREFIX_NAME.toString()));
        assertTrue(message.contains(CliSyntax.PREFIX_PHONE.toString()));
    }

    @Test
    public void format_trainer_containsNamePhoneAndTags() {
        HashSet<Tag> tags = new HashSet<>();
        tags.add(new Tag("friends"));
        Trainer trainer = new Trainer(new Name("Alex Yeoh"), new Phone("87438807"),
                new Email("alexyeoh@example.com"), tags);

        String formatted = Messages.format(trainer);
        assertTrue(formatted.contains("Alex Yeoh"));
        assertTrue(formatted.contains("Phone: 87438807"));
        assertTrue(formatted.contains("Tags:"));
        assertTrue(formatted.contains("friends"));
    }
}
