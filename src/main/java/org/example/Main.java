package org.example;

// *******************************************
// * Author :- Vishal Kumar Maurya (2022580) *
// *******************************************
// package org.example;

import java.util.*;
// import java.io.FileNotFoundException;
// import java.io.PrintWriter;

public class Main {
    class Book {
        private String bookID;
        private String title;
        private String author;
        private int totalCopies;
        private int availableCopies;
        private long issueTime;
        private long returnTime;

        public Book(String bookID, String title, String author, int totalCopies) {
            this.bookID = bookID;
            this.title = title;
            this.author = author;
            this.totalCopies = totalCopies;
            this.availableCopies = totalCopies;
        }

        public void setIssueTime() {
            long startme = System.currentTimeMillis();
            this.issueTime = startme;
        }

        public long getIssueTime() {
            return issueTime;
        }

        public void setReturnTime() {
            long endtme = System.currentTimeMillis();
            this.returnTime = endtme;
        }

        public long getReturnTime() {
            return returnTime;
        }

        public String getBookID() {
            return bookID;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public int getTotalCopies() {
            return totalCopies;
        }

        public int getAvailableCopies() {
            return availableCopies;
        }

        public void decreaseAvailableCopies() {
            if (availableCopies > 0) {
                availableCopies--;
            }
        }

        public void increaseAvailableCopies() {
            if (availableCopies < totalCopies) {
                availableCopies++;
            }
        }
    }

    class Member {
        private String phoneNumber;
        private String name;
        private int age;
        private double balance;
        private List<String> issuedBooks;

        public Member(String phoneNumber, String name, int age) {
            issuedBooks = new ArrayList<>();
            this.phoneNumber = phoneNumber;
            this.name = name;
            this.age = age;
            this.balance = 0.0;
        }

        public void addIssuedBook(String bookID) {
            issuedBooks.add(bookID);
        }

        public List<String> getIssuedBooks() {
            return issuedBooks;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public double getBalance() {
            return balance;
        }

        public double setBalance(double amount) {
            balance = amount;
            return balance;
        }

        public void increaseBalance(double amount) {
            balance += amount;
        }

        public void decreaseBalance(double amount) {
            if (balance >= amount) {
                balance -= amount;
            }
        }
    }

    class LibrarySystem {
        private Map<String, Book> books;
        private Map<String, Member> members;
        private Member loggedInMember;

        public LibrarySystem() {
            books = new HashMap<>();
            members = new HashMap<>();
            loggedInMember = null;
        }
        private String generateRandomBookID() {
            Random rand = new Random();
            int min = 1000;
            int max = 9999;
            int randomNum = rand.nextInt((max - min) + 1) + min;
            return String.valueOf(randomNum);
        }

        public void addBook(String bookID, String title, String author, int totalCopies) {
            Book book = new Book(bookID, title, author, totalCopies);
            books.put(bookID, book);
            System.out.println("\nBook with title :- "+title+" and ID :- "+bookID+" has been added successfully.");
        }

        public boolean isBookIssued(String bookID) {
            for (Member member : members.values()) {
                if (member.getIssuedBooks().contains(bookID)) {
                    return true;
                }
            }
            return false;
        }

        public void removeBook(String bookID) {
            if (books.containsKey(bookID)) {
                Book book = books.get(bookID);
                if (book.getAvailableCopies() == 0 && isBookIssued(bookID) == true) {
                    System.out.println("\nCannot remove the book because it has been issued already.");
                } else if (book.getAvailableCopies() == 0 && isBookIssued(bookID) != true) {
                    books.remove(bookID);
                    System.out.println("Data of book removed from the system!");
                } else if (book.getAvailableCopies() > 0) {
                    book.decreaseAvailableCopies();
                    System.out.println("\nOne copy of the book removed successfully.");
                }
            } else {
                System.out.println("\nBook not found.");
            }
        }

        public void registerMember(String phoneNumber, String name, int age) {
            Member member = new Member(phoneNumber, name, age);
            members.put(phoneNumber, member);
            System.out.println("\nMember registered successfully.");
        }

        public void removeMember(String phoneNumber) {
            if (members.containsKey(phoneNumber)) {
                Member member = members.get(phoneNumber);

                if (member.getIssuedBooks().isEmpty() && member.getBalance() == 0.0 ) {
                    members.remove(phoneNumber);
                    System.out.println("\nMember removed successfully.");
                } else {
                    System.out.println("\nCannot remove the member. They have issued books or fines pending.");
                }
            } else {
                System.out.println("\nMember not found.");
            }
        }


        public boolean enterAsMember(String phoneNumber, String name) {
            if (members.containsKey(phoneNumber)) {
                Member member = members.get(phoneNumber);
                if (member.getName().equals(name)) {
                    loggedInMember = member;
                    System.out.println("\n Logged in as " + loggedInMember.getName() + " !");
                    return true;
                } else {
                    System.out.println("\nIncorrect Credentials Cannot Login !");
                    return false;
                }
            } else {
                System.out.println("\nMember not found. Please register first.");
                return false;
            }
        }


        public void issueBook(String bookID) {
            if (loggedInMember == null) {
                System.out.println("\nPlease log in as a member first.");
                return;
            }
            if (loggedInMember.getBalance() > 0.0) {
                System.out.println("\nYou have a balance due of RS. " + loggedInMember.getBalance()
                        + " Please clear it before issuing a new book.");
                return;
            }
            if (books.containsKey(bookID)) {
                Book book = books.get(bookID);
                if (book.getAvailableCopies() > 0) {
                    if (loggedInMember.getIssuedBooks().size() >= 2) {
                        System.out.println("\nYou cannot issue more than 2 books at a time.");
                        return;
                    } else {
                        book.decreaseAvailableCopies();
                        book.setIssueTime();
                        loggedInMember.addIssuedBook(bookID);
                        System.out.println("\nBook issued successfully.");
                    }
                } else {
                    System.out.println("\nNo available copies of this book.");
                }
            } else {
                System.out.println("\nBook not found.");
            }
        }

        public void returnBook(String bookID) {
            if (loggedInMember == null) {
                System.out.println("\nPlease log in as a member first.");
                return;
            }
            if (books.containsKey(bookID)) {
                Book book = books.get(bookID);
                // if (book.getAvailableCopies() > 0) {
                if (loggedInMember.getIssuedBooks().contains(bookID)) {
                    long endTime = System.currentTimeMillis();
                    long lapsed = (endTime - book.getIssueTime()) / 1000;
                    if (lapsed > 10) {
                        double fine = (lapsed - 10) * 3.0;
                        loggedInMember.increaseBalance(fine);
                        book.increaseAvailableCopies();
                        book.setReturnTime();
                        loggedInMember.getIssuedBooks().remove(bookID);
                        System.out
                                .println("\nBook returned and a fine of " + fine + " rupees is added to your balance.");
                    } else {
                        book.increaseAvailableCopies();
                        book.setReturnTime();
                        loggedInMember.getIssuedBooks().remove(bookID);
                        System.out.println("\nBook returned successfully with no balance.");
                    }
                } else {
                    System.out.println("\nYou have not issued this book.");
                }
            } else {
                System.out.println("\nBook not found.");
            }
        }

        public void payfine() {
            List<String> issuedBooks = loggedInMember.getIssuedBooks();
            double totalFinePaid = 0.0;

            for (String bookID : issuedBooks) {
                if (books.containsKey(bookID)) {
                    Book book = books.get(bookID);
                    long endTime = System.currentTimeMillis();
                    long lapsed = (endTime - book.getIssueTime()) / 1000;
                    if (lapsed > 10) {
                        double fine = Math.abs(lapsed - 10) * 3.0;
                        totalFinePaid += fine;
                    }
                    book.setIssueTime();
                }
            }
            System.out.println("\nFines paid Successfully for Member " + loggedInMember.getName() + ".");
            System.out.println("Total Fine : " + totalFinePaid + " rupees.");
            System.out.println("Total balance : " + loggedInMember.getBalance() + " rupees.");
            loggedInMember.setBalance(0.0);
            System.out.println("Balance and fine after payment: " + loggedInMember.getBalance() + " rupees.");
        }

        public void listBooks() {
            System.out.println("\n List of available books:");
            for (Book book : books.values()) {
                System.out.println(" Book ID: " + book.getBookID());
                System.out.println(" Title: " + book.getTitle());
                System.out.println(" Author: " + book.getAuthor());
                System.out.println(" Available Copies: " + book.getAvailableCopies());
                System.out.println();
            }
        }

        public void listMembers() {
            System.out.println("\nList of registered members:");
            for (Member member : members.values()) {
                System.out.println("\tPhone Number: " + member.getPhoneNumber());
                System.out.println("\tName: " + member.getName());
                System.out.println("\tAge: " + member.getAge());
                System.out.println("\tBalance Due: " + member.getBalance());
                System.out.println();
            }
        }

        public void viewIssuedBooksAndFinesForMember() {
            if (loggedInMember == null) {
                System.out.println("\nPlease log in as a member first.");
                return;
            }

            List<String> issuedBooks = loggedInMember.getIssuedBooks();
            if (issuedBooks.isEmpty()) {
                System.out.println("\nYou have not issued any books yet.");
            } else {
                System.out.println("\nBooks issued by " + loggedInMember.getName() + ":");
                for (String bookID : issuedBooks) {
                    if (books.containsKey(bookID)) {
                        Book book = books.get(bookID);
                        long endTime = System.currentTimeMillis();
                        long lapsed = (endTime - book.getIssueTime()) / 1000;
                        System.out.println("\tBook ID: " + book.getBookID());
                        System.out.println("\tTitle: " + book.getTitle());
                        System.out.println("\tAuthor: " + book.getAuthor());
                        if (lapsed > 10) {
                            double fine = (lapsed - 10) * 3.0;
                            System.out.println("\tFine: " + (fine) + " rupees");
                        } else {
                            System.out.println("\tFine: 0.0 rupees");
                        }
                        System.out.println("\tBalance: " + loggedInMember.getBalance() + " rupees");
                        System.out.println();
                    }
                }
            }
        }

        public void viewMembersWithBooksAndFines() {
            System.out.println("\nMembers with issued books and fines :-\n");
            for (Member member : members.values()) {
                System.out.println("Member Name: " + member.getName());
                System.out.println("Phone Number: " + member.getPhoneNumber());

                List<String> issuedBooks = member.getIssuedBooks();
                if (issuedBooks.isEmpty()) {
                    System.out.println("\tIssued Books: None");
                    System.out.println("\tTotal Fine: 0.0 rupees");
                } else {
                    System.out.println("\nIssued Books:");
                    double totalFine = 0.0;
                    for (String bookID : issuedBooks) {
                        if (books.containsKey(bookID)) {
                            Book book = books.get(bookID);
                            long endTime = System.currentTimeMillis();
                            long lapsed = (endTime - book.getIssueTime()) / 1000;
                            System.out.println("\t\t Book Title: " + book.getTitle());
                            if (lapsed > 10) {
                                double fine = (lapsed - 10) * 3.0;
                                totalFine += fine;
                                System.out.println("\t\t Fine: " + (fine + loggedInMember.getBalance()) + " rupees\n");
                            } else {
                                System.out.println("\t\tFine: " + (loggedInMember.getBalance()) + " rupees\n");
                            }
                        }
                    }
                    System.out.println("\tBalance: " + loggedInMember.getBalance() + " rupees");
                    System.out.println("\nTotal Fine: " + (totalFine + loggedInMember.getBalance()) + " rupees\n");
                }
                System.out.println();
            }
        }

        public void listIssuedBooks() {
            if (loggedInMember == null) {
                System.out.println("\nPlease log in as a member first.");
                return;
            }

            List<String> issuedBooks = loggedInMember.getIssuedBooks();
            if (issuedBooks.isEmpty()) {
                System.out.println("\nYou have not issued any books yet.");
            } else {
                System.out.println("\nBooks issued by " + loggedInMember.getName() + ":");
                for (String bookID : issuedBooks) {
                    if (books.containsKey(bookID)) {
                        Book book = books.get(bookID);
                        System.out.println("\tBook ID: " + book.getBookID());
                        System.out.println("\tTitle: " + book.getTitle());
                        System.out.println("\tAuthor: " + book.getAuthor());
                        System.out.println();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        LibrarySystem librarySystem = new Main().new LibrarySystem();
        Scanner scan = new Scanner(System.in);
        System.out.println(
                " \n *******************************************\n" +
                        " * Author :- Vishal Kumar Maurya (2022580) * \n" +
                        " *******************************************");
        System.out.println("________________________________________________ ");
        System.out.print(" Welcome to IIITD library Management System\n");
        System.out.print("________________________________________________ \n\n");
        System.out.println(" Enter your choice ;- ");
        System.out.println(" (1) To Enter as Librarian.  \n");
        System.out.println(" (2) To Enter as Member.  \n");
        System.out.println(" (3) To Exit from the Application.  \n");
        while (true) {
            try {
                System.out.print(" Enter your choice to Initialize the software ;-  ");
                int taken_input = scan.nextInt();
                scan.nextLine();
                if (taken_input == 1) {
                    System.out.println("\n What would you like to do ?");
                    System.out.println(
                            " (1) Add a book\r\n" + //
                                    " (2) Remove a book\r\n" + //
                                    " (3) Register a member\r\n" + //
                                    " (4) Remove a member\r\n" + //
                                    " (5) View all members along with their books and fines to be paid\r\n" + //
                                    " (6) View all books\r\n" + //
                                    " (7) Back");
                    while (true) {
                        try {
                            System.out.print("\n Enter your Input :- ");
                            int librarian_input = scan.nextInt();
                            scan.nextLine();
                            if (librarian_input == 1) {
                                System.out.print("Enter Book's Title :- ");
                                String title = scan.nextLine();
                                System.out.print("Enter Book's Author :- ");
                                String author = scan.nextLine();
                                System.out.print("Enter Total Copies Available :- ");
                                int totalCopies = scan.nextInt();
                                String bookID = librarySystem.generateRandomBookID();
                                scan.nextLine();
                                librarySystem.addBook(bookID, title, author, totalCopies);
                            } else if (librarian_input == 4) {
                                System.out.print("Enter Phone Number to remove Member :- ");
                                String phoneNumber = scan.nextLine();
                                librarySystem.removeMember(phoneNumber);
                            } else if (librarian_input == 3) {
                                System.out.print("Enter Name of the Member :- ");
                                String name = scan.nextLine();
                                System.out.print("Enter Age :- ");
                                int age = scan.nextInt();
                                scan.nextLine();
                                System.out.print("Enter Phone Number :- ");
                                String phoneNumber = scan.nextLine();
                                librarySystem.registerMember(phoneNumber, name, age);
                            } else if (librarian_input == 2) {
                                System.out.print("To remove the Book Enter Book ID :- ");
                                String bookID = scan.nextLine();
                                librarySystem.removeBook(bookID);
                            } else if (librarian_input == 5) {
                                librarySystem.viewMembersWithBooksAndFines();
                            } else if (librarian_input == 6) {
                                librarySystem.listBooks();
                            } else if (librarian_input == 7) {
                                System.out.println(" Going Back ... \n");
                                break;
                            } else {
                                System.out.println(" Error Not a Command Retry....\n");
                                continue;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Error: Invalid input! Please enter a valid Integer number.\n");
                            scan.nextLine();
                            continue;
                        }
                    }
                } else if (taken_input == 2) {
                    try {
                        System.out.print("Enter Username :- ");
                        String check_name = scan.nextLine();
                        System.out.print("Enter Phone No. :- ");
                        String check_phn = scan.nextLine();
                        if (librarySystem.enterAsMember(check_phn, check_name)) {
                            System.out.println(" Now what would you like to do ?\n");
                            System.out.println(
                                    " (1) List Available Books\r\n" +
                                            " (2) List My Books\r\n" +
                                            " (3) Issue book\r\n" +
                                            " (4) Return book\r\n" +
                                            " (5) Pay Fine\r\n" +
                                            " (6) Logout and Go Back");
                            while (1 > 0) {
                                System.out.print("Enter Choice here :- ");
                                int inp = scan.nextInt();
                                scan.nextLine();
                                if (inp == 1) {
                                    System.out.println("Listing Available Book Details :- ");
                                    librarySystem.listBooks();
                                } else if (inp == 2) {
                                    librarySystem.listIssuedBooks();
                                } else if (inp == 3) {
                                    System.out.print("Enter Book ID to issue Book :- ");
                                    String bookID = scan.nextLine();
                                    librarySystem.issueBook(bookID);
                                } else if (inp == 4) {
                                    System.out.print("Enter Book ID to Return Book :- ");
                                    String bookID = scan.nextLine();
                                    librarySystem.returnBook(bookID);
                                } else if (inp == 5) {
                                    System.out.println("\nPaying fine portal opening :- \n");
                                    librarySystem.payfine();
                                } else if (inp == 6) {
                                    System.out.println("\nLogging Out and Going Back ... ");
                                    break;
                                } else {
                                    System.out.println(" Wrong Input Enter Again! ..\n");
                                    continue;
                                }
                            }
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Error: Invalid input! Please enter a valid Integer number.\n");
                        scan.nextLine();
                        continue;
                    }
                } else if (taken_input == 3) {
                    System.out.println("\n Exiting....Thanks For Visiting !\n");
                    scan.close();
                    break;
                } else {
                    System.out.println("\n Error Not a Command Retry....");
                    continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Invalid input! Please enter a valid Integer number.\n");
                scan.nextLine();
                continue;
            }
        }
    }
}