package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.ObservableList;
import seedu.address.model.Model;
import seedu.address.model.person.Client;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Trainer;

/**
 * Lists all trainers in the address book sorted by the number of clients they have in descending order.
 */
public class StatsCommand extends Command {

    public static final String COMMAND_WORD = "stats";

    public static final String MESSAGE_SUCCESS = "Listed all trainers sorted by number of clients.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredTrainerList(PREDICATE_SHOW_ALL_PERSONS);

        ObservableList<Person> allPersons = model.getAddressBook().getPersonList();

        Map<Phone, Integer> clientCounts = new HashMap<>();
        for (Person p : allPersons) {
            if (p instanceof Client) {
                Client client = (Client) p;
                Phone pPhone = client.getTrainerPhone();
                clientCounts.put(pPhone, clientCounts.getOrDefault(pPhone, 0) + 1);
            }
        }

        model.updateSortedTrainerList((p1, p2) -> {
            if (!(p1 instanceof Trainer) || !(p2 instanceof Trainer)) {
                return 0;
            }
            Trainer t1 = (Trainer) p1;
            Trainer t2 = (Trainer) p2;
            int count1 = clientCounts.getOrDefault(t1.getPhone(), 0);
            int count2 = clientCounts.getOrDefault(t2.getPhone(), 0);
            if (count1 == count2) {
                return t1.getName().toString().compareToIgnoreCase(t2.getName().toString());
            }
            return Integer.compare(count2, count1);
        });

        return new CommandResult(MESSAGE_SUCCESS);
    }
}
