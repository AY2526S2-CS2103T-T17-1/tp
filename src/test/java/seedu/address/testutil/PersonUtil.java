package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.commands.AddTrainerCommand;
import seedu.address.model.person.Trainer;

/**
 * A utility class for Person.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code person}.
     */
    public static String getAddTrainerCommand(Trainer trainer) {
        return AddTrainerCommand.COMMAND_WORD + " " + getTrainerDetails(trainer);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getTrainerDetails(Trainer trainer) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + trainer.getName().fullName + " ");
        sb.append(PREFIX_PHONE + trainer.getPhone().value + " ");
        sb.append(PREFIX_EMAIL + trainer.getEmail().value + " ");
        trainer.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }
}
