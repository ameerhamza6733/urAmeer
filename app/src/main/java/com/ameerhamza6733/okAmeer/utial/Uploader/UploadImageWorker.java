package com.ameerhamza6733.okAmeer.utial.Uploader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UploadImageWorker extends Worker {

    public static final String KEY_IMAGE_PATH_ARG = "KEY_IMAGE_PATH_ARG";
    public static final String KEY_FOLDER_NAME="KEY_FOLDER_NAME";
    private static final String TAG = "UploadImageWorkerTAG";

    public UploadImageWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        Log.d(TAG, "upload image worker created ");
    }


    @Override
    public Result doWork() {

        try {
            final String imagePath = getInputData().getString(KEY_IMAGE_PATH_ARG);
            final String folderName = getInputData().getString(KEY_FOLDER_NAME);
            if (imagePath == null) {
                Log.e(TAG, "unable to get image path for uploading ");
                return Result.success();
            }

            final File file = new File(imagePath);
            if (file.exists()) {
                final String imageName = ImageManager.UploadImage(imagePath,folderName);
                Log.d(TAG, "image uploaded to server :" + imageName);

            } else {
                Log.e(TAG, "File not exists ");
            }
        } catch (Exception e) {
          //  Crashlytics.log("Error while uploading photo to azuzse account ");
            //Crashlytics.logException(e);
            e.printStackTrace();
        }
        return Result.success();

    }
}