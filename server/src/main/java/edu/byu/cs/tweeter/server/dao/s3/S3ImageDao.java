package edu.byu.cs.tweeter.server.dao.s3;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

import edu.byu.cs.tweeter.server.dao.ImageDao;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class S3ImageDao implements ImageDao {

    private static S3Client s3 = S3Client.builder().region(Region.US_WEST_1).build();
    private static final String IMAGE_BUCKET_NAME = "cs340-tweeter-image-bucket";

    @Override
    public String storeImage(String imageBytes64) {
        byte[] bytes = Base64.getDecoder().decode(imageBytes64);
        InputStream inputStream = new ByteArrayInputStream(bytes);

        String key = UUID.randomUUID().toString();
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(IMAGE_BUCKET_NAME)
                .key(key)
                .contentLength((long) bytes.length)
                .contentType("image/jpg")
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3.putObject(objectRequest, RequestBody.fromInputStream(inputStream, bytes.length));

        GetUrlRequest getUrlRequest = GetUrlRequest
                .builder()
                .region(Region.US_WEST_1)
                .bucket(IMAGE_BUCKET_NAME)
                .key(key)
                .build();

        URL url = s3.utilities().getUrl(getUrlRequest);
        return url.toString();
    }
}
