package edu.byu.cs.tweeter.server.dao.dynamo.bean;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class Authentication {
    private String alias;
    private Long expiration;
    private String token;

    @DynamoDbPartitionKey
    public String getToken() {
        return token;
    }

    @DynamoDbSortKey
    public Long getExpiration() {
        return expiration;
    }

    public Authentication() {}

    public Authentication(String alias, AuthToken authToken) {
        this.alias = alias;
        this.expiration = authToken.getExpiration();
        this.token = authToken.getToken();
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
