package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Client;
import seedu.address.model.person.Person;
import seedu.address.model.person.Trainer;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label role;
    @FXML
    private Label email;
    @FXML
    private Label assignedTrainer;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        if (person instanceof Trainer) {
            Trainer trainer = (Trainer) person;
            role.setText("Role: Trainer");
            email.setText("Email: " + trainer.getEmail().value);
            email.setManaged(true);
            email.setVisible(true);
            assignedTrainer.setManaged(false);
            assignedTrainer.setVisible(false);
        } else if (person instanceof Client) {
            Client client = (Client) person;
            role.setText("Role: Client");
            assignedTrainer.setText("Assigned to Trainer: " + client.getTrainerName().fullName);
            assignedTrainer.setManaged(true);
            assignedTrainer.setVisible(true);
            email.setManaged(false);
            email.setVisible(false);
        }
    }
}
