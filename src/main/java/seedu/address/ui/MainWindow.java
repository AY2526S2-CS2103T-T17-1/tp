package seedu.address.ui;

import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.model.person.Trainer;

/**
 * The Main Window. Provides the basic application layout containing a menu bar
 * and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private PersonListPanel trainerListPanel;
    private PersonListPanel clientListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private Menu helpMenu;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane trainerListPanelPlaceholder;

    @FXML
    private StackPane clientListPanelPlaceholder;

    @FXML
    private Hyperlink clientFilterLink;

    @FXML
    private Hyperlink trainerFilterLink;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and
     * {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    /**
     * Returns the primary stage of the application.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     *
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        trainerListPanel = new PersonListPanel(
            logic.getFilteredTrainerList(),
            logic.getAddressBook().getPersonList());
        trainerListPanelPlaceholder.getChildren().add(trainerListPanel.getRoot());

        clientListPanel = new PersonListPanel(
            logic.getFilteredClientList(),
            logic.getAddressBook().getPersonList());
        clientListPanelPlaceholder.getChildren().add(clientListPanel.getRoot());

        trainerListPanel.setOnSelectedPersonChanged(this::handleSelectedTrainerChanged);
        updateClientFilterLinkText();
        updateTrainerFilterLinkState();

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Intercepts the Help menu opening and directly shows the help window instead.
     */
    @FXML
    private void handleHelpMenuShowing() {
        Platform.runLater(() -> {
            helpMenu.hide();
            handleHelp();
        });
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(
                primaryStage.getWidth(),
                primaryStage.getHeight(),
                (int) primaryStage.getX(),
                (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    /**
     * Returns the trainer list panel.
     */
    public PersonListPanel getPersonListPanel() {
        return trainerListPanel;
    }

    @FXML
    private void handleClearSelectedTrainer() {
        try {
            executeCommand("list-c");
        } catch (CommandException | ParseException e) {
            logger.info("Failed to list all clients: " + e.getMessage());
        }
    }

    @FXML
    private void handleListAllTrainers() {
        try {
            executeCommand("list-t");
        } catch (CommandException | ParseException e) {
            logger.info("Failed to list all trainers: " + e.getMessage());
        }
    }

    private void handleSelectedTrainerChanged(Person selectedPerson) {
        if (selectedPerson instanceof Trainer) {
            logic.setSelectedTrainer((Trainer) selectedPerson);
        } else {
            logic.clearSelectedTrainer();
        }
        updateClientFilterLinkText();
    }

    private void updateClientFilterLinkText() {
        String linkText = logic.getSelectedTrainer()
                .map(trainer -> "Showing: " + trainer.getName().getFullName())
                .orElseGet(() -> logic.isClientListFiltered()
                        ? "Showing: Filtered"
                        : "Showing All");
        clientFilterLink.setText(linkText);
    }

    private void updateTrainerFilterLinkState() {
        boolean filtered = logic.isTrainerListFiltered();
        trainerFilterLink.setDisable(false);
        trainerFilterLink.setText(filtered ? "Show All Trainers" : "Showing All");
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isClearTrainer()) {
                trainerListPanel.clearSelection();
            }

            updateClientFilterLinkText();
            updateTrainerFilterLinkState();

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
