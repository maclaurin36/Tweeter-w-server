package edu.byu.cs.tweeter.server.dao.dynamo.bean;

import edu.byu.cs.tweeter.server.dao.dto.FullUser;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class PasswordUser {
    private String firstName;
    private String lastName;
    private String user_handle;
    private String imageUrl;
    private String password;
    private Integer followerCount;
    private Integer followingCount;

    public PasswordUser() { }
    public PasswordUser(FullUser fullUser) {
        this.firstName = fullUser.getFirstName();
        this.lastName = fullUser.getLastName();
        this.user_handle = fullUser.getAlias();
        this.imageUrl = fullUser.getImageUrl();
        this.password = fullUser.getPassword();
        this.followerCount = fullUser.getFollowerCount();
        this.followingCount = fullUser.getFollowingCount();
    }

    @DynamoDbPartitionKey
    public String getUser_handle() {
        return user_handle;
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

    public void setUser_handle(String alias) {
        this.user_handle = alias;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
