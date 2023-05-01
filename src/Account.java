public class Account {
    private String email;
    private String username;
    private String password;
    private boolean isSeller;

    public Account (String username, String password, String email, boolean isSeller) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.isSeller = isSeller;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isSeller() {
        return isSeller;
    }

    public void setSeller(boolean seller) {
        isSeller = seller;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
