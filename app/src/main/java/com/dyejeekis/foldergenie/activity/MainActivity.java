package com.dyejeekis.foldergenie.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.dyejeekis.foldergenie.R;
import com.dyejeekis.foldergenie.util.FileUtil;
import com.dyejeekis.foldergenie.util.GeneralUtil;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.dyejeekis.foldergenie.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends BaseActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        binding.fab.setOnClickListener(v -> {
            showInfoDialog();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // TODO: 6/6/2021
            return true;
        } else if (id == R.id.action_grant_permissions) {
            checkPermissions();
            return true;
        } else if (id == R.id.action_generate_test_files) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            //intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uriToLoad);
            activityLauncher.launch(intent, result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Uri treeUri = result.getData().getData();

                    // TODO: 7/8/2021 permission denied when creating test files in sd card
                    //DocumentFile pickedDir = DocumentFile.fromTreeUri(this, treeUri);
                    //grantUriPermission(getPackageName(), treeUri,
                    //        Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    //getContentResolver().takePersistableUriPermission(treeUri,
                    //        Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


                    File file = new File(FileUtil.getFullPathFromTreeUri(treeUri, this));
                    Log.d(TAG, "Generating test files in " + file.getAbsolutePath());
                    Exception ex = null;
                    final int fileCount = 100;
                    try {
                        GeneralUtil.generateTestFiles(file, fileCount);
                    } catch (IOException e) {
                        ex = e;
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to generate test files", Toast.LENGTH_SHORT).show();
                    }
                    if (ex == null) Toast.makeText(this, fileCount + " test files generated successfully in " +
                            file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                }
            });
            return true;
        } else if (id == R.id.action_about) {
            showInfoDialog();
            return true;
        } else if (id == R.id.action_sort_presets) {
            // TODO: 6/6/2021
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public BetterActivityResult<Intent, ActivityResult> getActivityLauncher() {
        return activityLauncher;
    }

    private void showInfoDialog() {
        // TODO: 6/6/2021
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "Permissions granted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to grant required permissions", Toast.LENGTH_SHORT).show();
                }
            });

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissions already granted", Toast.LENGTH_SHORT).show();
        } else requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}