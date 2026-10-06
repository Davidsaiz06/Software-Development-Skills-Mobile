# RefereeApp - Football Match Management System

A native Android application engineered in Java to assist football referees in logging real-time match events, managing timers, enforcing data integrity, and reviewing post-match reports.

Developed as the final project for the **Mobile Development (Software Development Skills)** course.

---

## Video Demonstration
* **Project Walkthrough Video:** [Link to Demo Video (e.g., YouTube / Google Drive)](https://youtu.be/your-video-link-here)  
*(A companion file `video_link.txt` with this direct URL is also provided in the repository).*

---

## Features & Implementation

### 1. Match Setup (`MainActivity`)
* **Input Validation:** Enforces mandatory entry of both Home and Away team names using `TextUtils.isEmpty()` and visual error hints (`setError()`).
* **Explicit Navigation:** Passes initial team data seamlessly to the match runner via `Intent` extras.

### 2. Live Match Control (`MatchActivity`)
* **Real-time Stopwatch:** Custom asynchronous timer built with `Handler` and `Runnable` running on the UI thread (`Looper.getMainLooper()`), with full start/pause/resume lifecycle control.
* **Smart Minute Autofill:** Automatically derives and pre-fills the current match minute from the running stopwatch when left blank.
* **Strict Incident Logging & Data Integrity:**
  * Quick-action buttons for Goals (+1 local/visitor) and disciplinary sanctions (Yellow and Red cards).
  * Enforces mandatory player identification (name or jersey number) before persisting any event.
  * Contextual team selection via `RadioGroup`.
* **Undo Functionality:** Live rollback capability (`undoLastEvent()`) that removes the latest incident from memory and reconciles scoreboard tallies if a goal was mistakenly awarded.
* **Tactile & Visual UX:** Immediate non-blocking feedback via Android `Toast` notifications.
* **Termination Safeguard:** Modal confirmation workflow via `AlertDialog.Builder` before ending the match to prevent accidental loss of live data.

### 3. Summary & Match Report (`ReportActivity`)
* **Dynamic Information Display:** Chronological incident record rendered through a dedicated `ListView`.
* **Custom BaseAdapter & ViewHolder Pattern:** Efficient layout inflation (`LayoutInflater`) and view recycling through `EventAdapter`, dynamically assigning event icons (`ic_goal`, `ic_yellow_card`, `ic_red_card`).
* **Aggregated Analytics:** Computes and displays total goals, yellow cards, and red cards across the match.
* **Empty State Handling:** Conditional rendering of an empty state indicator (`tv_empty_events`) when no incidents occurred.
* **Stack Hygiene:** Restart match navigation resets the activity stack cleanly using `Intent.FLAG_ACTIVITY_CLEAR_TOP` and `FLAG_ACTIVITY_NEW_TASK`.

---

## Course Rubric Compliance Matrix

| Requirement | Project Implementation | Component / File |
| :--- | :--- | :--- |
| **Component Functionality** | Text inputs, score counters, radio buttons, timer controls, dialogs, toasts | `MainActivity`, `MatchActivity` |
| **Multiple Views** | 3 interconnected Activities with data passing | `MainActivity` $\rightarrow$ `MatchActivity` $\rightarrow$ `ReportActivity` |
| **Information Display** | Custom `ListView` with row layout and dynamic drawables | `EventAdapter.java`, `event_item_row.xml` |
| **Software Architecture** | MVC architecture, `Serializable` models, memory leak prevention in `onDestroy` | `MatchEvent.java`, `MatchActivity.java` |

---

## Repository Structure

```text
├── Coursework/               # Tutorial exercises (Parts 1, 2, and 3)
├── Project/                  # Complete Android Studio project (RefereeApp)
│   ├── app/
│   │   ├── src/main/java/com/example/refereeapp/
│   │   │   ├── MainActivity.java
│   │   │   ├── MatchActivity.java
│   │   │   ├── ReportActivity.java
│   │   │   ├── EventAdapter.java
│   │   │   └── MatchEvent.java
│   │   └── src/main/res/layout/
│   │       ├── activity_main.xml
│   │       ├── activity_match.xml
│   │       ├── activity_report.xml
│   │       └── event_item_row.xml
│   ├── build.gradle.kts
│   └── settings.gradle.kts
├── Learning_diary.docx       # Milestone entries tracking development
├── video_link.txt            # Direct link to the project demonstration video
└── README.md                 # Project documentation and build instructions
