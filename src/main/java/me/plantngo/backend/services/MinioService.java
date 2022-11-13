package me.plantngo.backend.services;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.SetBucketPolicyArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

@Service
public class MinioService {

    @Value("${minio.internalEndpoint}")
    private String minioEndpoint;

    @Value("${minio.externalEndpoint}")
    private String minioPublicAccessEndpoint;

    @Value("${minio.accessKey}")
    private String minioAccessKey;

    @Value("${minio.secretKey}")
    private String minioSecretKey;

    @Value("${minio.region}")
    private String minioRegion;

    @Value("${minio.bucket}")
    private String minioBucket;

    public String uploadFile(MultipartFile file, String serviceName, String merchantUsername)
            throws IOException, NoSuchAlgorithmException, InvalidKeyException,
            ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException,
            ServerException, XmlParserException, IllegalArgumentException {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioEndpoint)
                .credentials(minioAccessKey, minioSecretKey).region(minioRegion)
                .build();

        String uniqueFileName = file.getName() + "_" + java.util.UUID.randomUUID();

        minioClient.putObject(
                PutObjectArgs.builder().bucket(minioBucket)
                        .object(merchantUsername + "/" + serviceName + "/" + uniqueFileName)
                        .stream(new BufferedInputStream(file.getInputStream()), file.getSize(), -1)
                        .contentType("image/jpeg").build());

        return minioPublicAccessEndpoint + "/" + minioBucket + "/" + merchantUsername + "/" + serviceName + "/"
                + uniqueFileName;
    }

    public void initBuckets() throws InvalidKeyException, ErrorResponseException, InsufficientDataException,
            InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException,
            IllegalArgumentException, IOException {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioEndpoint)
                .credentials(minioAccessKey, minioSecretKey).region(minioRegion)
                .build();

        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioBucket).build());
        if (!found) {

            minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioBucket).build());
        } else {
            System.out.println("Bucket 'plantngodev' already exists.");
        }

        minioClient.setBucketPolicy(
                SetBucketPolicyArgs.builder().bucket(minioBucket).region(minioRegion).config(buildConfigJson())
                        .build());
    }

    public static String buildConfigJson() {

        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append("    \"Statement\": [\n");
        builder.append("        {\n");
        builder.append("            \"Action\": [\n");
        // builder.append(" \"s3:GetBucketLocation\",\n");
        builder.append("                \"s3:GetObject\"\n");
        builder.append("            ],\n");
        builder.append("            \"Effect\": \"Allow\",\n");
        builder.append("            \"Principal\": \"*\",\n");
        builder.append("            \"Resource\": \"arn:aws:s3:::plantngodev/*\"\n");
        builder.append("        }\n");
        builder.append("    ],\n");
        builder.append("    \"Version\": \"2012-10-17\"\n");
        builder.append("}\n");

        return builder.toString();
    }

}