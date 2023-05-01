import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class GUI extends JComponent implements Runnable {
    ArrayList<Account> customerList = new ArrayList<>();
    ArrayList<Account> sellerList = new ArrayList<>();

    private static ArrayList<String> messageList = new ArrayList<String>();
    private static ArrayList<String> storeList = new ArrayList<String>();
    private static ArrayList<String> invisibleList = new ArrayList<String>();

    private static ArrayList<String> blockedList = new ArrayList<String>();


    private static String userName;
    private static String password;
    private static String email;
    private static boolean isSeller;


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
            while(loggingIn) {
                String username = JOptionPane.showInputDialog(null, "Please enter your username");
                if ((existingUsername(username, choice)) != true) {
                    JOptionPane.showMessageDialog(null, "An account with this user does not exist!", "Login unsuccessful", JOptionPane.ERROR_MESSAGE);
                } else {
                    String password = JOptionPane.showInputDialog(null, "Please enter your password");
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
                }

            }

        } else {
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
                if (existingEmail(email, choice) == true) {
                    JOptionPane.showMessageDialog(null, "Account with this email already exists!", "Signup unsuccessful", JOptionPane.PLAIN_MESSAGE);
                } else {
                    String username = JOptionPane.showInputDialog(null, "Please enter your username");
                    if (existingUsername(username, choice) == true) {
                        JOptionPane.showMessageDialog(null, "Username is taken!", "Signup unsuccessful", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        String password = JOptionPane.showInputDialog(null, "Please enter your password");
                        if (choice == 0) {
                            newAccount = new Account(email, username, password, true);
                        } else {
                            newAccount = new Account(email, username, password, false);
                        }
                        if ((newAccount.getEmail() != null) && (newAccount.getPassword() != null) && (newAccount.getUsername() != null)) {
                            if (choice == 0) {
                                blockedList.add(newAccount.getUsername());
                                invisibleList.add(newAccount.getUsername());
                                sellerList.add(newAccount);
                                storeList.add(newAccount.getUsername() + ", ");
                                userName = username;
                                this.password = password;
                                this.email = email;
                                isSeller = true;
                                signup = false;
                            } else {
                                blockedList.add(newAccount.getUsername());
                                invisibleList.add(newAccount.getUsername());
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
                    }
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
        JFrame frame = new JFrame("Messaging Application");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(185, 240, 255));
        frame.setVisible(true);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(185, 240, 255));

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
                            choiceArray.add(userStores.substring(0,userStores.indexOf(",")));
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

                    try {
                        BufferedReader br = new BufferedReader(new FileReader("stores.txt"));
                        String line;
                        while ((line = br.readLine()) != null) {
                            if (line.contains(store)) {
                                user =  line.substring(0, line.indexOf(","));
                            }
                        }
                    } catch (Exception r) {

                    }

                    Object[] options = {"Message User", "Block User", "Become Invisible To User", "Close"};
                    int choice = JOptionPane.showOptionDialog(null, "Please select an option", user,
                            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[3]);
                    userInteraction(choice, user);
                } catch (ArrayIndexOutOfBoundsException a) {
                    JOptionPane.showMessageDialog(null, "There are no sellers yet!");
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
                    for (int i = 0; i < sellerList.size(); i++) {
                        if ((sellerList.get(i).getUsername()).equals(search)) {
                            //new array of options
                            success = true;
                            Object[] options = {"Message User", "Block User", "Become Invisible To User", "Close"};
                            int choice = JOptionPane.showOptionDialog(null, "Please select an option", search,
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

            }
        });

    }

    public void SellerGUIWindow() {
        JFrame frame = new JFrame("Messaging Application");
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
                        choiceArray.add(customerList.get(i).getUsername());
                    }
                    String[] choices = new String[choiceArray.size()];
                    for (int j = 0; j < choiceArray.size(); j++) {
                        choices[j] = choiceArray.get(j);
                    }

                    String user = (String) JOptionPane.showInputDialog(null, "Choose a user to message",
                            "List of customers", JOptionPane.QUESTION_MESSAGE, null,
                            choices, choices[0]);

                    Object[] options = {"Message User", "Block User", "Become Invisible To User", "Close"};
                    int choice = JOptionPane.showOptionDialog(null, "Please select an option", user,
                            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[3]);
                    userInteraction(choice, user);
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
                    for (int i = 0; i < customerList.size(); i++) {
                        if ((customerList.get(i).getUsername()).equals(search)) {
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
                String seller = line.substring(0, line.indexOf(","));
                boolean isSeller = Boolean.parseBoolean(seller);
                line = line.substring(line.indexOf(",") + 2, line.length());
                String email = line;
                Account newAccount = new Account(username, password, email, isSeller);
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
                String seller = line.substring(0, line.indexOf(","));
                boolean isSeller = Boolean.parseBoolean(seller);
                line = line.substring(line.indexOf(",") + 2, line.length());
                String email = line;
                Account newAccount = new Account(username, password, email, isSeller);
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
                    System.out.println("seller confirmed");
                    for (int i = 0; i < sellerList.size(); i++) {
                        if (((sellerList.get(i).getUsername()).equals(userName)) && (sellerList.get(i).getPassword()).equals(this.password)) {
                            System.out.println("account found");
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
        if (choice == 0) {
            //msg.. open new window and will have to check if two users already have a shared file
        } else if (choice == 1) {
            Object[] options = {"YES", "NO"};
            int option =  JOptionPane.showOptionDialog(null, "Are you sure you want to block "
                            + user + "?", "Block?", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (option == 0) {
                for (int i = 0; i < blockedList.size(); i++) {
                    String line = blockedList.get(i);
                    if(((line.substring(0, line.indexOf(",") )).equals(userName))) {
                        blockedList.set(i, line + ", " + user);
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
            for (int i = 0; i < invisibleList.size(); i++) {
                String line = invisibleList.get(i);
                if(((line.substring(0, line.indexOf(",") )).equals(userName))) {
                    invisibleList.set(i, line + ", " + user);
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
        return account.getUsername() + ", " + account.getPassword() + ", " + account.getEmail() + ", " + account.isSeller();
    }
}


