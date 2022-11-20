package edu.byu.cs.tweeter.server.dao.dynamo;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.server.dao.UserDao;
import edu.byu.cs.tweeter.server.dao.dto.FullUser;
import edu.byu.cs.tweeter.server.dao.dynamo.bean.Authentication;
import edu.byu.cs.tweeter.server.dao.dynamo.bean.PasswordUser;
import edu.byu.cs.tweeter.util.FakeData;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class DynamoUserDao extends BaseDynamoDao implements UserDao {

    private static final String AuthTableName = "authentication";
    private static final String UserTableName = "user";
    private static final DynamoDbTable<Authentication> authTable = enhancedClient.table(AuthTableName, TableSchema.fromBean(Authentication.class));
    private static final DynamoDbTable<PasswordUser> userTable = enhancedClient.table(UserTableName, TableSchema.fromBean(PasswordUser.class));

    @Override
    public FullUser getUser(String alias) {
        Key key = Key.builder()
                .partitionValue(alias)
                .build();

        PasswordUser passwordUser = userTable.getItem(key);

        if (passwordUser == null) {
            return null;
        }

        return new FullUser(passwordUser.getPassword(), passwordUser.getFirstName(), passwordUser.getLastName(), passwordUser.getUser_handle(), passwordUser.getImageUrl());
    }

    @Override
    public Boolean invalidateAuthToken(AuthToken authToken) {
        Key key = Key.builder()
                .partitionValue(authToken.getToken())
                .sortValue(authToken.getExpiration())
                .build();
        authTable.deleteItem(key);
        return true;
    }

    @Override
    public Boolean insertAuthToken(String alias, AuthToken authToken) {
        Authentication authentication = new Authentication(alias, authToken);
        authTable.putItem(authentication);
        return true;
    }

    @Override
    public void insertUser(FullUser user) {
        PasswordUser passwordUser = new PasswordUser(user);
        userTable.putItem(passwordUser);
    }

    @Override
    public AuthenticateResponse register(RegisterRequest request) {
        return new AuthenticateResponse(FakeData.getInstance().getFirstUser(), FakeData.getInstance().getAuthToken());
    }

    public User getUser(AuthToken authToken) {
        return new User("dummyFirstName","dummyLastName","someAlias","dummyImageUrl");
    }

    @Override
    public AuthToken getAuthToken(LoginRequest request) {
        return FakeData.getInstance().getAuthToken();
    }
}
