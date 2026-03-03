[![CI Status](https://github.com/se-edu/addressbook-level3/workflows/Java%20CI/badge.svg)](https://github.com/se-edu/addressbook-level3/actions)
[![codecov](https://codecov.io/gh/CS2103T-T17-1/tp/graph/badge.svg?token=DU35S1U7NP)](https://codecov.io/gh/CS2103T-T17-1/tp)

![Ui](docs/images/Ui.png)

# GymOps

GymOps is a **CLI-centric** desktop management application for **gym supervisors/managers** who oversee multiple trainers and their client assignments.

## Target user

Tech-savvy gym supervisors who:

* manage trainer-client assignments and occasional trainer substitutions
* operate primarily on desktop
* prefer fast, keyboard-based command input over menu-driven workflows

## Core value

High-speed management of a **hierarchical trainer → client** structure, plus lightweight tracking of each client’s:

* daily calorie target and intake
* current workout focus (e.g., Chest/Back/Legs/Core)

## Key workflows

* **Trainer substitution / handover**: reassign a client from Trainer A to Trainer B, while keeping the client’s plan and latest summary visible for quick handover.
* **Daily tracking**: set calorie targets, log intake, set workout focus, and view a one-line progress summary.
* **Data portability**: import/export `.csv` for sharing trainee information between supervisors/trainers or other tools.

## Command prefixes (Trainer vs Client)

GymOps distinguishes trainer and client operations with explicit commands/prefixes for speed and clarity.

Examples:

* Add a trainer: `add-trainer n/John Doe p/98765432 e/johndoe@example.com`
* Add a client assigned to a trainer: `add-client n/Alice Lim p/81234567 t/1`
* Reassign a client: `reassign c/1 t/2`

Calorie tracking:

* Set target: `set-cal c/1 target/2500`
* Log intake: `log-cal c/1 val/500`
* View summary: `view-progress 1`

## v1.0 feature list

* Trainer management: add-trainer, edit-trainer, delete-trainer, list-trainers
* Client management: add-client, edit-client, delete-client, list-clients (optional trainer filter)
* Assignment management: reassign client between trainers
* Tracking: set-cal (target), log-cal (intake), set-focus (workout focus), view-progress
* General: find (by name), remark (client notes), clear, help, exit

## Scope

GymOps focuses on **operational coordination**, not coaching:

* Tracks high-level workout focus and calories only
* Does **not** store exact exercises (e.g., dumbbell press) or exact meals (e.g., grilled steak)
* The app is for the supervisor; trainers/clients do not log in to GymOps

## Documentation

* User Guide: [docs/UserGuide.md](docs/UserGuide.md)
* Developer Guide: [docs/DeveloperGuide.md](docs/DeveloperGuide.md)


## Acknowledgement

This project is based on AddressBook-Level3 by the [SE-EDU initiative](https://se-education.org).
