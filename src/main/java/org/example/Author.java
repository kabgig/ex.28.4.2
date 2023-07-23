package org.example;


public class Author {

    private String firstName;

    private String lastName;

    private String biography;

    @BuilderField
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @BuilderField
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @BuilderField
    public void setBiography(String biography) {
        this.biography = biography;
    }
}
