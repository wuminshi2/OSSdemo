package com.wuminshi2.ossdemo;

import com.aliyun.oss.*;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.wuminshi2.ossdemo.properties.AliOssProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class OsSdemoApplicationTests {
    @Autowired
    private AliOssProperties aliOssProperties;

    @Test
    void simplyUpload() throws  Exception{
        String endpoint = aliOssProperties.getEndpoint();
        String accessKeyId = aliOssProperties.getAccessKeyId();
        String accessKeySecret = aliOssProperties.getAccessKeySecret();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = aliOssProperties.getBucketName();
        // 填写 Bucket 所在地域。以华东 1（杭州）为例，Region 填写为 cn-hangzhou。
        //String region = aliOssProperties.getRegion();
        // 填写 Object 完整路径，完整路径中不能包含 Bucket 名称，例如 exampledir/exampleobject.txt。
        String objectName = "images/exampleobject.png";
        // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        String filePath= "src/main/resources/static/images/exampleImage.png";

        // 创建 OSSClient 实例。
        // 当 OSSClient 实例不再使用时，调用 shutdown 方法以释放资源。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V1);
        
        //创建 OSSClient 实例
        OSS ossClient = new OSSClientBuilder().build(
                endpoint,
                accessKeyId,
                accessKeySecret,
                clientBuilderConfiguration
        );

        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new File(filePath));
            // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);

            // 上传文件。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Test
    void simplyDownload() throws Exception{
        String endpoint = aliOssProperties.getEndpoint();
        String accessKeyId = aliOssProperties.getAccessKeyId();
        String accessKeySecret = aliOssProperties.getAccessKeySecret();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = aliOssProperties.getBucketName();
        // 填写 Bucket 所在地域。以华东 1（杭州）为例，Region 填写为 cn-hangzhou。
        //String region = aliOssProperties.getRegion();
        // 填写不包含Bucket名称在内的Object完整路径，例如testfolder/exampleobject.txt。
        String objectName = "images/exampleobject.png";
        // 填写Object下载到本地的完整路径。
        String pathName = "src/main/resources/static/download/example.png";

        // 创建 OSSClient 实例。
        // 当 OSSClient 实例不再使用时，调用 shutdown 方法以释放资源。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V1);

        //创建 OSSClient 实例
        OSS ossClient = new OSSClientBuilder().build(
                endpoint,
                accessKeyId,
                accessKeySecret,
                clientBuilderConfiguration
        );

        try {
            // 下载Object到本地文件，并保存到指定的本地路径中。如果指定的本地文件存在会覆盖，不存在则新建。
            // 如果未指定本地路径，则下载后的文件默认保存到示例程序所属项目对应本地路径中。
            ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File(pathName));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
