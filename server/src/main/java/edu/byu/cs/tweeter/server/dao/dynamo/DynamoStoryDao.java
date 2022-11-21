package edu.byu.cs.tweeter.server.dao.dynamo;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.StoryDao;
import edu.byu.cs.tweeter.server.dao.dynamo.bean.DynamoStatus;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class DynamoStoryDao extends DynamoBaseStatusDao implements StoryDao {
    private static final String STORY_TABLE_NAME = "story";
    private static final DynamoDbTable<DynamoStatus> storyTable = enhancedClient.table(STORY_TABLE_NAME, TableSchema.fromBean(DynamoStatus.class));

    @Override
    protected DynamoDbTable<DynamoStatus> getTableToQuery() {
        return storyTable;
    }

    @Override
    public void insertStatusToStory(Status status) {
        DynamoStatus dynamoStatus = new DynamoStatus(status.getUser().getAlias(), status);
        storyTable.putItem(dynamoStatus);
    }
}
