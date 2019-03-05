/**
 * ----------------------------------------------------------------------------------
 * Microsoft Developer & Platform Evangelism
 * <p>
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * <p>
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND,
 * EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR PURPOSE.
 * ----------------------------------------------------------------------------------
 * The example companies, organizations, products, domain names,
 * e-mail addresses, logos, people, places, and events depicted
 * herein are fictitious.  No association with any real company,
 * organization, product, domain name, email address, logo, person,
 * places, or events is intended or should be inferred.
 * ----------------------------------------------------------------------------------
 **/

package com.ameerhamza6733.okAmeer.utial.Uploader;

import android.util.Log;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlobDirectory;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.LinkedList;

public class ImageManager {
    /*
    **Only use Shared Key authentication for testing purposes!** 
    Your account name and account key, which give full read/write access to the associated Storage account, 
    will be distributed to every person that downloads your app. 
    This is **not** a good practice as you risk having your key compromised by untrusted clients. 
    Please consult following documents to understand and use Shared Access Signatures instead. 
    https://docs.microsoft.com/en-us/rest/api/storageservices/delegating-access-with-a-shared-access-signature 
    and https://docs.microsoft.com/en-us/azure/storage/common/storage-dotnet-shared-access-signature-part-1 
    */
    public static final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=giflivestorge;AccountKey=QdYMyB9D7yfHjzSmaNRtTAJiOkl9GbVTVvS2gHWmNqvICTwIRmxF99G5tslqSb5VlmnGUYJ7KkeKD/bTgApyKg==;EndpointSuffix=core.windows.net;"
            + "AccountName=giflivestorge;"
            + "AccountKey=QdYMyB9D7yfHjzSmaNRtTAJiOkl9GbVTVvS2gHWmNqvICTwIRmxF99G5tslqSb5VlmnGUYJ7KkeKD/bTgApyKg==";
    static final String validChars = "abcdefghijklmnopqrstuvwxyz";
    private static final String TAG = "Image manger TAG ";
    static SecureRandom rnd = new SecureRandom();

    private static CloudBlobContainer getContainer() throws Exception {
        // Retrieve storage account from connection-string.

        CloudStorageAccount storageAccount = CloudStorageAccount
                .parse(storageConnectionString);

        // Create the blob client.
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

        // Get a reference to a container.
        // The container name must be lower case
        CloudBlobContainer container = blobClient.getContainerReference("okameer");

        return container;
    }

    public static String UploadImage(InputStream fileInputStream, int fileLength, String fileName) throws Exception {
        CloudBlobContainer container = getContainer();

        container.createIfNotExists();


        CloudBlockBlob imageBlob = container.getBlockBlobReference(fileName);
        imageBlob.upload(fileInputStream, fileLength);


        return fileName;

    }

    public static String UploadImage(String filepath,String folderName) throws Exception {
        final String fileName = new File(filepath).getName();
        Log.d(TAG, "to be upload to server file name : " + fileName);
        CloudBlobContainer container = getContainer();

        container.createIfNotExists();
        CloudBlobDirectory dir = container.getDirectoryReference(folderName);
        CloudBlockBlob imageBlob = dir.getBlockBlobReference(fileName);
        if (imageBlob.exists())
            imageBlob.delete();
            imageBlob.uploadFromFile(filepath);

        return fileName;

    }

    public static String[] ListImages() throws Exception {
        CloudBlobContainer container = getContainer();

        Iterable<ListBlobItem> blobs = container.listBlobs();

        LinkedList<String> blobNames = new LinkedList<>();
        for (ListBlobItem blob : blobs) {
            blobNames.add(((CloudBlockBlob) blob).getName());
        }

        return blobNames.toArray(new String[blobNames.size()]);
    }

    public static void GetImage(String name, OutputStream imageStream, long imageLength) throws Exception {
        CloudBlobContainer container = getContainer();

        CloudBlockBlob blob = container.getBlockBlobReference(name);

        if (blob.exists()) {
            blob.downloadAttributes();

            imageLength = blob.getProperties().getLength();

            blob.download(imageStream);
        }
    }

    static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(validChars.charAt(rnd.nextInt(validChars.length())));
        return sb.toString();
    }

}
