import java.io.*;
import java.util.Scanner;

public class DSA_Mini_Project {

    static class Game {
        String title, genre, platform;
        int releaseYear;

        public Game(String title, String genre, String platform, int releaseYear) {
            this.title = title;
            this.genre = genre;
            this.platform = platform;
            this.releaseYear = releaseYear;
        }


        public String toString() {
            return title + " | " + genre + " | " + platform + " | " + releaseYear;
        }

        public String toFileString() {
            return title + "|" + genre + "|" + platform + "|" + releaseYear;
        }

        public static Game fromFileString(String line) {
            String[] parts = line.split("\\|");
            if (parts.length != 4) return null;
            return new Game(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
        }
    }

    static class Node {
        Game data;
        Node next;

        public Node(Game data) {
            this.data = data;
            this.next = null;
        }
    }

    static class GameLinkedList {
        Node head = null;

        void insert(Game game) {
            Node newNode = new Node(game);
            if (head == null) {
                head = newNode;
                return;
            }
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }

        void deleteByTitle(String title) {
            if (head == null) return;

            if (head.data.title.equalsIgnoreCase(title)) {
                head = head.next;
                return;
            }

            Node current = head;
            while (current.next != null && !current.next.data.title.equalsIgnoreCase(title)) {
                current = current.next;
            }

            if (current.next != null) {
                current.next = current.next.next;
            }
        }

        void display() {
            if (head == null) {
                System.out.println("No games in the directory.");
                return;
            }
            Node temp = head;
            while (temp != null) {
                System.out.println(temp.data);
                temp = temp.next;
            }
        }

        void saveToFile(String filename) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
                Node temp = head;
                while (temp != null) {
                    bw.write(temp.data.toFileString());
                    bw.newLine();
                    temp = temp.next;
                }
            } catch (IOException e) {
                System.out.println("Error saving file: " + e.getMessage());
            }
        }

        void loadFromFile(String filename) {
            head = null;
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("File not found. Starting with empty list.");
                return;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    Game game = Game.fromFileString(line);
                    if (game != null) {
                        insert(game);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GameLinkedList list = new GameLinkedList();
        String filename = "game_directory.txt";

        list.loadFromFile(filename);

        int choice;
        do {
            System.out.println("-------------------------------");
            System.out.println("| Game Directory Management    |");
            System.out.println("| 1. Display all games         |");
            System.out.println("| 2. Add new game              |");
            System.out.println("| 3. Delete a game by title    |");
            System.out.println("| 0. Save and Exit             |");
            System.out.println("|Enter your choice:            |");
            System.out.println("-------------------------------");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    list.display();
                    break;
                case 2:
                    System.out.print("Enter game title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter genre: ");
                    String genre = sc.nextLine();
                    System.out.print("Enter platform: ");
                    String platform = sc.nextLine();
                    System.out.print("Enter release year: ");
                    int year = sc.nextInt();
                    sc.nextLine();
                    list.insert(new Game(title, genre, platform, year));
                    System.out.println("Game added.");
                    break;
                case 3:
                    System.out.print("Enter game title to delete: ");
                    String deleteTitle = sc.nextLine();
                    list.deleteByTitle(deleteTitle);
                    System.out.println("Game deleted (if found).");
                    break;
                case 0:
                    list.saveToFile(filename);
                    System.out.println("Data saved. Exiting.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);

        sc.close();
    }
}
