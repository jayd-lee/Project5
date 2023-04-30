import java.util.Scanner;

public class Tools {
    static Scanner scan = new Scanner(System.in);

    public static String username() {
        System.out.println("Enter your username: ");
        String username = scan.nextLine();
        return username;
    }

    public static String password() {
        System.out.println("Enter your password: ");
        String password = scan.nextLine();
        return password;
    }

    public static String email() {
        System.out.println("Enter your email: ");
        String email = scan.nextLine();
        return email;
    }










}
