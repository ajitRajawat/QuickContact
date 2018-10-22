package ajflims.quickcontact;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import ajflims.quickcontact.Activities.AddNewActivity;
import ajflims.quickcontact.Activities.EditContactActivity;
import ajflims.quickcontact.Activities.SearchActivity;
import ajflims.quickcontact.Fragments.ContactFragment;
import ajflims.quickcontact.Fragments.FavouriteFragment;
import ajflims.quickcontact.Fragments.RecentFragment;
import ajflims.quickcontact.RoomDB.Contact;
import ajflims.quickcontact.RoomDB.ContactDatabase;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,BottomNavigationView.OnNavigationItemReselectedListener{

    private BottomNavigationView bottomNavigationView;
    public static ContactDatabase myDatabase;
    private android.support.v7.widget.Toolbar toolbar;
    private LinearLayout mSarchLayout;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        myDatabase = Room.databaseBuilder(HomeActivity.this, ContactDatabase.class,"contactdb").build();
        toolbar = findViewById(R.id.home_toolbar);

        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemReselectedListener(this);
        mSarchLayout = findViewById(R.id.home_search_layout);

        permission();

        displayFragment(new FavouriteFragment());

        mSarchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void permission() {

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.CALL_PHONE
            }, 10);

            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    return;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_deleteall,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home_delete_All:
                deleteAll();
                break;
            case R.id.home_help:
                Dialog dialog = new Dialog(HomeActivity.this);
                LayoutInflater inflater = (LayoutInflater) HomeActivity.this.getSystemService(HomeActivity.this.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.help_layout,null);
                dialog.setContentView(view);
                dialog.show();
                break;
        }
        return true;
    }

    private void deleteAll() {

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to Delete All?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              // new FavouriteFragment().execute();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment f;
        switch (item.getItemId()){
            case R.id.nav_favourite:
                f = new FavouriteFragment();
                break;
            case R.id.nav_recents:
                f = new RecentFragment();
                break;
            case R.id.nav_phonebook:
                f = new ContactFragment();
                break;
            default:
                f = new FavouriteFragment();
        }
        displayFragment(f);
        return true;
    }

    private void displayFragment(Fragment f) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_area,f).commit();
    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_favourite:
                break;
            case R.id.nav_recents:
                break;
            case R.id.nav_phonebook:
                break;
            default:
        }

    }
}







