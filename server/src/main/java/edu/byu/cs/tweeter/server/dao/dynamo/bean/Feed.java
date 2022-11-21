package edu.byu.cs.tweeter.server.dao.dynamo.bean;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.service.utility.DateUtility;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class Feed {
    private String user_handle;
    private Long date_posted;
    private String post;
    private List<String> urls;
    private List<String> mentions;
    private String firstName;
    private String lastName;
    private String imageUrl;

    public Feed() {}

    public Feed(Status status) {
        this.user_handle = status.getUser().getAlias();
        this.date_posted = DateUtility.getDateFromString(status.getDate());
        this.post = status.getPost();
        this.urls = status.getUrls();
        this.mentions = status.getMentions();
        this.firstName = status.getUser().getFirstName();
        this.lastName = status.getUser().getLastName();
        this.imageUrl = status.getUser().getImageUrl();
    }

    @DynamoDbPartitionKey
    public String getUser_handle() {
        return user_handle;
    }

    public void setUser_handle(String user_handle) {
        this.user_handle = user_handle;
    }

    @DynamoDbSortKey
    public Long getDate_posted() {
        return date_posted;
    }

    public void setDate_posted(Long date_posted) {
        this.date_posted = date_posted;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
