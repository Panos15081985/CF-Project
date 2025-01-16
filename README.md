# Dance2Impress

## Ακολουθούν Οδηγίες Ανάπτυξης στα Ελληνικά και Αγγλικά

Αυτές οι οδηγίες παρέχουν βήμα-βήμα οδηγίες για την ανάπτυξη (Deploy) και την κατασκευή (Build) της εφαρμογής Dance2Impress. Ακολουθήστε τα παρακάτω βήματα προσεκτικά για να εξασφαλίσετε μια επιτυχημένη ρύθμιση.

## Περιγραφή

Η εφαρμογή αφορά μια σχολή χορού, προσφέροντας τη δυνατότητα διαχείρισης και αναζήτησης με διάφορα φίλτρα για τους χορευτές, τους προπονητές και τα μαθήματα που διεξάγονται στη σχολή. Οι χρήστες της εφαρμογής χωρίζονται σε δύο κατηγορίες, με διαφορετικά επίπεδα πρόσβασης:

### • Διαχειριστής:
Ο διαχειριστής διαθέτει πλήρη εξουσιοδότηση (authorization) σε όλα τα endpoints της εφαρμογής. Έχει τη δυνατότητα να:
- Προσθέτει, ενημερώνει και διαγράφει χορευτές, προπονητές και μαθήματα.
- Πραγματοποιεί αναζητήσεις για χορευτές, προπονητές και μαθήματα, χρησιμοποιώντας διαφορετικά φίλτρα.

### • Προπονητές:
Κάθε προπονητής έχει πρόσβαση μόνο στους χορευτές που συμμετέχουν στα μαθήματα που διδάσκει. Μπορεί να:
- Διαχειρίζεται τα στοιχεία των χορευτών αυτών.
- Πραγματοποιεί αναζητήσεις για χορευτές που αφορούν αποκλειστικά τα μαθήματα που διδάσκει.

Οι προπονητές δεν έχουν πρόσβαση σε όλα τα endpoints της εφαρμογής.

## Login

Για να αποκτήσετε πρόσβαση στην εφαρμογή, ακολουθήστε τα παρακάτω βήματα:

1. Ανοίξτε την εφαρμογή στον browser σας (π.χ., [http://localhost:4200](http://localhost:4200)).
2. Χρησιμοποιήστε τα εξής στοιχεία σύνδεσης:

### Για τον Διαχειριστή:
- **Username:** panos
- **Password:** Panos123!@

### Για έναν Προπονητή:
- **Username:** celina
- **Password:** Celina123!@

## Προαπαιτούμενα

- Git εγκατεστημένο στον υπολογιστή σας.
- MySQL Workbench εγκατεστημένο.
- IntelliJ IDEA για την ανάπτυξη του backend.
- Node.js και npm για το frontend.
- Ένας web browser για τη δοκιμή του Swagger και του frontend.

## Βήμα 1: Κλωνοποίηση του Repository
1. Μεταβείτε στο repository του GitHub.
2. Πατήστε το πράσινο κουμπί "Code".
3. Αντιγράψτε το URL HTTPS.
4. Ανοίξτε ένα τερματικό στον υπολογιστή σας και εκτελέστε την παρακάτω εντολή:

    ```bash
    git clone https://github.com/Panos15081985/CF-Project.git
    ```

5. Δύο φάκελοι (backend και frontend) θα κατέβουν τώρα στον υπολογιστή σας.
   
## Βήμα 2: Ρύθμιση της Βάσης Δεδομένων MySQL

1. Ανοίξτε το MySQL Workbench.
2. Δημιουργήστε μια νέα βάση δεδομένων με το: 
  - Όνομα: `dance2impress-db`,
  - Charset: utf8mb4,
  - Collation: urf8mb4_0900_ai_ci

3. Δημιουργήστε έναν χρήστη για τη βάση και δώστε του τα κατάλληλα δικαιώματα.
   - Παράδειγμα διαπιστευτηρίων:
     - Όνομα Χρήστη: `userdb6`
     - Κωδικός Πρόσβασης: `12345`
   - Δωστε στο χρηστη δικαιώματα πρόσβασης: Select *ALL*
       
## Βήμα 3: Ρύθμιση του Backend

1. Ανοίξτε τον φάκελο `backend` στο IntelliJ IDEA.
2. Μεταβείτε στο `src/main/resources/application-test.properties`.
3. Ενημερώστε τα παρακάτω πεδία με τα διαπιστευτήρια της βάσης σας:
   - `${MYSQL_USER:userdb6}`
   - `${MYSQL_PASSWORD:12345}`
4. Εκτελέστε την εφαρμογή backend:
   - Πατήστε το `bootRun` για να ξεκινήσει το πρόγραμμα.
5. Την πρώτη φορά που θα τρέξει, η εφαρμογή θα δημιουργήσει αυτόματα τους πίνακες της βάσης δεδομένων από το μοντέλο.
6. Σταματήστε την εφαρμογή.

## Βήμα 4: Εισαγωγή Δεδομένων στη Βάση

1. Επιστρέψτε στο `src/main/resources/application-test.properties`.
2. Αφαιρέστε τα σχόλια από τις παρακάτω γραμμές για να φορτώσετε τα αρχικά δεδομένα:
   ```properties
   spring.sql.init.mode=always
   spring.sql.init.data-locations=classpath:sql/guardians.sql,classpath:sql/contact_details.sql,classpath:sql/coaches.sql,classpath:sql/courses.sql,classpath:sql/dancers.sql,classpath:sql/dancers_courses.sql
3. Τρέξτε `bootRun` ξανά την εφαρμογή.

## Βήμα 5: Πρόσβαση στη Τεκμηρίωση Swagger

Όταν το backend τρέχει, μπορείτε να έχετε πρόσβαση στη τεκμηρίωση Swagger στη διεύθυνση:
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Βήμα 6: Ρύθμιση και Εκτέλεση του Frontend

1. Ανοίξτε τον φάκελο frontend σε ένα τερματικό.
2. Εκτελέστε τις παρακάτω εντολές:
   ```bash
   npm install
   ng serve
   ```
3. Η εφαρμογή frontend θα τρέχει τώρα στη διεύθυνση: http://localhost:4200

## Πρόσθετες Σημειώσεις

- Βεβαιωθείτε ότι το backend τρέχει πριν ξεκινήσετε το frontend.
- Θύρα backend: 8080.
- Θύρα frontend: 4200.

# English

## Prerequisites

- Git installed on your machine.
- MySQL Workbench installed.
- IntelliJ IDEA for backend development.
- Node.js and npm for frontend.
- A web browser for Swagger and frontend testing.

## Step 1: Clone the Repository

1. Go to the GitHub repository.
2. Click the green Code button.
3. Copy the HTTPS URL.
4. Open a terminal on your computer and run the following command:

   ```bash
   git clone https://github.com/Panos15081985/CF-Project.git
   ```
 5. Two folders (backend and frontend) will now be downloaded to your computer.  

## Step 2: Set Up the MySQL Database

1. Open MySQL Workbench.
2. Create a new database with: the
  - name: `dance2impress-db`
  - Charset: utf8mb4,
  - Collation: urf8mb4_0900_ai_ci
   
4. Create a user for the database and assign it appropriate privileges.
   - Example credentials:
     - Username: `userdb6`
     - Password: `12345`

## Step 3: Configure the Backend

1. Open the backend folder in IntelliJ IDEA.
2. Navigate to `src/main/resources/application-test.properties`.
3. Update the following placeholders with your database credentials:
   - `${MYSQL_USER:userdb6}`
   - `${MYSQL_PASSWORD:12345}`
4. Run the backend application:
   - Click on `bootRun` to start the program.
5. On the first run, the application will automatically create the database tables from the model.
6. Stop the application.

Step 4: Initialize Data in the Database
1.	Go back to src/main/resources/application-test.properties.
2.	Uncomment the following lines to load initial data:
3.	spring.sql.init.mode=always
```spring.sql.init.data-locations=classpath:sql/guardians.sql,classpath:sql/contact_details.sql,classpath:sql/coaches.sql,classpath:sql/courses.sql,classpath:sql/dancers.sql,classpath:sql/dancers_courses.sql
```
4.	Run the program again. This will populate the database tables with sample data.
5.	After the data is loaded, comment out the above lines again to avoid reloading data on every run.
6.	Run the program one more time.

## Step 5: Access the Swagger Documentation

Once the backend is running, you can access the Swagger documentation at:
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Step 6: Set Up and Run the Frontend
1.	Open the frontend folder in a terminal.
2.	Run the following commands:
3.	```npm install```
     ```ng serve```
4.	The frontend application will now be running at:
http://localhost:4200

## Additional Notes

- Make sure the backend is running before starting the frontend.
- Default backend port: 8080.
- Default frontend port: 4200.
