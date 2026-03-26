package seedu.address.ui;

import java.util.Comparator;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Client;
import seedu.address.model.person.Person;
import seedu.address.model.person.Trainer;
import seedu.address.model.tag.Tag;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved
     * keywords in JavaFX. As a consequence, UI elements' variable names cannot
     * be set to such keywords or an exception will be thrown by JavaFX during
     * runtime.
     *
        * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The
        *     issue on AddressBook level 4</a>
     */
    private final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label personIcon;
    @FXML
    private Label phone;
    @FXML
    private Label role;
    @FXML
    private Label email;
    @FXML
    private Label clientCount;
    @FXML
    private Label assignedTrainer;
    @FXML
    private Label calorieInfo;
    @FXML
    private ProgressBar calorieProgressBar;
    @FXML
    private Label workoutFocus;
    @FXML
    private Label remark;
    @FXML
    private Label validity;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PersonCard} with the given {@code Person} and index to
     * display.
     */
    public PersonCard(Person person, int displayedIndex, ObservableList<Person> allPersons) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().getFullName());
        phone.setText(person.getPhone().getValue());
        person.getTags().stream()
                .sorted(Comparator.comparing(Tag::getTagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.getTagName())));

        role.setManaged(false);
        role.setVisible(false);

        if (person instanceof Trainer) {
            Trainer trainer = (Trainer) person;
            personIcon.setText("💪");
            personIcon.getStyleClass().add("trainer-icon");
            email.setText("Email: " + trainer.getEmail().getValue());
            email.setManaged(true);
            email.setVisible(true);

            setClientCountLabel(trainer, allPersons);

            assignedTrainer.setManaged(false);
            assignedTrainer.setVisible(false);
            calorieInfo.setManaged(false);
            calorieInfo.setVisible(false);
            calorieProgressBar.setManaged(false);
            calorieProgressBar.setVisible(false);
            workoutFocus.setManaged(false);
            workoutFocus.setVisible(false);
            remark.setManaged(false);
            remark.setVisible(false);
            validity.setManaged(false);
            validity.setVisible(false);
        } else if (person instanceof Client) {
            Client client = (Client) person;
            personIcon.setText("👤");
            personIcon.getStyleClass().add("client-icon");
            assignedTrainer.setText("Trainer: " + client.getTrainerName().getFullName());
            assignedTrainer.setManaged(true);
            assignedTrainer.setVisible(true);
            email.setManaged(false);
            email.setVisible(false);

            clientCount.setManaged(false);
            clientCount.setVisible(false);

            setCalorieInfoLabel(client);
            setWorkoutFocusLabel(client);
            setRemarkLabel(client);
            setValidityLabel(client);
        }
    }

    public Person getPerson() {
        return person;
    }

    private void setWorkoutFocusLabel(Client client) {
        if (client.getWorkoutFocus().isPresent()) {
            workoutFocus.setText("Workout focus: " + client.getWorkoutFocus().get().getValue());
            workoutFocus.setManaged(true);
            workoutFocus.setVisible(true);
        } else {
            workoutFocus.setManaged(false);
            workoutFocus.setVisible(false);
        }
    }

    private void setRemarkLabel(Client client) {
        if (client.getRemark().isPresent()) {
            remark.setText("Remark: " + client.getRemark().get().getValue());
            remark.setManaged(true);
            remark.setVisible(true);
        } else {
            remark.setManaged(false);
            remark.setVisible(false);
        }
    }

    private void setValidityLabel(Client client) {
        if (client.getValidity().isPresent()) {
            validity.setText("Membership valid till: " + client.getValidity().get().toString());
            validity.setManaged(true);
            validity.setVisible(true);
        } else {
            validity.setManaged(false);
            validity.setVisible(false);
        }
    }

    private void setClientCountLabel(Trainer trainer, ObservableList<Person> allPersons) {
        long count = allPersons.stream()
                .filter(p -> p instanceof Client)
                .map(p -> (Client) p)
                .filter(c -> c.getTrainerPhone().equals(trainer.getPhone()))
                .count();
        clientCount.setText(count + " clients");
        clientCount.setManaged(true);
        clientCount.setVisible(true);
    }

    /**
     * Sets the calorie info label text, progress bar, and visibility
     * based on the client's calorie data.
     * Shows intake vs target with a progress bar if a target is set,
     * or just the intake if only intake is logged.
     * Hides both label and bar if no calorie data has been recorded.
     */
    private void setCalorieInfoLabel(Client client) {
        int target = client.getCalorieTarget();
        int intake = client.getCalorieIntake();

        if (target > 0) {
            int remaining = target - intake;
            String calorieStatus = remaining >= 0
                    ? remaining + " kcal remaining"
                    : "exceeded by " + (-remaining) + " kcal";
            calorieInfo.setText(String.format("Calories: %d / %d kcal (%s)",
                    intake, target, calorieStatus));
            calorieInfo.setManaged(true);
            calorieInfo.setVisible(true);

            double progress = Math.min(1.0, (double) intake / target);
            calorieProgressBar.setProgress(progress);
            calorieProgressBar.setManaged(true);
            calorieProgressBar.setVisible(true);

            calorieProgressBar.getStyleClass().removeAll("calorie-bar-exceeded");
            if (intake > target) {
                calorieProgressBar.getStyleClass().add("calorie-bar-exceeded");
            }
        } else if (intake > 0) {
            calorieInfo.setText(String.format("Calories consumed: %d kcal", intake));
            calorieInfo.setManaged(true);
            calorieInfo.setVisible(true);
            calorieProgressBar.setManaged(false);
            calorieProgressBar.setVisible(false);
        } else {
            calorieInfo.setManaged(false);
            calorieInfo.setVisible(false);
            calorieProgressBar.setManaged(false);
            calorieProgressBar.setVisible(false);
        }
    }
}
