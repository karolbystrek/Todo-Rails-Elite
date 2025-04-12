# TodoRails-Elite

A simple yet powerful todo list application built with Spring Boot. This project serves as the final assignment for the "Amazon Java Junior Developer" course.

## Tech Stack

* **Backend:** Java 21, Spring Boot 3.4.4
* **Build Tool:** Maven
* **Database:** MySQL
* **Frontend:** Thymeleaf
* **Security:** Spring Security

## Getting Started

Follow these instructions to get a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

* Java Development Kit (JDK) 21 or later
* Maven 3.6+ (Recommended: 3.9+ for Java 21)
* Git
* MySQL Server running locally (or accessible)
* Set the `MYSQL_PASSWORD` environment variable with your MySQL root password.
  * On macOS/Linux: `export MYSQL_PASSWORD='your_password'`
  * On Windows (Command Prompt): `set MYSQL_PASSWORD=your_password`
  * On Windows (PowerShell): `$env:MYSQL_PASSWORD='your_password'`
  * Alternatively, configure it in your IDE's run configuration.

### Installation & Running

1. **Clone the repository:**

    ```bash
    git clone https://github.com/karolbystrek/Todo-Rails-Elite.git
    cd TodoRails-Elite
    ```

2. **Build the project:**

    ```bash
    mvn clean install
    ```

3. **Run the application:**
    * Using Maven:

        ```bash
        mvn spring-boot:run
        ```

    * Alternatively, you can run the packaged JAR file (ensure `MYSQL_PASSWORD` is set in the environment where you run this):

        ```bash
        java -jar target/todo.rails.elite-1.0.0.jar
        ```

4. **Access the application:**
    Open your web browser and navigate to `http://localhost:8080`. The application uses the `todorails` database, which will be created automatically if it doesn't exist (`spring.jpa.hibernate.ddl-auto=update`).

## Usage

1. **Access the Application:** Open your web browser and navigate to `http://localhost:8080`.
2. **Login/Register:**
    * If you are a new user, look for a "Register" link to create an account. You will need to provide a username, email and password.
    * If you already have an account, enter your credentials on the login page.
3. **View Todo List:** After logging in, you should be directed to your main todo list page, displaying the dashboard containing list of tasks.
4. **Add a Task:** Find the green input button ("+") to create a new task. Enter the task description with a due date and click submit.
5. **Manage Tasks:**
    * **Mark as Complete:** Tasks have an icon to mark them as done.
    * **Edit Task:** There is an edit icon to modify the task description.
    * **Delete Task:** Look for a delete icon next to each task to remove it.
6.**Logout:** When finished, find the "Logout out" button in the bottom-left corrner.
