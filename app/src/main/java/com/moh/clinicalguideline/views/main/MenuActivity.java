package com.moh.clinicalguideline.views.main;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.databinding.ActivityMenuBinding;
import com.moh.clinicalguideline.helper.view.BaseActivity;
import com.moh.clinicalguideline.views.algorithm.AlgorithmActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;

public class MenuActivity extends BaseActivity implements MenuNavigator {

    @Inject
    public MenuViewModel viewModel;

    private ActivityMenuBinding viewModelBinding;

    private RecyclerView symptomsListView;

    private SearchView searchView;

    private String videoUrl;

    private Handler handler;

    private AlertDialog alertDialog;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.adult_symptom:
                viewModel.loadAdultSymptom();
                return true;
            case R.id.child_symptom:
                viewModel.loadChildSymptom();
                return true;
            case R.id.chronic_care:
                viewModel.loadChronic();
                return true;
            case R.id.all_symptom:
                viewModel.loadAll();
                return true;
        }
        return false;
    };

    private Runnable videoCopier = () -> {
        videoUrl = createVideoOnStorage();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        // Block Display Policy
        // Must agree to continue to use the app
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String agreementAccepted = sharedPref.getString(getResources().getString(R.string.agreed),"0") ;

        if (agreementAccepted.equals("0")) {
            final Dialog dialog = new Dialog(MenuActivity.this, android.R.style.Theme_Light);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_agreement);
            dialog.show();
            Button agreeAndContinue = dialog.findViewById(R.id.agree_and_continue);
            agreeAndContinue.setOnClickListener(v -> dialog.dismiss());
            dialog.setCancelable(false);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.agreed), "1");
            editor.apply();
        }
        //
        setContentView(R.layout.activity_menu);
        viewModel.setNavigator(this);
        viewModelBinding = DataBindingUtil.setContentView(this, R.layout.activity_menu);
        viewModelBinding.setMenu(viewModel);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.all_symptom);

       viewModel.createFooterList(this);
        searchView = (SearchView) findViewById(R.id.simpleSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                viewModel.getAdapter().getFilter().filter(s);
                return false;
            }
        });
        symptomsListView = (RecyclerView) findViewById(R.id.recycler_view);
        if (symptomsListView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) symptomsListView
                    .getLayoutManager();
            final int[] firstVisibleInListview = {linearLayoutManager.findFirstVisibleItemPosition()};
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView.clearFocus();
        postVideoCopier();
    }

    private String createVideoOnStorage() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + R.raw.guide + ".mp4";
        File file = new File(path);
        if (!file.exists()) {
            try {
                OutputStream out = new FileOutputStream(file);
                InputStream in = getResources().openRawResource(R.raw.guide);

                int buffer_size = 1024 * 1024;
                byte[] bytes = new byte[buffer_size];
                for (; ; ) {
                    int count = in.read(bytes, 0, buffer_size);
                    if (count == -1)
                        break;
                    out.write(bytes, 0, count);
                }
                in.close();
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file.getPath();
    }

    public void launchVideo(View view) {

        if (videoUrl != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(new File(videoUrl).getAbsolutePath()), "video/mp4");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Choose player to see PACK user guide video"));
        } else {
            Toast.makeText(this, "Couldn't play video. Please grant write to storage permission for this application and try again.", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void openSymptom(int nodeId) {
        Intent intent = new Intent(this, AlgorithmActivity.class);
        intent.putExtra(AlgorithmActivity.Extra_NodeId, nodeId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        postVideoCopier();

    }

    private void postVideoCopier() {
        if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            handler = new Handler();
            handler.post(videoCopier);
        }
    }
}
