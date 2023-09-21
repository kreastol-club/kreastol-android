package club.kreastol.community.Common.Items;

public class UserIsHereItem {
    private String firstName;
    private String lastName;
    private int userId;
    private boolean userIsHere;

    public UserIsHereItem(String firstName, String lastName, int userId, boolean userIsHere) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.userIsHere = userIsHere;
    }

    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isUserIsHere() {
        return userIsHere;
    }

    public void setUserIsHere(boolean userIsHere) {
        this.userIsHere = userIsHere;
    }
}
