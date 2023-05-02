import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.time.LocalTime;
import java.util.Scanner;


public class GUI extends JComponent implements Runnable {
    ArrayList<Account> customerList = new ArrayList<>();
    ArrayList<Account> sellerList = new ArrayList<>();

    private static ArrayList<String> messageList = new ArrayList<String>();
    private static ArrayList<String> storeList = new ArrayList<String>();
    private static ArrayList<String> invisibleToList = new ArrayList<String>();

    private static ArrayList<String> invisibleByList = new ArrayList<String>();
    private static ArrayList<String> blockedList = new ArrayList<String>();

    private static ArrayList<String> blockedMeList = new ArrayList<String>();
    private static ArrayList<String> invisibleList = new ArrayList<String>();

    private static String userName;
    private static String password;
    private static String email;
    private static boolean isSeller;
    private static String recipient;


    public void startProgram() {
        SwingUtilities.invokeLater(new GUI());
    }

    @Override
    public void run() {

        readAccounts();
        readStores();
        readBI();
        int option;
        option = welcome();
        if (option == 0) {
            option = login();
        } else if (option == 1) {
            try {
                option = signup();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            exit();
        }
        //user has either successfully logged in or signed up; now application window can be opened
        if (option == 0) {
            SellerGUIWindow();
        } else if (option == 1) {
            CustomerGUIWindow();
        }

    }

    public int welcome() {
        Object[] options = {"Login", "Signup", "Exit"};
        int choice = JOptionPane.showOptionDialog(null, "Please select an option", "Welcome to the messaging app!",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[2]);
        if (choice == 0) {
            return 0;
        } else if (choice == 1) {
            return 1;
        } else {
            return 2;
        }

    }

    public int login() {
        boolean loggingIn = true;
        Object[] options = {"Seller login", "Customer login", "Exit"};
        int choice = JOptionPane.showOptionDialog(null, "Please select an option", "Login options",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[2]);
        if (choice == 0 || choice == 1) {
            while (loggingIn) {
                String username = JOptionPane.showInputDialog(null, "Please enter your username");
                if (username != null) {
                    if ((existingUsername(username, choice)) != true) {
                        JOptionPane.showMessageDialog(null, "An account with this user does not exist!", "Login unsuccessful", JOptionPane.ERROR_MESSAGE);
                    } else {
                        String password = JOptionPane.showInputDialog(null, "Please enter your password");

                        if (password != null) {
                            if ((checkPassword(username, password, choice)) != true) {
                                JOptionPane.showMessageDialog(null, "Incorrect Password!", "Login unsuccessful", JOptionPane.ERROR_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Login successful!", "Logged In", JOptionPane.PLAIN_MESSAGE);
                                if (choice == 0) {
                                    isSeller = true;
                                    for (int i = 0; i < sellerList.size(); i++) {
                                        if ((sellerList.get(i).getUsername().equals(username)) && (sellerList.get(i).getPassword().equals(password))) {
                                            this.email = sellerList.get(i).getEmail();
                                        }
                                    }
                                } else {
                                    isSeller = false;
                                }
                                this.userName = username;
                                this.password = password;
                                loggingIn = false;
                            }

                        } else {
                            return -1;
                        }

                    }
                } else{
                    return -1;
                }

            }

        } else{
            exit();
            return -1;
        }

        return choice;

    }

    public int signup() throws IOException {
        boolean signup = true;
        Account newAccount;
        Object[] options = {"Seller signup", "Customer signup", "Exit"};
        int choice = JOptionPane.showOptionDialog(null, "Please select an option", "Signup options",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[2]);

        if (choice == 0 || choice == 1) {
            while (signup) {
                String email = JOptionPane.showInputDialog(null, "Please enter your email");
                if (email != null) {
                    if (existingEmail(email, choice) == true) {
                        JOptionPane.showMessageDialog(null, "Account with this email already exists!", "Signup unsuccessful", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        String username = JOptionPane.showInputDialog(null, "Please enter your username");
                        if (username != null) {
                            if (existingUsername(username, choice) == true) {
                                JOptionPane.showMessageDialog(null, "Username is taken!", "Signup unsuccessful", JOptionPane.PLAIN_MESSAGE);
                            } else {
                                String password = JOptionPane.showInputDialog(null, "Please enter your password");
                                if (password != null) {
                                    if (choice == 0) {
                                        newAccount = new Account(email, username, password);
                                    } else {
                                        newAccount = new Account(email, username, password);
                                    }
                                    if ((newAccount.getEmail() != null) && (newAccount.getPassword() != null) && (newAccount.getUsername() != null)) {
                                        if (choice == 0) {
                                            blockedList.add(newAccount.getUsername() + ", ");
                                            invisibleList.add(newAccount.getUsername() + ", ");
                                            sellerList.add(newAccount);
                                            storeList.add(newAccount.getUsername() + ", ");
                                            userName = username;
                                            this.password = password;
                                            this.email = email;
                                            isSeller = true;
                                            signup = false;
                                        } else {
                                            blockedList.add(newAccount.getUsername() + ", ");
                                            invisibleList.add(newAccount.getUsername() + ", ");
                                            customerList.add(newAccount);
                                            userName = username;
                                            this.password = password;
                                            this.email = email;
                                            isSeller = false;
                                            signup = false;
                                        }
                                    }
                                    writeAccounts();
                                    writeStore();
                                    writeBI();
                                } else {
                                    return -1;
                                }
                            }
                        } else {
                            return -1;
                        }
                    }
                } else {
                    return -1;
                }
            }
        } else {
            exit();
            return -1;
        }
        return choice;
    }

    public void exit() {
        JOptionPane.showMessageDialog(null, "Thanks for using the chat app!");
    }

    public void CustomerGUIWindow() {

        JFrame frame = new JFrame("@" + userName+" " + "Customer Messaging Application" );
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(185, 240, 255));
        frame.setVisible(true);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(185, 240, 255));


        JButton listSellers = new JButton("List Sellers");
        topPanel.add(listSellers);

        listSellers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ArrayList<String> choiceArray = new ArrayList<>();
                    for (int i = 0; i < sellerList.size(); i++) {
                        String sellerUsername = sellerList.get(i).getUsername();

                        // Check if the seller is in the invisibleByList
                        if (!invisibleByList.contains(sellerUsername)) {
                            choiceArray.add(sellerUsername);
                        }
                    }

                    String[] choices = new String[choiceArray.size()];
                    for (int j = 0; j < choiceArray.size(); j++) {
                        choices[j] = choiceArray.get(j);
                    }

                    recipient = (String) JOptionPane.showInputDialog(null, "Choose a user to message",
                            "List of sellers", JOptionPane.QUESTION_MESSAGE, null,
                            choices, choices[0]);

                    if (recipient != null) {
                        if (blockedMeList.contains(recipient)) {
                            JOptionPane.showMessageDialog(null, "You cannot proceed as this seller has blocked you.");
                        } else {
                            Object[] options = {"Message User", "Block User", "Become Invisible To User", "Close"};

                            int choice = JOptionPane.showOptionDialog(null, "Please select an option", "Interaction with: " + recipient, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[3]);

                            if (blockedList.contains(recipient)) {
                                JOptionPane.showMessageDialog(null, "You can access the seller's messages, but the seller will not be able to access yours.");
                            }

                            if (invisibleToList.contains(recipient)) {
                                JOptionPane.showMessageDialog(null, "You are invisible to this seller.");
                            }

                            if (choice == 0) {
                                messageRead();
                            } else if (choice == 1 || choice == 2) {
                                userInteraction(choice, recipient);
                            } else {
                                System.out.println("else");
                            }
                        }
                    }

                } catch (ArrayIndexOutOfBoundsException a) {
                    JOptionPane.showMessageDialog(null, "There are no Sellers yet!");
                }
            }
        });
        JButton listStores = new JButton("List Stores");
        topPanel.add(listStores);

        listStores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = "";
                try {
                    ArrayList<String> choiceArray = new ArrayList<>();
                    for (int i = 0; i < storeList.size(); i++) {
                        String userStores = storeList.get(i);
                        userStores = userStores.substring(userStores.indexOf(",") + 1, userStores.length());
                        while (userStores.contains(",")) {
                            choiceArray.add(userStores.substring(0, userStores.indexOf(",")));
                            userStores = userStores.substring(userStores.indexOf(",") + 1, userStores.length());
                        }
                    }

                    String[] choices = new String[choiceArray.size()];
                    for (int j = 0; j < choiceArray.size(); j++) {
                        choices[j] = choiceArray.get(j);
                    }

                    String store = (String) JOptionPane.showInputDialog(null, "Choose a store to message",
                            "List of stores", JOptionPane.QUESTION_MESSAGE, null,
                            choices, choices[0]);
                    if (store != null) {

                        try {
                            BufferedReader br = new BufferedReader(new FileReader("stores.txt"));
                            String line;
                            while ((line = br.readLine()) != null) {
                                if (line.contains(store)) {
                                    recipient = line.substring(0, line.indexOf(","));
                                }
                            }
                        } catch (Exception r) {

                        }

                        Object[] options = {"Message User", "Block User", "Become Invisible To User", "Close"};
                        int choice = JOptionPane.showOptionDialog(null, "Please select an option", "Seller chosen: " + user,
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[3]);
                        if (choice == 0) {
                            messageRead();
                        } else if (choice == 1 || choice == 2) {
                            userInteraction(choice, recipient);
                        } else {

                        }
                    }
                } catch(ArrayIndexOutOfBoundsException a){
                    JOptionPane.showMessageDialog(null, "There are no stores yet!");
                }
            }
        });

        JButton searchSeller = new JButton("Search Seller");
        topPanel.add(searchSeller);
        JTextField sellerField = new JTextField(18);
        topPanel.add(sellerField);

        searchSeller.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) throws ArrayIndexOutOfBoundsException {
                boolean success = false;
                try {
                    String search = sellerField.getText();
                    recipient = search;
                    for (int i = 0; i < sellerList.size(); i++) {
                        if ((sellerList.get(i).getUsername()).equals(search)) {
                            //new array of options
                            success = true;
                            Object[] options = {"Message User", "Block User", "Become Invisible To User", "Close"};
                            int choice = JOptionPane.showOptionDialog(null, "Please select an option", "Customer chosen: " + search,
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[3]);
                            userInteraction(choice, search);
                        }
                    }
                    if (success == false) {
                        JOptionPane.showMessageDialog(null, "Seller with this username was not found!");
                    }
                }catch (ArrayIndexOutOfBoundsException a) {
                    JOptionPane.showMessageDialog(null, "There are no sellers yet!");
                }
            }
        });

        content.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(185, 240, 255));

        //NEEDS TO DISPLAY USERNAME OF LOGGED IN USER
        JButton accountOptions = new JButton("Account options");
        centerPanel.add(accountOptions);


        accountOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    Object[] options = {"Edit username", "Edit Password", "Edit email", "Delete Account", "Close"};
                    int choice = JOptionPane.showOptionDialog(null, "Please select an option", "Account options",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[4]);
                    try {
                        writeAccounts();
                        edit(userName, password, email, isSeller, choice);
                    } catch (IOException x) {
                        JOptionPane.showMessageDialog(null, "There was an error editing your account!,", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (ArrayIndexOutOfBoundsException a) {
                    JOptionPane.showMessageDialog(null, "There are no sellers yet!");
                }
            }
        });
        JButton refresh = new JButton("Refresh");
        centerPanel.add(refresh);
        content.add(centerPanel, BorderLayout.CENTER);

        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Messages message = new Messages();
                messageList = message.getMessages();

                customerList.clear();
                sellerList.clear();
                storeList.clear();

                readAccounts();

                readStores();

                for (int x = 0; x < messageList.size(); x++) {
                    String[] line = messageList.get(x).split(",");
                    if ((line[0].trim().equals(userName)) || (line[1].trim().equals(userName))) {
                        if (line[0].trim().equals(userName)) {
                            if (line[4].equals("true")) {
                                blockedList.add(line[1]);
                            }
                            if (line[5].equals("true")) {
                                blockedMeList.add(line[1]);
                            }
                            if (line[6].equals("true")) {
                                invisibleToList.add(line[1]);
                            }
                            if (line[7].equals("true")) {
                                invisibleByList.add(line[1]);
                            }
                        } else {
                            if (line[4].equals("true")) {
                                blockedMeList.add(line[0]);
                            }
                            if (line[5].equals("true")) {
                                blockedList.add(line[0]);
                            }
                            if (line[6].equals("true")) {
                                invisibleByList.add(line[0]);
                            }
                            if (line[7].equals("true")) {
                                invisibleToList.add(line[0]);
                            }
                        }
                    }
                }

                JOptionPane.showMessageDialog(null, "Your messaging app is Refreshed!", "Refreshed", JOptionPane.INFORMATION_MESSAGE);

            }
        });

    }

    public void SellerGUIWindow() {
        JFrame frame = new JFrame("@" + userName + "Seller Messaging Application");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(185, 240, 255));
        frame.setVisible(true);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(185, 240, 255));
        JButton listCustomers = new JButton("List Customers");
        topPanel.add(listCustomers);

        listCustomers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //want like a dropdown list or scroll list
                try {
                    ArrayList<String> choiceArray = new ArrayList<>();
                    for (int i = 0; i < customerList.size(); i++) {
                        // If the customer is not in the invisibleByList
                        if (!invisibleByList.contains(customerList.get(i).getUsername())) {
                            choiceArray.add(customerList.get(i).getUsername());
                        }
                    }
                    String[] choices = new String[choiceArray.size()];
                    for (int j = 0; j < choiceArray.size(); j++) {
                        choices[j] = choiceArray.get(j);
                    }

                    recipient = (String) JOptionPane.showInputDialog(null, "Choose a user to message",
                            "List of customers", JOptionPane.QUESTION_MESSAGE, null,
                            choices, choices[0]);
                    if (recipient != null) {

                        // Check if the user is in blockedMeList
                        if (blockedMeList.contains(recipient)) {
                            JOptionPane.showMessageDialog(null, "This user has blocked you. You cannot proceed.");
                            return;
                        }

                        // Check if the user is in blockedList
                        if (blockedList.contains(recipient)) {
                            JOptionPane.showMessageDialog(null, "You can access this user's messages but they cannot access yours.");
                        }

                        // Check if the user is in invisibleToList
                        if (invisibleToList.contains(recipient)) {
                            JOptionPane.showMessageDialog(null, "This user cannot see you.");
                        }

                        Object[] options = {"Message User", "Block User", "Become Invisible To User", "Close"};
                        int choice = JOptionPane.showOptionDialog(null, "Please select an option", recipient, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[3]);
                        if (choice == 0) {
                            messageRead();
                        } else if (choice == 1 || choice == 2) {
                            userInteraction(choice, recipient);
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException a) {
                    JOptionPane.showMessageDialog(null, "There are no customers yet!");
                }
            }
        });


        JButton searchCustomer = new JButton("Search Customer");
        topPanel.add(searchCustomer);
        JTextField customerField = new JTextField(18);
        topPanel.add(customerField);

        searchCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean success = false;
                try {
                    String search = customerField.getText();
                    recipient = search;
                    for (int i = 0; i < customerList.size(); i++) {
                        if ((customerList.get(i).getUsername()).equals(search)) {

                            // Check if the user is in invisibleByList
                            if (invisibleByList.contains(search)) {
                                JOptionPane.showMessageDialog(null, "This user is invisible to you.");
                                return;
                            }

                            // Check if the user is in blockedMeList
                            if (blockedMeList.contains(search)) {
                                JOptionPane.showMessageDialog(null, "This user has blocked you. You cannot proceed.");
                                return;
                            }

                            // Check if the user is in blockedList
                            if (blockedList.contains(search)) {
                                JOptionPane.showMessageDialog(null, "You can access this user's messages but they cannot access yours.");
                            }

                            // Check if the user is in invisibleToList
                            if (invisibleToList.contains(search)) {
                                JOptionPane.showMessageDialog(null, "This user cannot see you.");
                            }
                            success = true;
                            Object[] options = {"Message User", "Block User", "Become Invisible To User", "Close"};
                            int choice = JOptionPane.showOptionDialog(null, "Please select an option", search,
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[3]);
                            userInteraction(choice, search);

                        }
                    }
                    if (success == false) {
                        JOptionPane.showMessageDialog(null, "Customer with this username was not found!");
                    }

                } catch (ArrayIndexOutOfBoundsException a) {
                    JOptionPane.showMessageDialog(null, "There are no customers yet!");
                }
            }
        });

        JButton storeOptions = new JButton("Store Options");
        topPanel.add(storeOptions);

        content.add(topPanel, BorderLayout.NORTH);

        storeOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = {"Add Store", "Delete Store", "Close"};
                int choice = JOptionPane.showOptionDialog(null, "Please select an option", "Store options",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[2]);

                if (choice == 0) {
                    String name = JOptionPane.showInputDialog(null, "Enter the name of your new store!", "Pick store name", JOptionPane.PLAIN_MESSAGE);
                    for (int i = 0; i < storeList.size(); i++) {
                        if ((storeList.get(i).contains(userName)) && (storeList.get(i).contains(name))) {
                            JOptionPane.showMessageDialog(null, "Store name is already associated with another store or account!");
                        } else {
                            if (storeList.get(i).contains(userName)) {
                                storeList.set(i, storeList.get(i) + name + ", ");
                                JOptionPane.showMessageDialog(null, "Store has been created!");


                            }

                        }
                    }
                } else if (choice == 1) {
                    boolean success = false;
                    //actually would be better to do a drop-down but this is easier
                    String name = JOptionPane.showInputDialog(null, "Enter the name of the store you'd like to delete!", "Delete store", JOptionPane.PLAIN_MESSAGE);
                    for (int i = 0; i < storeList.size(); i++) {
                        if ((storeList.get(i).contains(userName)) && (storeList.get(i).contains(name))) {
                            String newString = ((storeList.get(i)).replace( name + ", ", ""));
                            storeList.set(i, newString);
                            JOptionPane.showMessageDialog(null, "Store has been deleted!");
                            success = true;
                        }
                    }
                    if (success == false) {
                        JOptionPane.showMessageDialog(null, "This store name does not exist!");
                    }
                } else {

                }
                try {
                    writeStore();
                } catch (Exception s) {

                }
            }
        });

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(185, 240, 255));
        JButton refresh = new JButton("Refresh");
        centerPanel.add(refresh);

        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Messages message = new Messages();
                messageList = message.getMessages();

                customerList.clear();
                sellerList.clear();
                storeList.clear();

                readAccounts();

                readStores();

                for (int x = 0; x < messageList.size(); x++) {
                    String[] line = messageList.get(x).split(",");
                    if ((line[0].trim().equals(userName)) || (line[1].trim().equals(userName))) {
                        if (line[0].trim().equals(userName)) {
                            if (line[4].equals("true")) {
                                blockedList.add(line[1]);
                            }
                            if (line[5].equals("true")) {
                                blockedMeList.add(line[1]);
                            }
                            if (line[6].equals("true")) {
                                invisibleToList.add(line[1]);
                            }
                            if (line[7].equals("true")) {
                                invisibleByList.add(line[1]);
                            }
                        } else {
                            if (line[4].equals("true")) {
                                blockedMeList.add(line[0]);
                            }
                            if (line[5].equals("true")) {
                                blockedList.add(line[0]);
                            }
                            if (line[6].equals("true")) {
                                invisibleByList.add(line[0]);
                            }
                            if (line[7].equals("true")) {
                                invisibleToList.add(line[0]);
                            }
                        }
                    }
                }

                JOptionPane.showMessageDialog(null, "Your messaging app is Refreshed!", "Refreshed", JOptionPane.INFORMATION_MESSAGE);



            }
        });

        JButton accountOptions = new JButton("Account options");
        centerPanel.add(accountOptions);
        content.add(centerPanel, BorderLayout.CENTER);

        accountOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Object[] options = {"Edit username", "Edit Password", "Edit email", "Delete Account", "Close"};
                int choice = JOptionPane.showOptionDialog(null, "Please select an option", "Account options",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[4]);
                try {
                    edit(userName, password, email, isSeller, choice);
                    writeAccounts();
                } catch (IOException x) {
                    JOptionPane.showMessageDialog(null, "There was an error editing your account!,", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    public void writeAccounts() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("sellers.txt"));
        for (int i = 0; i < sellerList.size(); i++) {
            bw.write(accountString(sellerList.get(i)) + "\n");
        }
        bw.close();
        BufferedWriter bw2 = new BufferedWriter(new FileWriter("customers.txt"));
        for (int i = 0; i < customerList.size(); i++) {
            bw2.write(accountString(customerList.get(i)) + "\n");
        }
        bw2.close();
    }
    public void readStores() {

        try {
            BufferedReader br = new BufferedReader(new FileReader("stores.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                storeList.add(line);
            }
            br.close();
        } catch (Exception e) {

        }

    }
    public void writeStore() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("stores.txt"));
        for (int i = 0; i < storeList.size(); i++) {
            bw.write(storeList.get(i) + "\n");
        }
        bw.close();
    }
    public void readAccounts() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("customers.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String username = line.substring(0, line.indexOf(","));
                line = line.substring(line.indexOf(",") + 2, line.length());
                String password = line.substring(0, line.indexOf(","));
                line = line.substring(line.indexOf(",") + 2, line.length());
                String email = line;
                Account newAccount = new Account(email, username, password);
                customerList.add(newAccount);
            }
            br.close();
        } catch (Exception e) {

        }
        try {
            BufferedReader br = new BufferedReader(new FileReader("sellers.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String username = line.substring(0, line.indexOf(","));
                line = line.substring(line.indexOf(",") + 2, line.length());
                String password = line.substring(0, line.indexOf(","));
                line = line.substring(line.indexOf(",") + 2, line.length());
                String email = line;
                Account newAccount = new Account(email, username, password);
                sellerList.add(newAccount);
            }
            br.close();
        } catch (Exception e) {

        }
    }
    public boolean existingEmail(String email, int choice) {
        BufferedReader br = null;
        if ((email != null) && !(email.isEmpty())) {
            if (choice == 0) {
                try {
                    br = new BufferedReader(new FileReader("sellers.txt"));

                    String line;
                    while ((line = br.readLine()) != null) {
                        line = line.substring(line.indexOf(",") + 1, line.lastIndexOf(","));
                        line = line.substring(line.indexOf("," + 1, line.length()));
                        if (line.equals(email)) {
                            return true;
                        }
                    }
                    br.close();
                } catch (Exception e) {
                    return false;
                }
            } else {
                try {
                    br = new BufferedReader(new FileReader("customers.txt"));

                    String line;
                    while ((line = br.readLine()) != null) {
                        line = line.substring(line.indexOf(",") + 1, line.lastIndexOf(","));
                        line = line.substring(line.indexOf("," + 1, line.length()));
                        if (line.equals(email)) {
                            return true;
                        }
                    }
                    br.close();
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return false;
    }
    public boolean existingUsername(String username, int choice) {
        if ((username != null) && !(username.isEmpty())) {
            if (choice == 0) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader("sellers.txt"));
                    String line;
                    while ((line = br.readLine()) != null) {
                        line = line.substring(0, line.indexOf(","));
                        line = line.trim();
                        if (line.equals(username)) {
                            this.userName = username;
                            return true;
                        }
                    }
                    br.close();
                } catch (Exception e) {
                    return false;
                }
            } else {
                try {
                    BufferedReader br = new BufferedReader(new FileReader("customers.txt"));
                    String line;
                    while ((line = br.readLine()) != null) {
                        line = line.substring(0, line.indexOf(","));
                        line = line.trim();
                        if (line.equals(username)) {
                            this.userName = username;
                            return true;
                        }
                    }
                    br.close();
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return false;
    }
    public boolean checkPassword(String username, String password, int choice) {
        if (choice == 0) {
            try {
                BufferedReader br = new BufferedReader(new FileReader("sellers.txt"));
                String line;
                while ((line = br.readLine()) != null) {
                    String user = line.substring(0, line.indexOf(","));
                    if (username.equals((user.trim()))) {
                        line = line.substring(line.indexOf(",") + 1, line.length());
                        line = line.trim();



                        if (password.equals(line.substring(0, line.indexOf(",")))) {
                            this.password = password;
                            return true;
                        }
                    }

                }
                br.close();
            } catch (Exception e) {
                return false;
            }
        } else {
            try {
                BufferedReader br = new BufferedReader(new FileReader("customers.txt"));
                String line;
                while ((line = br.readLine()) != null) {
                    /////////////////////////////////////////////////
                    String user = line.substring(0, line.indexOf(","));
                    /////////////////////////////////////////////////
                    if (username.equals((user.trim()))) {
                        line = line.substring(line.indexOf(",") + 1, line.length());
                        line = line.trim();
                        if (password.equals(line.substring(0, line.indexOf(",")))) {
                            this.password = password;
                            return true;
                        }
                    }
                }
                br.close();
            } catch(Exception e){
                return false;
            }


        }
        return false;
    }
    public boolean edit(String username, String password, String email, boolean isSeller, int choice) throws IOException {
        boolean success = false;
        boolean edit = true;
        int num;
        if (choice == 0) {
            while (edit) {
                String newUsername = JOptionPane.showInputDialog("Please enter a new username");
                if (newUsername != null) {
                    if (isSeller) {
                        num = 0;
                    } else {
                        num = 1;
                    }
                    if ((existingUsername(newUsername, num)) != true) {
                        if (isSeller) {
                            for (int i = 0; i < sellerList.size(); i++) {
                                if ((sellerList.get(i).getUsername()).equals(username)) {
                                    (sellerList.get(i)).setUsername(newUsername);
                                }
                                if ((sellerList.get(i).getUsername()).equals(newUsername)) {
                                    JOptionPane.showMessageDialog(null, "Account successfully edited!");
                                    success = true;
                                    edit = false;
                                }
                            }
                        } else {
                            for (int i = 0; i < customerList.size(); i++) {
                                if ((customerList.get(i).getUsername()).equals(username)) {
                                    customerList.get(i).setUsername(newUsername);
                                }
                                if ((customerList.get(i).getUsername()).equals(newUsername)) {
                                    JOptionPane.showMessageDialog(null, "Account successfully edited!");
                                    success = true;
                                    edit = false;
                                }
                            }

                        }


                    } else {
                        JOptionPane.showMessageDialog(null, "This username already exists!", "Please try again!", JOptionPane.PLAIN_MESSAGE);
                    }
                } else {
                    break;
                }
            }


        } else if (choice == 1) {
            String newPassword = null;
            newPassword = JOptionPane.showInputDialog("Please enter a new password");
            if (newPassword != null) {
                if (isSeller) {
                    for (int i = 0; i < sellerList.size(); i++) {
                        if (((sellerList.get(i).getPassword()).equals(password)) && ((sellerList.get(i).getUsername()).equals(userName))) {
                            sellerList.get(i).setPassword(newPassword);
                        }
                        if ((sellerList.get(i).getPassword()).equals(newPassword)) {
                            JOptionPane.showMessageDialog(null, "Account successfully edited!");
                            success = true;
                        }
                    }
                } else {
                    for (int i = 0; i < customerList.size(); i++) {

                        if (((customerList.get(i).getPassword()).equals(password)) && ((customerList.get(i).getUsername()).equals(userName))) {
                            customerList.get(i).setPassword(newPassword);
                        }
                        if ((customerList.get(i).getPassword()).equals(newPassword)) {
                            JOptionPane.showMessageDialog(null, "Account successfully edited!");
                            success = true;
                        }
                    }
                }
            } else {

            }

        } else if (choice == 2) {
            while (edit) {
                String newEmail = JOptionPane.showInputDialog("Please enter a new email");
                if (newEmail != null) {
                    if (isSeller) {
                        num = 0;
                    } else {
                        num = 1;
                    }
                    if ((existingEmail(newEmail, num)) != true) {
                        if (isSeller) {
                            for (int i = 0; i < sellerList.size(); i++) {
                                if ((sellerList.get(i).getEmail()).equals(email)) {
                                    sellerList.get(i).setEmail(newEmail);
                                }
                                if ((sellerList.get(i).getEmail()).equals(newEmail)) {
                                    JOptionPane.showMessageDialog(null, "Account successfully edited!");
                                    edit = false;
                                    success = true;
                                }
                            }
                        } else {
                            for (int i = 0; i < customerList.size(); i++) {
                                if ((customerList.get(i).getEmail()).equals(email)) {
                                    customerList.get(i).setEmail(newEmail);
                                }
                                if ((customerList.get(i).getEmail()).equals(newEmail)) {
                                    JOptionPane.showMessageDialog(null, "Account successfully edited!");
                                    edit = false;
                                    success = true;
                                }
                            }
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "This email is already associated with an account!", "Please try again!", JOptionPane.PLAIN_MESSAGE);
                    }
                } else {
                    break;
                }
            }
        } else if (choice == 3) {
            //delete account
            Object[] options = {"YES", "NO"};
            int option = JOptionPane.showOptionDialog(null, "Are you sure you want to delete this account?", "Delete Account", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (option == 0) {
                if (isSeller) {

                    for (int i = 0; i < sellerList.size(); i++) {
                        if (((sellerList.get(i).getUsername()).equals(userName)) && (sellerList.get(i).getPassword()).equals(this.password)) {

                            sellerList.remove(i);
                            storeList.remove(i);
                        }
                        if (!(sellerList.get(i).getUsername().equals(userName))) {
                            JOptionPane.showMessageDialog(null, "Account successfully edited!");
                            success = true;
                            exit();
                            if (sellerList.size() == 0) {
                                BufferedWriter bw = new BufferedWriter(new FileWriter("customers.txt"));
                                bw.write("");
                                bw.close();
                                break;
                            }
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < customerList.size(); i++) {
                        //close window or remove buttons so user has to exit
                        if ((customerList.get(i).getUsername().equals(userName)) && (customerList.get(i).getPassword()).equals(this.password)) {
                            customerList.remove(i);
                        }
                        if ((!((customerList.get(i).getUsername()).equals(userName)))) {
                            JOptionPane.showMessageDialog(null, "Account successfully edited!");
                            success = true;
                            exit();
                            if (customerList.size() == 0) {
                                BufferedWriter bw = new BufferedWriter(new FileWriter("customers.txt"));
                                bw.write("");
                                bw.close();
                                break;
                            }
                            break;
                        }
                    }
                }

            } else {

            }
        } else {

        }
        if (success == true) {
            writeAccounts();
            return true;
        }

        return false;
    }

    public boolean userInteraction(int choice, String user) {
        LocalTime currentTime;
        int hours;
        int minutes;
        String time;
        currentTime = LocalTime.now();
        hours = currentTime.getHour();
        minutes = currentTime.getMinute();
        time = hours + ":" + minutes;
        Messages message = new Messages();
        messageList = message.getMessages();
        int sizes = (messageList != null) ? messageList.size() : 0;
        ArrayList<String> userRec = new ArrayList<>();
        for(int i = 0; i < messageList.size(); i++) {
            String[] messageToEdit = messageList.get(i).split(",");
            if (messageToEdit[0].equals(userName) && messageToEdit[1].equals(recipient)) {
                userRec.add(messageList.get(i));
            }
            if (messageToEdit[1].equals(userName) && messageToEdit[0].equals(recipient)) {
                userRec.add(messageList.get(i));
            }
        }
        if (userRec.size() == 0) {
            messageList.add(userName + "," + recipient + "," + time + "," + "START OF CONVO" +
                    "," + "false," + "false," + "false," + "false");
            message.writeMessages(messageList);
        }
        if (choice == 0) {
            Object[] options = {"Write messages", "Edit Messages", "Delete Messages", "Exit"};
            int option = JOptionPane.showOptionDialog(null, "Pick an option", "Messaging options", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null, options, options[3]);
            if ((option >= 0 || option <= 5)) {
                if (option == 0) {
                    while (true) {
                        // Write message in the format of
                        // (Sender, Receiver, TimeStamp, Message, false, false, false, false)
                        String newMessage = JOptionPane.showInputDialog(null, "Enter message:    (Enter 'return' to return to options)");
                        if (newMessage.equals("return")) {
                            break;
                        } else if (newMessage == null) {

                        } else {
                            JOptionPane.showMessageDialog(null, "Message Sent!");
                        }
                        currentTime = LocalTime.now();
                        hours = currentTime.getHour();
                        minutes = currentTime.getMinute();
                        time = hours + ":" + minutes;
                        messageList.add(userName + "," + recipient + "," + time + "," + newMessage +
                                ",false,false,false,false");
                        message.writeMessages(messageList);

                    }
                } else if (option == 1) {
                    // enter the specific message you want to edit, and the new message
                    // Have to go through a for loop, until the message is found && the sender is the user
                    // otherwise, print out "choose a valid message to edit"
                    int size = (messageList != null) ? messageList.size() : 0;
                    for (int i = 0; i < size; i++) {
                        String edit = JOptionPane.showInputDialog(null, "Enter the message you want to edit: ");
                        if (edit != null) {
                            String[] messageToEdit = messageList.get(i).split(",");
                            if (messageToEdit[3].equals(edit)
                                    && messageToEdit[0].equals(userName) && messageToEdit[1] == recipient) {
                                String newMessage = JOptionPane.showInputDialog(null, "Enter the new message: ");
                                String newLine = messageToEdit[0] + "," + messageToEdit[1] + "," +
                                        messageToEdit[2] + "," + newMessage + "," + messageToEdit[4] + "," +
                                        messageToEdit[5] + "," + messageToEdit[6] + "," + messageToEdit[7];

                                messageList.set(i, newLine);
                                message.writeMessages(messageList);
                                JOptionPane.showMessageDialog(null, "Message was edited!");
                            } else {
                                JOptionPane.showMessageDialog(null, "Message does not exist!");
                            }
                        } else {

                        }
                    }
                    //after the for loop, update the Message.txt file with the new messageList
                } else if (option == 2) {
                    // enter the specific message you want to delete
                    // Have to go through a for loop, until the message is found && the sender is the user
                    // otherwise, print out "choose a valid message to delete"
                    String delete = JOptionPane.showInputDialog("Enter the message you want to delete: ");
                    int size = (messageList != null) ? messageList.size() : 0;
                    for (int i = 0; i < size; i++) {
                        String[] messageToEdit = messageList.get(i).split(",");
                        if (messageToEdit[3].equals(delete)
                                && messageToEdit[0].equals(userName) && messageToEdit[1].equals(recipient)) {
                            messageList.remove(i);
                            message.writeMessages(messageList);
                        }
                    }
                }  else {

                }

            } else {

            }
        } else if (choice == 1) {
            Object[] options = {"YES", "NO"};
            int option =  JOptionPane.showOptionDialog(null, "Are you sure you want to block "
                            + user + "?", "Block?", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (option == 0) {
                // Have to go through a for loop through messageList, and turn the
                // (4th index to true (Sender blocks receiver) if the user is the sender)
                int size = (messageList != null) ? messageList.size() : 0;
                for (int i = 0; i < size; i++) {
                    String[] messageToEdit = messageList.get(i).split(",");
                    if (messageToEdit[0].equals(userName) && messageToEdit[1].equals(recipient)) {
                        String newLine = messageToEdit[0] + "," + messageToEdit[1] + "," +
                                messageToEdit[2] + "," + messageToEdit[3] + "," + "true" + "," +
                                messageToEdit[5] + "," + messageToEdit[6] + "," + messageToEdit[7];
                        messageList.set(i, newLine);
                        message.writeMessages(messageList);
                    }

                    if (messageToEdit[1].equals(userName) && messageToEdit[0].equals(recipient)) {
                        String newLine = messageToEdit[0] + "," + messageToEdit[1] + "," +
                                messageToEdit[2] + "," + messageToEdit[3] + "," + messageToEdit[4] + "," +
                                "true" + "," + messageToEdit[6] + "," + messageToEdit[7];
                        messageList.set(i, newLine);
                        message.writeMessages(messageList);
                    }
                }
                JOptionPane.showMessageDialog(null, "You have blocked " + recipient + ". THEY will no longer be able to send " +
                        "or view your messages. However, YOU can still interact with the messages." );

                for (int i = 0; i < blockedList.size(); i++) {
                    String line = blockedList.get(i);
                    if(((line.substring(0, line.indexOf(",") )).equals(userName))) {
                        blockedList.set(i, line + " " + user + ",");
                    }

                }
                writeBI();
            } else {

            }
            //block
        } else if (choice == 2) {
            //invis
            Object[] options = {"YES", "NO"};
            int option =  JOptionPane.showOptionDialog(null, "Are you sure you want to be invisible to " + user + "?", "Invisible?", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            int size = (messageList != null) ? messageList.size() : 0;

            for (int i = 0; i < size; i++) {
                String[] messageToEdit = messageList.get(i).split(",");
                if (messageToEdit[0].trim().equals(userName) && messageToEdit[1].trim().equals(recipient)) {
                    String newLine = messageToEdit[0] + "," + messageToEdit[1] + "," +
                            messageToEdit[2] + "," + messageToEdit[3] + "," + messageToEdit[4] + "," +
                            messageToEdit[5] + "," + "true" + "," + messageToEdit[7];
                    messageList.set(i, newLine);
                    message.writeMessages(messageList);
                }

                if (messageToEdit[1].equals(userName) && messageToEdit[0].equals(recipient)) {
                    String newLine = messageToEdit[0] + "," + messageToEdit[1] + "," +
                            messageToEdit[2] + "," + messageToEdit[3] + "," + messageToEdit[4] + "," +
                            messageToEdit[5] + "," + messageToEdit[6] + "," + "true" + "," +
                            messageToEdit[7];
                    messageList.set(i, newLine);
                    message.writeMessages(messageList);
                }

            }
            JOptionPane.showMessageDialog(null, "Now invisible to " + recipient + "." + " They will no longer be able to see " +
                    "your store or your account.");

            for (int i = 0; i < invisibleList.size(); i++) {
                String line = invisibleList.get(i);
                if(((line.substring(0, line.indexOf(",") )).equals(userName))) {
                    invisibleList.set(i, line + " " + user + ",");
                }
                writeBI();
            }
        } else {
            //blank for close
        }
        return false;
    }
    public void readBI() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("blocked.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                blockedList.add(line);
            }
            br.close();
        } catch (Exception e) {

        }
        try {
            BufferedReader br = new BufferedReader(new FileReader("invisible.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                invisibleList.add(line);
            }
            br.close();
        } catch (Exception e) {

        }
    }
    public void writeBI() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("blocked.txt"));
            for (int i = 0; i < blockedList.size(); i++) {
                bw.write(blockedList.get(i) + "\n");
            }
            bw.close();
        } catch (Exception e) {

        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("invisible.txt"));
            for (int i = 0; i < invisibleList.size(); i++) {
                bw.write(invisibleList.get(i));
            }
            bw.close();
        } catch (Exception e) {

        }

    }

    public String accountString(Account account) {
        return account.getUsername() + ", " + account.getPassword() + ", " + account.getEmail();// + ", " + account.isSeller();
    }

        public void messageRead() {
        Messages message = new Messages();
        messageList = message.getMessages();
        if (messageList == null) {
            JOptionPane.showMessageDialog(null, "null");
        }
        String storeName = null;
        String sell = null;
        String cust = null;
        boolean invalid = false;
        if (isSeller) {
            int size = (messageList != null) ? messageList.size() : 0;
            for (int x = 0; x < size; x++) {
                String[] line = messageList.get(x).split(",");

                if ((line[0].trim().equals(userName)) || (line[1].trim().equals(userName))) {
                    processLine(line);
                }
            }
        }

        while (true) {
            int size = (messageList != null) ? messageList.size() : 0;
            LocalTime currentTime = LocalTime.now();
            int hours = currentTime.getHour();
            int minutes = currentTime.getMinute();
            String time = hours + ":" + minutes;
            if (size == 0) {
                messageList.add(userName + "," + cust + "," + time + "," + "START OF CONVO" + ",false,false,false,false");
                message.writeMessages(messageList);
            }
            displayMessageHistory(size);

            Object[] options = {"Write messages", "Edit", "Delete"};
            int n = JOptionPane.showOptionDialog(null, "Choose an option", "Message Options",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

            switch (n) {
                case 0:
                    writeMessages();
                    break;
                case 1:
                    editMessage();
                    break;
                case 2:
                    deleteMessage();
                    break;
                default:
                    break;
            }
        }
    }

    private void processLine(String[] line) {
        if (line[0].trim().equals(userName)) {
            if (line[4].equals("true")) {
                blockedList.add(line[1]);
            }
            if (line[5].equals("true")) {
                blockedMeList.add(line[1]);
            }

            if (line[6].equals("true")) {
                invisibleToList.add(line[1]);
            }

            if (line[7].equals("true")) {
                invisibleByList.add(line[1]);
            }

        } else {
            if (line[4].equals("true")) {
                blockedMeList.add(line[0]);
            }
            if (line[5].equals("true")) {
                blockedList.add(line[0]);
            }
            if (line[6].equals("true")) {
                invisibleByList.add(line[0]);
            }
            if (line[7].equals("true")) {
                invisibleToList.add(line[0]);
            }
        }
    }

    private void displayMessageHistory(int size) {
        StringBuilder messageHistory = new StringBuilder("Message history:\n");
        for (int x = 0; x < size; x++) {
            if (messageList.get(x).contains(recipient) && messageList.get(x).contains(userName)) {
                String[] line = messageList.get(x).split(",");
                String send = line[0].trim();
                String receive = line[1].trim();
                String showTime = line[2].trim();
                String mess = line[3].trim();
                if (!mess.equals("START OF CONVO")) {
                    messageHistory.append("Time: ").append(showTime).append("    ").append(send).append(": ").append(mess).append("\n");
                }
            }
        }
        JOptionPane.showMessageDialog(null, messageHistory.toString());
    }

    private void writeMessages() {
        while (true) {
            Messages message = new Messages();
            messageList = message.getMessages();
            String newMessage = JOptionPane.showInputDialog("Enter message: (Enter 'return' to return to message options");
            if (newMessage.equals("return")) {
                break;
            }
            LocalTime currentTime = LocalTime.now();
            int hours = currentTime.getHour();
            int minutes = currentTime.getMinute();
            String time = hours + ":" + minutes;
            messageList.add(userName + "," + recipient + "," + time + "," + newMessage + ",false,false,false,false");
            message.writeMessages(messageList);
        }
    }

    private void editMessage() {
        Messages message = new Messages();
        messageList = message.getMessages();
        String edit = JOptionPane.showInputDialog("Enter the message you want to edit: ");
        int size = (messageList != null) ? messageList.size() : 0;
        boolean isEdited = false;
        for (int i = 0; i < size; i++) {
            String[] messageToEdit = messageList.get(i).split(",");
            if (messageToEdit[3].equals(edit) && messageToEdit[0].equals(userName) && messageToEdit[1].equals(recipient)) {
                String newMessage = JOptionPane.showInputDialog("Enter the new message: ");
                String newLine = messageToEdit[0] + "," + messageToEdit[1] + "," + messageToEdit[2] + "," + newMessage + "," + messageToEdit[4] + "," + messageToEdit[5] + "," + messageToEdit[6] + "," + messageToEdit[7];
                messageList.set(i, newLine);
                message.writeMessages(messageList);
                isEdited = true;
                break;
            }
        }
        if(!isEdited) {
            JOptionPane.showMessageDialog(null, "Error: Choose a valid message to edit");
        }
    }

    private void deleteMessage() {
        Messages message = new Messages();
        messageList = message.getMessages();
        String delete = JOptionPane.showInputDialog("Enter the message you want to delete: ");
        int size = (messageList != null) ? messageList.size() : 0;
        boolean isDeleted = false;
        for (int i = 0; i < size; i++) {
            String[] messageToEdit = messageList.get(i).split(",");
            if (messageToEdit[3].equals(delete) && messageToEdit[0].equals(userName) && messageToEdit[1].equals(recipient)) {
                messageList.remove(i);
                message.writeMessages(messageList);
                isDeleted = true;
                break;
            }
        }
        if(!isDeleted) {
            JOptionPane.showMessageDialog(null, "Error: Choose a valid message to delete");
        }
    }



    public void displayMessages() {

    }
}







