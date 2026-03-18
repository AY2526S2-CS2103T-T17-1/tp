---
layout: page
title: User Guide
---

GymOps is a **desktop app for managing gym trainers and their clients**, optimized for use via a **Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI).

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from this project's release page.

1. Copy the file to the folder you want to use as the _home folder_ for GymOps.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use `java -jar YOUR_FILE_NAME.jar` to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

  * `list-trainers` : Lists all trainers.

  * `list-clients` : Lists all clients.

  * `find-trainers alice` : Finds all trainers whose names match `alice`.

  * `add-trainer n/John Doe p/98765432 e/johndoe@example.com` : Adds a trainer.

  * `add-client n/Alice Lim p/81234567 t/1` : Adds a client assigned to trainer #1.

  * `clear` : Deletes all persons.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add-trainer n/NAME`, `NAME` is a parameter which can be used as `add-trainer n/John Doe ...`.

* Items in square brackets are optional.<br>
  e.g `find KEYWORD [MORE_KEYWORDS]` can be used as `find alice` or `find alice bob`.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* For commands that use an `INDEX`, the index refers to the index number shown in the **currently displayed person list**.

* GymOps displays **two lists**: **Trainers** (left) and **Clients** (right). When a command takes an index:
  * Trainer-related indices refer to the **trainer list**.
  * Client-related indices refer to the **client list**.
  * The `delete` command is typed (`delete t/...` or `delete c/...`) to make this explicit.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a trainer: `add-trainer`

Adds a trainer to GymOps.

Format: `add-trainer n/NAME p/PHONE_NUMBER e/EMAIL`

Examples:
* `add-trainer n/John Doe p/98765432 e/johndoe@example.com`

### Adding a client: `add-client`

Adds a client and assigns them to a trainer.

Format: `add-client n/NAME p/PHONE_NUMBER t/TRAINER_INDEX`

* `TRAINER_INDEX` must refer to a **trainer** in the currently displayed list.

Examples:
* `add-client n/Alice Lim p/81234567 t/1`

### Listing all persons : `list`

Shows all persons in GymOps.

This resets both the trainer list and client list to show all entries, and clears any trainer-selection filter applied to the client list.

Format: `list`

### Locating persons by name: `find`

Finds trainers and clients whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Each keyword must be alphanumeric (letters and/or digits). e.g. `Bob123` is allowed, `Bob@` is not.
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`
* To return to the full list after a `find`, run `list`.

Alternatively, you can search within a specific list using `find-trainers` or `find-clients`.

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Listing all trainers: `list-trainers`

Shows all trainers in GymOps.

Format: `list-trainers`

### Listing all clients: `list-clients`

Shows all clients in GymOps.

Format: `list-clients`

### Viewing a Trainer's Clients (GUI)

In the GUI, you can click on any trainer card in the **Trainers** list (left) to view their assigned clients. This will automatically filter the **Clients** list (right) to show only the clients assigned to that specific trainer.

To clear the filter and view all clients again, click the **Showing All** link above the client list, or run the `list` command.

### Locating trainers by name: `find-trainers`

Finds trainers whose names contain any of the given keywords.

Format: `find-trainers KEYWORD [MORE_KEYWORDS]`

### Locating clients by name: `find-clients`

Finds clients whose names contain any of the given keywords.

Format: `find-clients KEYWORD [MORE_KEYWORDS]`

### Setting a client's calorie target: `set-calorie-target`

Sets the daily calorie target for a client.

Format: `set-calorie-target INDEX cal/CALORIES`

* `INDEX` must refer to a **client** in the client list.
* `CALORIES` must be a positive integer.

Examples:
* `set-calorie-target 1 cal/2000`

### Logging a client's calorie intake: `log-calorie`

Logs calorie intake for a client. The calories are added to the client's existing daily intake.

Format: `log-calorie INDEX cal/CALORIES`

* `INDEX` must refer to a **client** in the client list.
* `CALORIES` must be a positive integer.

Examples:
* `log-calorie 1 cal/500`

### Setting a client's workout focus: `set-focus`

Sets the current primary workout focus for a client. If a focus already exists, it will be overwritten.

Format: `set-focus c/CLIENT_INDEX f/FOCUS`

* `CLIENT_INDEX` must refer to a **client** in the client list.
* `FOCUS` must contain only letters (`A-Z` or `a-z`).

Examples:
* `set-focus c/1 f/Chest`

### Adding a remark to a client: `remark`

Adds a remark to an existing client. If a remark already exists, it will be overwritten.

Format: `remark INDEX r/REMARK`

* `INDEX` must refer to a **client** in the client list.
* `REMARK` must not be empty (after trimming).

Examples:
* `remark 1 r/Recovering from ACL surgery`

### Deleting a person : `delete`

Deletes the specified person from GymOps.

Format: `delete t/TRAINER_INDEX` or `delete c/CLIENT_INDEX`

* `t/TRAINER_INDEX` refers to the index number shown in the trainer list.
* `c/CLIENT_INDEX` refers to the index number shown in the client list.
* Trainer deletion will be rejected if the trainer still has active clients.

Examples:
* `delete t/2` deletes the 2nd trainer in the trainer list.
* `delete c/1` deletes the 1st client in the client list.

### Deleting a client : `delete-client`

Deletes the specified client.

Format: `delete-client INDEX`

* `INDEX` must refer to a **client** in the client list.

Examples:
* `delete-client 1`

### Deleting a trainer : `delete-trainer`

Deletes the specified trainer.

Format: `delete-trainer INDEX`

* `INDEX` must refer to a **trainer** in the trainer list.

Examples:
* `delete-trainer 1`

### Clearing all entries : `clear`

Clears all entries from GymOps.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

GymOps data are saved on disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

GymOps data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, GymOps will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause GymOps to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous GymOps home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add trainer** | `add-trainer n/NAME p/PHONE_NUMBER e/EMAIL`<br> e.g., `add-trainer n/John Doe p/98765432 e/johndoe@example.com`
**Add client** | `add-client n/NAME p/PHONE_NUMBER t/TRAINER_INDEX`<br> e.g., `add-client n/Alice Lim p/81234567 t/1`
**Set calorie target** | `set-calorie-target INDEX cal/CALORIES`<br> e.g., `set-calorie-target 1 cal/2000`
**Log calorie intake** | `log-calorie INDEX cal/CALORIES`<br> e.g., `log-calorie 1 cal/500`
**Set workout focus** | `set-focus c/CLIENT_INDEX f/FOCUS`<br> e.g., `set-focus c/1 f/Chest`
**Remark** | `remark INDEX r/REMARK`<br> e.g., `remark 1 r/Recovering from ACL surgery`
**Find (both lists)** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**Find trainers** | `find-trainers KEYWORD [MORE_KEYWORDS]`<br> e.g., `find-trainers John`
**Find clients** | `find-clients KEYWORD [MORE_KEYWORDS]`<br> e.g., `find-clients Alice`
**List (both lists)** | `list`
**List trainers** | `list-trainers`
**List clients** | `list-clients`
**Delete (typed)** | `delete t/TRAINER_INDEX` or `delete c/CLIENT_INDEX`<br> e.g., `delete t/1`
**Delete client** | `delete-client INDEX`<br> e.g., `delete-client 2`
**Delete trainer** | `delete-trainer INDEX`<br> e.g., `delete-trainer 1`
**Clear** | `clear`
**Help** | `help`
**Exit** | `exit`
