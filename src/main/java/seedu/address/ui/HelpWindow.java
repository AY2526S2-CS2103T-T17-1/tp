package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2526s2-cs2103t-t17-1.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE = "Refer to the user guide: " + USERGUIDE_URL;

    public static final String COMMAND_SUMMARY_MESSAGE = ""
            + "add-trainer n/NAME p/PHONE e/EMAIL\n"
            + "add-client n/NAME p/PHONE t/TRAINER_INDEX\n"
            + "reassign-client CLIENT_INDEX t/TRAINER_INDEX\n"
            + "\n"
            + "delete-trainer INDEX\n"
            + "delete-client INDEX\n"
            + "delete t/TRAINER_INDEX  |  delete c/CLIENT_INDEX\n"
            + "\n"
            + "list  |  list-trainers  |  list-clients\n"
            + "find KEYWORD  |  find-trainers KEYWORD  |  find-clients KEYWORD\n"
            + "\n"
            + "set-calorie-target INDEX cal/CALORIES\n"
            + "log-calorie INDEX cal/CALORIES\n"
            + "set-focus c/CLIENT_INDEX f/FOCUS\n"
            + "remark INDEX r/REMARK\n"
            + "\n"
            + "export FILE_PATH  |  import FILE_PATH\n"
            + "clear  |  exit";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label commandSummary;

    @FXML
    private Label helpMessage;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);
        commandSummary.setText(COMMAND_SUMMARY_MESSAGE);
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
    }
}
