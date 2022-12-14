package edu.byu.cs.tweeter.server.dao.dto;

public class FullUser {
    private String password;
    private String firstName;
    private String lastName;
    private String alias;
    private String imageUrl;
    private Integer followerCount;
    private Integer followingCount;

    public FullUser() {}
    public FullUser(String password, String firstName, String lastName, String alias, String imageUrl, Integer followerCount, Integer followingCount) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.imageUrl = imageUrl;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }

    public Integer getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }
}
