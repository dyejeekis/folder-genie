package com.dyejeekis.foldergenie;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.dyejeekis.foldergenie.model.FolderSort;

public class GenieService extends JobIntentService {

    public static final String TAG = GenieService.class.getSimpleName();

    public static final int JOB_ID = 1000;

    public static final String RECEIVER = "receiver";

    public static final String ACTION_SORT_FILES = "action.SORT_FILES";
    public static final String ACTION_FLATTEN_FILE_TREE = "action.FLATTEN_FILE_TREE";

    public static final String EXTRA_FOLDER_SORT = "folderSort";
    public static final String EXTRA_FOLDER_FLATTEN = "folderFlatten";

    private ResultReceiver resultReceiver;

    public static void enqueueFolderSort(Context context, ServiceResultReceiver
                                   workerResultReceiver, FolderSort folderSort) {
        Intent intent = new Intent(context, GenieService.class);
        intent.putExtra(RECEIVER, workerResultReceiver);
        intent.putExtra(EXTRA_FOLDER_SORT, folderSort);
        intent.setAction(ACTION_SORT_FILES);
        enqueueWork(context, GenieService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork() called with: intent = [" + intent + "]");
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_SORT_FILES:
                    // TODO: 6/6/2021
                    break;
                case ACTION_FLATTEN_FILE_TREE:
                    // TODO: 6/6/2021
                    break;
            }
        }
    }
}