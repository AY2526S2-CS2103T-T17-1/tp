---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* is a gym supervisor/manager overseeing multiple trainers and their respective client bases
* is tech-savvy, comfortable on desktop, and prefers CLI input over GUI navigation
* types quickly and values rapid data entry for operational coordination
* frequently handles trainer substitutions, client reallocations, and handovers across a shifting weekly schedule
* needs to track high-level client requirements (calorie targets/intake and workout focus) without managing detailed coaching prescriptions

**Value proposition**: reduce the operational burden of managing trainer–client relationships during frequent schedule changes by enabling fast CLI updates and reassignment, while preserving clients’ workout-relevant details (workout focus and calories) for handover.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                    | I want to …​                     | So that I can…​                                                        |
| -------- | ------------------------------------------ | ------------------------------ | ---------------------------------------------------------------------- |
| `* * *`  | new supervisor user                        | see usage instructions         | refer to instructions when I forget how to use GymOps                  |
| `* * *`  | supervisor                                 | add a new trainer              | build and maintain my roster of staff                                  |
| `* * *`  | supervisor                                 | edit a trainer’s details       | keep trainer contact information accurate                               |
| `* * *`  | supervisor                                 | list all trainers              | see which trainers are currently employed/registered                    |
| `* * *`  | supervisor                                 | delete a trainer who has no assigned clients | remove trainers who have left the gym                      |
| `* * *`  | supervisor                                 | add a client assigned to a trainer | allocate responsibility for that member                             |
| `* * *`  | supervisor                                 | edit a client’s details        | keep client contact information accurate                                |
| `* * *`  | supervisor                                 | list all clients (optionally by trainer) | view allocations and a trainer’s current client base            |
| `* * *`  | supervisor                                 | delete a client                | remove members who have cancelled their membership                      |
| `* * *`  | supervisor                                 | reassign a client to another trainer | handle trainer unavailability and schedule changes                 |
| `* * *`  | supervisor                                 | set a calorie target for a client | record their nutritional goal (e.g., 2500 kcal)                     |
| `* * *`  | supervisor                                 | log a client’s calorie intake  | track whether they are meeting their nutritional goals                  |
| `* * *`  | supervisor                                 | set a workout focus for a client | preserve the right context during handovers                          |
| `* * *`  | supervisor                                 | view a client’s progress summary | see target vs consumed calories and workout focus quickly            |
| `* *`    | supervisor                                 | find trainers/clients by name  | locate their record without scrolling through long lists                |
| `* *`    | supervisor                                 | add a remark to a client       | keep operational notes (e.g., injuries, payment checks)                 |
| `* *`    | supervisor                                 | import data from a CSV file    | bulk-load or sync clients/trainers from other tools                     |
| `* *`    | supervisor                                 | export data to a CSV file      | share or archive data outside of the app                                |
| `* *`    | supervisor                                 | clear all entries              | reset the system for a new term/season                                  |
| `*`      | supervisor                                 | undo the last command          | quickly recover from accidental deletions/edits                         |
| `*`      | supervisor                                 | see a time/day-based handover view | know which clients’ requirements are most relevant right now        |

### Use cases

(For all use cases below, the **System** is `GymOps` and the **Actor** is the `supervisor`, unless specified otherwise)

**Use case: Add a client to a trainer**

**MSS**

1.  Supervisor requests to list trainers.
2.  GymOps shows a list of trainers with index numbers.
3.  Supervisor issues the command to add a client, specifying a trainer index.
4.  GymOps validates the input and adds the client assigned to the specified trainer.

    Use case ends.

**Extensions**

* 2a. The trainer list is empty.

   * 2a1. GymOps shows an empty list message.

      Use case resumes at step 1.

* 3a. The trainer index is invalid (not a number, missing, or out of bounds).

   * 3a1. GymOps shows an error message.

      Use case resumes at step 2.

* 3b. The client’s phone number already exists in the system.

   * 3b1. GymOps rejects the command and shows an error message.

      Use case resumes at step 2.


**Use case: Reassign a client to another trainer**

**MSS**

1.  Supervisor requests to list clients.
2.  GymOps shows a list of clients with index numbers.
3.  Supervisor requests to list trainers.
4.  GymOps shows a list of trainers with index numbers.
5.  Supervisor issues the command to reassign a client index to a new trainer index.
6.  GymOps validates the input and updates the client’s assigned trainer.

   Use case ends.

**Extensions**

* 2a. The client list is empty.

   * 2a1. GymOps shows an empty list message.

      Use case resumes at step 1.

* 5a. The client index is invalid.

   * 5a1. GymOps shows an error message.

      Use case resumes at step 2.

* 5b. The trainer index is invalid.

   * 5b1. GymOps shows an error message.

      Use case resumes at step 4.

* 6a. The client is already assigned to the specified trainer.

   * 6a1. GymOps rejects the command and shows an error message.

      Use case resumes at step 4.


**Use case: Update a client’s daily calories (target + intake)**

**MSS**

1.  Supervisor requests to find clients by name.
2.  GymOps shows a list of matching clients.
3.  Supervisor identifies the relevant client and issues a command to set the client’s calorie target.
4.  GymOps updates the client’s calorie target.
5.  Supervisor issues a command to log calorie intake for the same client.
6.  GymOps adds the logged intake to the client’s total intake for the day.

   Use case ends.

**Extensions**

* 2a. No clients match the search keywords.

   * 2a1. GymOps shows an empty results message.

      Use case ends.

* 3a. The calorie target value is invalid (not a number or out of the accepted range).

   * 3a1. GymOps rejects the command and shows an error message.

      Use case resumes at step 2.

* 5a. The logged intake value is invalid (not a positive integer).

   * 5a1. GymOps rejects the command and shows an error message.

      Use case resumes at step 2.


**Use case: Delete a trainer**

**MSS**

1.  Supervisor requests to list trainers.
2.  GymOps shows a list of trainers with index numbers.
3.  Supervisor issues the command to delete a trainer by index.
4.  GymOps deletes the trainer.

   Use case ends.

**Extensions**

* 3a. The given trainer index is invalid.

   * 3a1. GymOps shows an error message.

      Use case resumes at step 2.

* 4a. The trainer still has assigned clients.

   * 4a1. GymOps rejects the deletion and informs the supervisor to reassign or delete those clients first.

      Use case resumes at step 2.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ (Windows, Linux, macOS) as long as it has Java `17` or above installed.
2.  Should be optimized for a CLI-centric workflow (i.e., common operations are achievable with a small number of commands and without requiring mouse input).
3.  For a dataset of up to 100 trainers and 1000 clients, typical commands (list, find, reassign, set-cal, log-cal, view-progress) should complete within 1 second on a typical laptop.
4.  Data should be persisted locally and remain intact after restarting the application.
5.  The system should not require an internet connection for normal operation.
6.  The system is single-user (supervisor-only) and does not require multiple logins or role-based access control.
7.  CSV import/export (if implemented) should preserve all relevant fields needed for operations (trainer/client identities and client tracking fields) without loss.

### Glossary

* **Supervisor**: The primary (and only) intended user of GymOps; manages allocations and operational coordination.
* **Trainer**: A staff member who trains clients. In GymOps, trainers are the parent entity that clients are assigned to.
* **Client**: A gym member assigned to exactly one trainer at any point in time.
* **Assignment**: The association linking a client to a trainer.
* **Reassignment**: Moving a client’s assignment from one trainer to another.
* **Workout focus**: A high-level muscle group emphasis (e.g., Chest, Back, Legs, Core), not specific exercises.
* **Calorie target**: A client’s intended daily calorie goal (kcal).
* **Calorie intake**: The calories logged as consumed by a client for the day (kcal).
* **Remark**: A free-text operational note about a client (e.g., injuries, payment flags).
* **CSV**: Comma-Separated Values format used for import/export.
* **Mainstream OS**: Windows, Linux, macOS.
* **GymOps**: The name of the application.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
