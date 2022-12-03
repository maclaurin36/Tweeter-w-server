package edu.byu.cs.tweeter.server.dao.dynamo;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.server.dao.FeedDao;
import edu.byu.cs.tweeter.server.dao.dynamo.bean.DynamoStatus;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class DynamoFeedDao extends DynamoBaseStatusDao implements FeedDao {
    private static final String FEED_TABLE_NAME = "feed";
    private static final DynamoDbTable<DynamoStatus> feedTable = enhancedClient.table(FEED_TABLE_NAME, TableSchema.fromBean(DynamoStatus.class));

    @Override
    protected DynamoDbTable<DynamoStatus> getTableToQuery() {
        return feedTable;
    }

    @Override
    public void insertStatusToFeed(String feedAlias, Status status) {
        DynamoStatus dynamoStatus = new DynamoStatus(feedAlias, status);
        feedTable.putItem(dynamoStatus);
    }

    @Override
    public void insertAllStatusToFeed(List<String> aliases, Status status) {
        List<DynamoStatus> dynamoStatusList = new ArrayList<>();
        for (String alias : aliases) {
            DynamoStatus dynamoStatus = new DynamoStatus(alias, status);
            dynamoStatusList.add(dynamoStatus);
        }
        writeStatusChunk(dynamoStatusList);
    }

    private void writeStatusChunk(List<DynamoStatus> dynamoStatusList) {
        WriteBatch.Builder<DynamoStatus> writeBuilder = WriteBatch.builder(DynamoStatus.class).mappedTableResource(feedTable);
        for (DynamoStatus item : dynamoStatusList) {
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(feedTable).size() > 0) {
                writeStatusChunk(result.unprocessedPutItemsForTable(feedTable));
            }

        } catch (DynamoDbException e) {
            System.out.println(e.getMessage());
        }
    }
}
