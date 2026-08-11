# OnlineExam

OnlineExam is a web application where students can give online quizzes and teachers can add the questions for them.

A teacher logs in and adds subjects and multiple-choice questions. A student logs in, picks a subject and attempts a timed quiz. After submitting, the student gets the score, and every attempt is saved so it can be shown later on the dashboard and on a common leaderboard.

It is written in Java using JSP and Servlets, the data is stored in a MySQL database, and it runs on an Apache Tomcat server.

## Screenshots

| Home page | Student dashboard |
|---|---|
| ![Home page](screenshots/homepage.png) | ![Student dashboard](screenshots/scorecard.png) |

| Choosing a subject | Quiz with the timer |
|---|---|
| ![Choosing a subject](screenshots/subjectchoicepage.png) | ![Quiz with the timer](screenshots/quiz.png) |

## What the app does

There are three kinds of users, student, teacher and admin, and each one gets its own section after logging in.

### Student

- Register and log in. Passwords are stored in a hashed form, not as plain text.
- Pick a subject from a dropdown and start a timed multiple-choice quiz.
- The quiz has a countdown timer of 40 seconds per question and submits on its own when the time is over.
- After submitting, the student sees the score as a percentage and can check which answers were right and which were wrong.
- A results page lists all the quizzes the student has attempted.

### Teacher

- Create, rename and delete their own subjects.
- Add, edit and delete questions under a subject. The correct answer is chosen with a radio button so it always matches one of the four options.
- See all the attempts that students have made on their subjects.

### Admin

- A dashboard showing how many students, teachers, subjects, questions and attempts there are.
- View and delete students, teachers and subjects.
- See every quiz attempt in the system.

There is also a leaderboard that lists the top attempts ordered by percentage and then by score.

## Tools and technologies

- Java 17
- JSP and Servlets on Apache Tomcat 9
- JDBC with prepared statements
- MySQL 8
- HTML and CSS, one stylesheet, no CSS framework

## How the project is organised

The Java code follows the MVC pattern and is split into packages:

- model, plain Java classes for the data
- dao, the classes that run the SQL queries, one per table
- controller, the Servlets that handle the requests
- filter, an AuthFilter that blocks the student, teacher and admin pages unless you are logged in as that kind of user
- util, helper classes for password hashing and form input

The JSP files only display the pages. All the SQL stays inside the DAO classes.

```
OnlineExam/
  database.sql
  src/main/java/
    db.properties
    com/onlineexam/
      model/
      dao/
      controller/
      filter/
      util/
  src/main/webapp/
    index.jsp
    assets/css/style.css
    WEB-INF/
      web.xml
      lib/
      views/
```

## Database

The database is called college and has these tables:

- student, teacher, admin, the user accounts
- subject, linked to the teacher who created it
- question, four options and the correct answer, linked to a subject
- result, one row per quiz attempt, linked to a student and a subject

The script that creates all of it with some sample data is in database.sql.

## How to run it

You need JDK 17, Apache Tomcat 9, MySQL 8 and Eclipse.

1. Create the database:

   ```
   mysql -u root -p < database.sql
   ```

2. Open src/main/java/db.properties and set db.user and db.password to your own MySQL username and password. Change the port in db.url if your MySQL does not run on 3306.
3. Import the project into Eclipse, add it to a Tomcat 9 server and start it.
4. Open http://localhost:8080/OnlineExam/ in the browser.

The demo accounts created by the script are admin@tiet.edu, prof.sharma@tiet.edu and adwitiya@tiet.edu, with the passwords Admin@123, Teacher@123 and Student@123.

## License

MIT License, see the LICENSE file.

## Author

Adwitiya Shukla
