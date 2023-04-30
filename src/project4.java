import jdk.jshell.spi.ExecutionControl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Scanner;
import java.io.*;
import java.util.SimpleTimeZone;
import java.util.TreeMap;
import java.util.ArrayList;


public class project4 {
    static Scanner scan = new Scanner(System.in);
    private static ArrayList<String> messageList = new ArrayList<String>();
    private static ArrayList<String> storeList = new ArrayList<String>();
    private static ArrayList<String> sellerList = new ArrayList<String>();
    private static ArrayList<String> customerList = new ArrayList<String>();
    private static ArrayList<String> invisibleToList = new ArrayList<String>();

    private static ArrayList<String> invisibleByList = new ArrayList<String>();
    private static ArrayList<String> blockedList = new ArrayList<String>();

    private static ArrayList<String> blockedMeList = new ArrayList<String>();


    public static User user;
    private static boolean isSeller;


    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to the chat app!");
        System.out.println("1. Login");
        System.out.println("2. Sign up");
        System.out.println("3. Exit");
        String choice = scan.nextLine();

        switch (choice) {
            case "1":
                System.out.println("1. Seller Login");
                System.out.println("2. Customer Login");
                System.out.println("3. Exit");

                do {
                    choice = scan.nextLine();
                } while (!choice.equals("3") && !choice.equals("2") && !choice.equals("1"));
                if (choice.equals("3")) {
                    System.out.println("Thanks for using the chat app!");
                    return;
                }

                login(choice);
                break;

            case "2":
                System.out.println("1. Seller Signup");
                System.out.println("2. Customer Signup");
                System.out.println("3. Exit");
                do {
                    choice = scan.nextLine();
                } while (!choice.equals("3") && !choice.equals("2") && !choice.equals("1"));
                if (choice.equals("3")) {
                    System.out.println("Thanks for using the chat app!");
                    return;
                }

                signup(choice);
                break;

            case "3":
                System.out.println("Thanks for using the chat app!");
                return;

            default:
                System.out.println("Invalid choice");
                main(args);
                break;
        }

        Messages message = new Messages();
        messageList = message.getMessages();

        Customer customer = new Customer();
        customerList = customer.getCustomers();

        Seller seller = new Seller();
        sellerList = seller.getSeller();

        Stores store = new Stores();
        storeList = store.getStores();


        String storeName = null;
        String sell = null;
        String cust = null;
        boolean invalid = false;
        if (isSeller) {
            int size = (messageList != null) ? messageList.size() : 0;
            for (int x = 0; x < size; x++) {

            /*
            0. Sender
            1. Receiver
            2. TimeStamp
            3. Message
            4. Sender blocks receiver
            5. Receiver blocks sender
            6. Sender invisible to receiver
            7. Receiver invisible to sender
             */
                String[] line = messageList.get(x).split(",");

                if ((line[0].trim().equals(user.getName())) || (line[1].trim().equals(user.getName()))) {
                    if (line[0].trim().equals(user.getName())) {
                        if (line[4].equals("true")) {
                            blockedList.add(line[1]);
                            //Added the receiver to blocked list to indicate that I blocked the receiver
                        }
                        if (line[5].equals("true")) {
                            blockedMeList.add(line[1]);
                            //Added the receiver to blocked me list to indicate that I am blocked by the receiver
                        }

                        if (line[6].equals("true")) {
                            invisibleToList.add(line[1]);
                            // Added the receiver to invisible list to indicate that I am invisible to the receiver
                        }

                        if (line[7].equals("true")) {
                            invisibleByList.add(line[1]);
                            // Added the receiver to invisible list to indicate that the receiver is invisible to me
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


            do {
                System.out.println("Choose an option");
                System.out.println("1. List customers to contact");
                System.out.println("2. Search customer");
                System.out.println("3. Add store");
                System.out.println("4. Edit account");
                System.out.println("5. Exit");
                choice = scan.nextLine();
                if (choice.equals("1")) {
                    size = (customerList != null) ? customerList.size() : 0;
                    if (size == 0 || invisibleByList.size() == size) {
                        System.out.println("No customers found");
                        invalid = true;

                    } else {
                        while (true) {
                            invalid = false;
                            System.out.println("Enter customer number: ");


                            for (int i = 0; i < size; i++) {
                                if (!invisibleByList.contains(customerList.get(i))) {
                                    System.out.println((i) + ": " + customerList.get(i));
                                }
                            }

                            choice = scan.nextLine();
                            int index = Integer.parseInt(choice);
                            cust = customerList.get(index);
                            if (blockedMeList.contains(cust)) {
                                System.out.println("This customer blocked you. " +
                                        "You cannot send messages to this customer, choose another customer");
                            } else {
                                break;
                            }
                        }
                    }

                } else if (choice.equals("2")) {
                    if (size == 0) {
                        System.out.println("No customers found");
                        invalid = true;
                    } else {
                        while (true) {
                            System.out.println("Enter customer name: ");
                            cust = scan.nextLine();
                            if ((customerList.contains(cust)) && (!invisibleByList.contains(cust))) {
                                System.out.println("Customer found");

                                if (blockedMeList.contains(cust)) {
                                    System.out.println("This customer blocked you. " +
                                            "You cannot send messages to this customer, choose another customer");
                                } else {
                                    break;
                                }

                            } else {
                                System.out.println("Customer not found, try again");
                            }

                        }
                        break;
                    }

                } else if (choice.equals("3")) {
                    System.out.println("Enter store name: ");
                    String newStore = scan.nextLine();
                    storeList.add(newStore + "," + user.getName());
                    store.writeStores(storeList);
                    invalid = true;
                    System.out.println("Store added!");

                } else if (choice.equals("4")) {
                    String type = "seller";
                    boolean isdelete = edit(user.getName(), type);

                    if (isdelete) {
                        System.out.println("Account deleted!");
                        return;
                    } else {
                        System.out.println("Account edited!");
                        invalid = true;
                    }


                } else if (choice.equals("5")) {
                    System.out.println("Thank you for using the chat app!");
                    return;

                } else {
                    invalid = true;
                    System.out.println("Invalid option.");
                }
            } while (invalid);

            if (blockedList.contains(cust)) {
                System.out.println("You blocked this customer, you can still view, send, " +
                        "edit messages but they will not be able to see your messages.");
            }

            if (invisibleToList.contains(cust)) {
                System.out.println("You are invisible to this customer, you can still view, send, " +
                        "edit messages but they will not be able to see your messages.");
            }


            while (true) {
                System.out.println("Message history:");
                size = (messageList != null) ? messageList.size() : 0;
                LocalTime currentTime = LocalTime.now();
                int hours = currentTime.getHour();
                int minutes = currentTime.getMinute();
                String time = hours + ":" + minutes;
                if (size == 0) {
                    messageList.add(user.getName() + "," + cust + "," + time + "," + "START OF CONVO" + "," + "false," + "false," + "false," + "false");
                    message.writeMessages(messageList);
                }
                for (int x = 0; x < size; x++) {
                    if (messageList.get(x).contains(cust) && messageList.get(x).contains(user.getName())) {
                        String[] line = messageList.get(x).split(",");
                        String send = line[0].trim();
                        String receive = line[1].trim();
                        String showTime = line[2].trim();
                        String mess = line[3].trim();
                        if (!mess.equals("START OF CONVO")) {
                            System.out.println("Time: " + showTime + "    " + send + ": " + mess);
                        }
                    }
                }
                System.out.println("Choose an option");
                System.out.println("1. Write messages");
                System.out.println("2. Edit");
                System.out.println("3. Delete");
                System.out.println("4. Block");
                System.out.println("5. Invisible");
                System.out.println("6. Exit");

                choice = scan.nextLine();

                switch (choice) {
                    case "1":
                        while (true) {
                            // Write message in the format of
                            // (Sender, Receiver, TimeStamp, Message, false, false, false, false)
                            System.out.println("Enter message:          (Enter 'exit' to exit)");
                            String newMessage = scan.nextLine();
                            if (newMessage.equals("exit")) {
                                break;
                            }
                            currentTime = LocalTime.now();
                            hours = currentTime.getHour();
                            minutes = currentTime.getMinute();
                            time = hours + ":" + minutes;
                            messageList.add(user.getName() + "," + cust + "," + time + "," + newMessage +
                                    ",false,false,false,false");
                            message.writeMessages(messageList);

                        }
                        break;
                    case "2":
                        // enter the specific message you want to edit, and the new message
                        // Have to go through a for loop, until the message is found && the sender is the user
                        // otherwise, print out "choose a valid message to edit"
                        size = (messageList != null) ? messageList.size() : 0;
                        for (int i = 0; i < size; i++) {
                            System.out.println("Enter the message you want to edit: ");
                            String edit = scan.nextLine();
                            String[] messageToEdit = messageList.get(i).split(",");
                            if (messageToEdit[3].equals(edit)
                                    && messageToEdit[0].equals(user.getName()) && messageToEdit[1] == cust) {
                                System.out.println("Enter the new message: ");
                                String newMessage = scan.nextLine();
                                String newLine = messageToEdit[0] + "," + messageToEdit[1] + "," +
                                        messageToEdit[2] + "," + newMessage + "," + messageToEdit[4] + "," +
                                        messageToEdit[5] + "," + messageToEdit[6] + "," + messageToEdit[7];

                                messageList.set(i, newLine);
                                message.writeMessages(messageList);
                            }
                        }
                        //after the for loop, update the Message.txt file with the new messageList

                        break;
                    case "3":
                        // enter the specific message you want to delete
                        // Have to go through a for loop, until the message is found && the sender is the user
                        // otherwise, print out "choose a valid message to delete"
                        System.out.println("Enter the message you want to delete: ");
                        String delete = scan.nextLine();
                        size = (messageList != null) ? messageList.size() : 0;
                        for (int i = 0; i < size; i++) {
                            String[] messageToEdit = messageList.get(i).split(",");
                            if (messageToEdit[3].equals(delete)
                                    && messageToEdit[0].equals(user.getName()) && messageToEdit[1].equals(cust)) {
                                messageList.remove(i);
                                message.writeMessages(messageList);
                                break;
                            } else {
                                System.out.println("Error: Choose a valid message to delete");
                            }
                        }
                        //after the for loop, update the Message.txt file with the new messageList

                        break;
                    case "4":

                        // Have to go through a for loop through messageList, and turn the
                        // (4th index to true (Sender blocks receiver) if the user is the sender)
                        size = (messageList != null) ? messageList.size() : 0;
                        for (int i = 0; i < size; i++) {
                            String[] messageToEdit = messageList.get(i).split(",");
                            if (messageToEdit[0].equals(user.getName()) && messageToEdit[1].equals(cust)) {
                                String newLine = messageToEdit[0] + "," + messageToEdit[1] + "," +
                                        messageToEdit[2] + "," + messageToEdit[3] + "," + "true" + "," +
                                        messageToEdit[5] + "," + messageToEdit[6] + "," + messageToEdit[7];
                                messageList.set(i, newLine);
                                message.writeMessages(messageList);
                            }

                            if (messageToEdit[1].equals(user.getName()) && messageToEdit[0].equals(cust)) {
                                String newLine = messageToEdit[0] + "," + messageToEdit[1] + "," +
                                        messageToEdit[2] + "," + messageToEdit[3] + "," + messageToEdit[4] + "," +
                                        "true" + "," + messageToEdit[6] + "," + messageToEdit[7];
                                messageList.set(i, newLine);
                                message.writeMessages(messageList);
                            }
                            System.out.println("You have blocked " + cust + ". They will no longer be able to send " +
                                    "or view your messages. However, you can still interact with the messages.");
                        }

                        break;
                    case "5":
                        // Have to go through a for loop, and turn the
                        // (6th index to true (Sender invisible to receiver) if the user is the sender)
                        // (7th index to true (Receiver invisible to sender) if the user is the receiver)
                        size = (messageList != null) ? messageList.size() : 0;
                        for (int i = 0; i < size; i++) {
                            String[] messageToEdit = messageList.get(i).split(",");
                            if (messageToEdit[0].equals(user.getName()) && messageToEdit[1].equals(cust)) {
                                String newLine = messageToEdit[0] + "," + messageToEdit[1] + "," +
                                        messageToEdit[2] + "," + messageToEdit[3] + "," + messageToEdit[4] + "," +
                                        messageToEdit[5] + "," + "true" + "," + messageToEdit[7];
                                messageList.set(i, newLine);
                                message.writeMessages(messageList);
                            }

                            if (messageToEdit[1].equals(user.getName()) && messageToEdit[0].equals(cust)) {
                                String newLine = messageToEdit[0] + "," + messageToEdit[1] + "," +
                                        messageToEdit[2] + "," + messageToEdit[3] + "," + messageToEdit[4] + "," +
                                        messageToEdit[5] + "," + messageToEdit[6] + "," + "true" + "," + messageToEdit[7];
                                messageList.set(i, newLine);
                                message.writeMessages(messageList);
                            }

                        }
                        System.out.println("Now invisible to " + cust + "." + " They will no longer be able to see " +
                                "your store or your account.");
                        break;
                    case "6":
                        System.out.println("Thanks for using the messaging system!");
                        return;
                }
            }



            /* CUSTOMER OPTIONS */
        } else {
            int size = (messageList != null) ? messageList.size() : 0;
            for (int x = 0; x < size; x++) {
            /*
            0. Sender
            1. Receiver
            2. TimeStamp
            3. Message
            4. Sender blocks receiver
            5. Receiver blocks sender
            6. Sender invisible to receiver
            7. Receiver invisible to sender
             */
                String[] line = messageList.get(x).split(",");

                if ((line[0].trim().equals(user.getName())) || (line[1].trim().equals(user.getName()))) {
                    if (line[0].trim().equals(user.getName())) {
                        if (line[4].equals("true")) {
                            blockedList.add(line[1]);
                            //Added the receiver to blocked list to indicate that I blocked the receiver
                        }
                        if (line[5].equals("true")) {
                            blockedMeList.add(line[1]);
                            //Added the receiver to blocked me list to indicate that I am blocked by the receiver
                        }

                        if (line[6].equals("true")) {
                            invisibleToList.add(line[1]);
                            // Added the receiver to invisible list to indicate that I am invisible to the receiver
                        }

                        if (line[7].equals("true")) {
                            invisibleByList.add(line[1]);
                            // Added the receiver to invisible list to indicate that the receiver is invisible to me
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
            do {
                System.out.println("Choose an option");
                System.out.println("1. List the stores");
                System.out.println("2. Search seller");
                System.out.println("3. Edit Account");
                System.out.println("4. exit");
                choice = scan.nextLine();

                if (choice.equals("1")) {
                    size = (storeList != null) ? storeList.size() : 0;
                    if (storeList.size() == size) {
                        System.out.println("No stores found");
                        return;
                    }
                    System.out.println("Enter store number: ");
                    for (int i = 0; i < size; i++) {
                        if (!invisibleByList.contains(storeList.get(i).split(",")[1])) {
                            System.out.println((i + 1) + ": " + storeList.get(i).split(",")[0]);
                        }
                    }

                    while (true) {
                        choice = scan.nextLine();
                        int index = Integer.parseInt(choice);
                        storeName = storeList.get(index - 1);

                        String[] storeNameList = storeName.split(",");
                        sell = storeNameList[1];
                        if (blockedMeList.contains(sell)) {
                            System.out.println("This customer blocked you. " +
                                    "You cannot send messages to this customer, choose another customer");
                        } else {
                            System.out.println(sell + " is the owner of " + storeNameList[0]);
                            System.out.println("Connecting to " + sell + "...");
                            break;
                        }
                    }
                } else if (choice.equals("2")) {
                    while (true) {
                        System.out.println("Enter seller name: ");
                        sell = scan.nextLine();
                        //abcdefg
                        if (invisibleByList.contains(sell)) {
                            System.out.println("No seller found");
                        } else if ((sellerList.contains(sell)) && (!invisibleByList.contains(sell))) {
                            System.out.println("Seller found");

                            if (blockedMeList.contains(sell)) {
                                System.out.println("This customer blocked you. " +
                                        "You cannot send messages to this customer, choose another customer");
                            } else {
                                break;
                            }
                        } else {
                            System.out.println("Seller not found, try again");
                        }
                    }
                } else if (choice.equals("3")) {
                    String type = "customer";
                    boolean isdelete = edit(user.getName(), type);
                    if (isdelete) {
                        System.out.println("Account deleted!");
                        return;
                    } else {
                        System.out.println("Account edited!");
                        invalid = true;
                    }

                } else if (choice.equals("4")) {
                    System.out.println("Thanks for using the messaging system!");
                    return;
                } else {
                    System.out.println("Invalid input");
                }
            } while (invalid);

            if (blockedList.contains(sell)) {
                System.out.println("You blocked this seller, you can still view, send, " +
                        "edit messages but they will not be able to see your messages.");
            }

            if (invisibleToList.contains(sell)) {
                System.out.println("You are invisible to this seller, you can still view, send, " +
                        "edit messages but they will not be able to see your messages.");
            }


            while (true) {
                System.out.println("Message History:");
                size = (messageList != null) ? messageList.size() : 0;
                LocalTime currentTime = LocalTime.now();
                int hours = currentTime.getHour();
                int minutes = currentTime.getMinute();
                String time = hours + ":" + minutes;
                if (size == 0) {
                    messageList.add(user.getName() + "," + sell + "," + time + "," + "START OF CONVO" + "," + sell + "," + "false," + "false," + "false," + "false");
                    message.writeMessages(messageList);
                }
                for (int x = 0; x < size; x++) {
                    if (messageList.get(x).contains(sell) && messageList.get(x).contains(user.getName())) {
                        String[] line = messageList.get(x).split(",");
                        String send = line[0].trim();
                        String receive = line[1].trim();
                        String showTime = line[2].trim();
                        String mess = line[3].trim();
                        if (!mess.equals("START OF CONVO")) {
                            System.out.println("Time: " + showTime + "    " + send + ": " + mess);
                        }

                    }
                }
                System.out.println("Choose an option");
                System.out.println("1. Write messages");
                System.out.println("2. Edit");
                System.out.println("3. Delete");
                System.out.println("4. Block");
                System.out.println("5. Invisible");
                System.out.println("6. Exit");
                choice = scan.nextLine();

                switch (choice) {
                    case "1":
                        while (true) {
                            // Write message in the format of
                            // (Sender, Receiver, TimeStamp, Message, false, false, false, false)
                            System.out.println("Enter message:          (Enter 'exit' to exit)");
                            String newMessage = scan.nextLine();
                            if (newMessage.equals("exit")) {
                                break;
                            }
                            currentTime = LocalTime.now();
                            hours = currentTime.getHour();
                            minutes = currentTime.getMinute();
                            time = hours + ":" + minutes;
                            messageList.add(user.getName() + "," + sell + "," + time + "," + newMessage +
                                    ",false,false,false,false");
                            message.writeMessages(messageList);
                        }
                        break;
                    case "2":
                        // enter the specific message you want to edit, and the new message
                        // Have to go through a for loop, until the message is found && the sender is the user
                        // otherwise, print out "choose a valid message to edit"
                        size = (messageList != null) ? messageList.size() : 0;
                        for (int i = 0; i < size; i++) {
                            System.out.println("Enter the message you want to edit: ");
                            String edit = scan.nextLine();
                            String[] messageToEdit = messageList.get(i).split(",");
                            if (messageToEdit[3].equals(edit)
                                    && messageToEdit[0].equals(user.getName()) && messageToEdit[1] == cust) {
                                System.out.println("Enter the new message: ");
                                String newMessage = scan.nextLine();
                                String newLine = messageToEdit[0] + "," + messageToEdit[1] + "," +
                                        messageToEdit[2] + "," + newMessage + "," + messageToEdit[4] + "," +
                                        messageToEdit[5] + "," + messageToEdit[6] + "," + messageToEdit[7];

                                messageList.set(i, newLine);
                                message.writeMessages(messageList);
                            }
                        }
                        //after the for loop, update the Message.txt file with the new messageList

                        break;
                    case "3":
                        // enter the specific message you want to delete
                        // Have to go through a for loop, until the message is found && the sender is the user
                        // otherwise, print out "choose a valid message to delete"
                        System.out.println("Enter the message you want to delete: ");
                        String delete = scan.nextLine();
                        size = (messageList != null) ? messageList.size() : 0;
                        for (int i = 0; i < size; i++) {
                            String[] messageToEdit = messageList.get(i).split(",");
                            if (messageToEdit[3].equals(delete)
                                    && messageToEdit[0].equals(user.getName()) && messageToEdit[1].equals(sell)) {
                                messageList.remove(i);
                                message.writeMessages(messageList);
                                break;
                            } else {
                                System.out.println("Error: Choose a valid message to delete");
                            }
                        }
                        //after the for loop, update the Message.txt file with the new messageList

                        break;
                    case "4":

                        // Have to go through a for loop through messageList, and turn the
                        // (4th index to true (Sender blocks receiver) if the user is the sender)
                        size = (messageList != null) ? messageList.size() : 0;
                        for (int i = 0; i < size; i++) {
                            String[] messageToEdit = messageList.get(i).split(",");
                            if (messageToEdit[0].equals(user.getName()) && messageToEdit[1].equals(sell)) {
                                String newLine = messageToEdit[0] + "," + messageToEdit[1] + "," +
                                        messageToEdit[2] + "," + messageToEdit[3] + "," + "true" + "," +
                                        messageToEdit[5] + "," + messageToEdit[6] + "," + messageToEdit[7];
                                messageList.set(i, newLine);
                                message.writeMessages(messageList);
                            }

                            if (messageToEdit[1].equals(user.getName()) && messageToEdit[0].equals(sell)) {
                                String newLine = messageToEdit[0] + "," + messageToEdit[1] + "," +
                                        messageToEdit[2] + "," + messageToEdit[3] + "," + messageToEdit[4] + "," +
                                        "true" + "," + messageToEdit[6] + "," + messageToEdit[7];
                                messageList.set(i, newLine);
                                message.writeMessages(messageList);
                            }
                            System.out.println("You have blocked " + sell + ". They will no longer be able to send " +
                                    "or view your messages. However, you can still interact with the messages.");
                        }

                        break;
                    case "5":
                        // Have to go through a for loop, and turn the
                        // (6th index to true (Sender invisible to receiver) if the user is the sender)
                        // (7th index to true (Receiver invisible to sender) if the user is the receiver)
                        size = (messageList != null) ? messageList.size() : 0;
                        for (int i = 0; i < size; i++) {
                            String[] messageToEdit = messageList.get(i).split(",");
                            if (messageToEdit[0].equals(user.getName()) && messageToEdit[1].equals(sell)) {
                                String newLine = messageToEdit[0] + "," + messageToEdit[1] + "," +
                                        messageToEdit[2] + "," + messageToEdit[3] + "," + messageToEdit[4] + "," +
                                        messageToEdit[5] + "," + "true" + "," + messageToEdit[7];
                                messageList.set(i, newLine);
                                message.writeMessages(messageList);

                            }

                            if (messageToEdit[1].equals(user.getName()) && messageToEdit[0].equals(sell)) {
                                String newLine = messageToEdit[0] + "," + messageToEdit[1] + "," +
                                        messageToEdit[2] + "," + messageToEdit[3] + "," + messageToEdit[4] + "," +
                                        messageToEdit[5] + "," + "true" + "," + messageToEdit[7];
                                messageList.set(i, newLine);
                                message.writeMessages(messageList);
                            }
                        }
                        System.out.println("Now invisible to " + sell);
                        break;
                    case "6":
                        System.out.println("Thanks for using the messaging system!");
                        System.out.println("Goodbye!");
                        return;
                }
            }
        }

    }

    public static void login(String choice) throws IOException {
        String loginUsername = null;
        String loginPassword = null;
        String loginEmail = null;

        if (choice.equals("1")) {
            boolean usernameExists = false;
            BufferedReader br = null;
            while (!usernameExists) {
                try {
                    br = new BufferedReader(new FileReader("src/seller.txt"));
                    String line;
                    String username = Tools.username();

                    while ((line = br.readLine()) != null) {
                        String[] details = line.split(",");
                        if (details[0].trim().equals(username)) {
                            usernameExists = true;
                            loginUsername = details[0].trim();
                            loginPassword = details[1].trim();
                            loginEmail = details[2].trim();
                            break;
                        }
                    }
                    if (!usernameExists) {
                        System.out.println("Username does not exist. Please try again.");
                    }

                } catch (IOException e) {
                    System.out.println("Error reading file: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            System.out.println("Error closing file: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }

            while (true) {
                if (Tools.password().equals(loginPassword)) {
                    System.out.println("Login successful");
                    break;
                } else {
                    System.out.println("Invalid password, try again");
                }
            }
            user = new User(loginUsername, loginPassword, loginEmail);
            isSeller = true;


        } else if (choice.equals("2")) {

            boolean usernameExists = false;
            BufferedReader br = null;
            while (!usernameExists) {
                try {
                    br = new BufferedReader(new FileReader("src/customer.txt"));
                    String line;
                    String username = Tools.username();

                    while ((line = br.readLine()) != null) {
                        String[] details = line.split(",");
                        if (details[0].trim().equals(username)) {
                            usernameExists = true;
                            loginUsername = details[0].trim();
                            loginPassword = details[1].trim();
                            loginEmail = details[2].trim();
                            break;
                        }
                    }
                    if (!usernameExists) {
                        System.out.println("Username does not exist. Please try again.");
                    }

                } catch (IOException e) {
                    System.out.println("Error reading file: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            System.out.println("Error closing file: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }

            while (true) {
                if (Tools.password().equals(loginPassword)) {
                    System.out.println("Login successful");
                    break;
                } else {
                    System.out.println("Invalid password, try again");
                }
            }

            user = new User(loginUsername, loginPassword, loginEmail);
            isSeller = false;

        } else {
            System.out.println("Thanks for using the chat app!");
        }
    }

    public static void signup(String choice) {
        String loginUsername = null;
        String loginPassword = null;
        String loginEmail = null;

        if (choice.equals("1")) {

            BufferedReader br = null;
            ArrayList<String> usernameList = new ArrayList<>();
            ArrayList<String> passwordList = new ArrayList<>();
            ArrayList<String> emailList = new ArrayList<>();

            try {
                br = new BufferedReader(new FileReader("src/seller.txt"));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] details = line.split(",");
                    loginUsername = details[0].trim();
                    loginPassword = details[1].trim();
                    loginEmail = details[2].trim();

                    usernameList.add(loginUsername);
                    passwordList.add(loginPassword);
                    emailList.add(loginEmail);
                }

            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        System.out.println("Error closing file: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }


            while (true) {
                loginEmail = Tools.email();
                if (emailList.contains(loginEmail)) {
                    System.out.println("Email already exists. Please try again.");
                } else {
                    break;
                }
            }

            while (true) {
                loginUsername = Tools.username();
                if (usernameList.contains(loginUsername)) {
                    System.out.println("Username already exists. Please try again.");
                } else {
                    break;
                }
            }

            loginPassword = Tools.password();

            try {
                FileWriter fileWriter = new FileWriter("src/seller.txt", true);
                PrintWriter pw = new PrintWriter(fileWriter);
                pw.println(loginUsername + ", " + loginPassword + ", " + loginEmail);
                pw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            user = new User(loginUsername, loginPassword, loginEmail);
            isSeller = true;

        } else if (choice.equals("2")) {
            BufferedReader br = null;
            ArrayList<String> usernameList = new ArrayList<>();
            ArrayList<String> passwordList = new ArrayList<>();
            ArrayList<String> emailList = new ArrayList<>();

            try {
                br = new BufferedReader(new FileReader("src/customer.txt"));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] details = line.split(",");
                    loginUsername = details[0].trim();
                    loginPassword = details[1].trim();
                    loginEmail = details[2].trim();

                    usernameList.add(loginUsername);
                    passwordList.add(loginPassword);
                    emailList.add(loginEmail);
                }

            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        System.out.println("Error closing file: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            while (true) {
                loginEmail = Tools.email();
                if (emailList.contains(loginEmail)) {
                    System.out.println("Email already exists. Please try again.");
                } else {
                    break;
                }
            }

            while (true) {
                loginUsername = Tools.username();
                if (usernameList.contains(loginUsername)) {
                    System.out.println("Username already exists. Please try again.");
                } else {
                    break;
                }
            }

            loginPassword = Tools.password();

            try {
                FileWriter fileWriter = new FileWriter("src/customer.txt", true);
                PrintWriter pw = new PrintWriter(fileWriter);
                pw.println(loginUsername + ", " + loginPassword + ", " + loginEmail);
                pw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            user = new User(loginUsername, loginPassword, loginEmail);
            isSeller = false;

        } else {
            System.out.println("Thanks for using the chat app!");
        }
    }

    public static boolean edit(String username, String type) throws IOException {
        ArrayList<String> userDetailsList = new ArrayList<>();
        ArrayList<String> userNameList = new ArrayList<>();
        ArrayList<String> passwordList = new ArrayList<>();
        ArrayList<String> emailList = new ArrayList<>();
        String loginUsername = null;
        String loginPassword = null;
        String loginEmail = null;
        String status = null;
        System.out.println("Edit account options:");
        System.out.println("1. Change username");
        System.out.println("2. Change email");
        System.out.println("3. Change password");
        System.out.println("4. Delete account");
        String choice = scan.nextLine();
        BufferedReader br = null;

        try {
            if (type.equals("seller")) {
                br = new BufferedReader(new FileReader("src/seller.txt"));
            } else {
                br = new BufferedReader(new FileReader("src/customer.txt"));
            }
            String line;
            while ((line = br.readLine()) != null) {
                String[] seperate = line.split(",");
                userNameList.add(seperate[0].trim());
                passwordList.add(seperate[1].trim());
                emailList.add(seperate[2].trim());
                userDetailsList.add(line);
            }
            for (int i = 0; i < userDetailsList.size(); i++) {
                String[] details = userDetailsList.get(i).split(",");
                if (details[0].trim().equals(username)) {
                    if (choice.equals("1")) {
                        while (true) {
                            System.out.println("Enter new username: ");
                            String newUsername = scan.nextLine();
                            if (userNameList.contains(newUsername)) {
                                System.out.println("Username already exists. Please try again.");
                            } else {
                                userDetailsList.set(i, newUsername + "," + details[1].trim() + "," + details[2].trim());
                                user.setName(newUsername);
                                break;
                            }
                        }
                    } else if (choice.equals("2")) {
                        while (true) {
                            System.out.println("Enter new email: ");
                            String newEmail = scan.nextLine();
                            if (emailList.contains(newEmail)) {
                                System.out.println("Email already exists. Please try again.");
                            } else {
                                userDetailsList.set(i, details[0].trim() + "," + details[1].trim() + "," + newEmail);
                                break;
                            }
                        }
                    } else if (choice.equals("3")) {
                        System.out.println("Enter new password: ");
                        String newPassword = scan.nextLine();
                        userDetailsList.set(i, details[0].trim() + "," + newPassword + "," + details[2].trim());
                    } else if (choice.equals("4")) {
                        userDetailsList.remove(i);
                        status = "deleted";
                        i--; // Decrement the index to account for removed element
                        break; // Exit the loop as account is deleted
                    }
                }
            }

        } catch (IOException e) {
                throw new IOException(e);
            }
            if (type.equals("seller")) {
                    FileWriter fileWriter = new FileWriter("src/seller.txt");
                    PrintWriter pw = new PrintWriter(fileWriter);
                    for (int i = 0; i < userDetailsList.size(); i++) {
                        pw.println(userDetailsList.get(i));
                    }
                    pw.close();
                } else {
                    FileWriter fileWriter = new FileWriter("src/customer.txt");
                    PrintWriter pw = new PrintWriter(fileWriter);
                    for (int i = 0; i < userDetailsList.size(); i++) {
                        pw.println(userDetailsList.get(i));
                    }
                    pw.close();
                }
        if (status == null) {
            return false;
        } else {
            return true;
        }
    }
}



//    public void writeMessages() {
//        messageList.writeMessages(messageList);
//    }
