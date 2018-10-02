package ajflims.quickcontact;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import ajflims.quickcontact.Activities.AddNewActivity;
import ajflims.quickcontact.Activities.EditContactActivity;
import ajflims.quickcontact.RoomDB.Contact;
import ajflims.quickcontact.RoomDB.ContactDatabase;

public class HomeActivity extends AppCompatActivity {

    private FloatingActionButton mAddNew;
    public static ContactDatabase myDatabase;

    private android.support.v7.widget.Toolbar toolbar;
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private List<Contact> mList;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        myDatabase = Room.databaseBuilder(getApplicationContext(), ContactDatabase.class,"contactdb").build();

        mAddNew = findViewById(R.id.home_Add);
        toolbar = findViewById(R.id.home_toolbar);
        recyclerView = findViewById(R.id.home_recyclerview);
        setSupportActionBar(toolbar);

        permission();

        mAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,AddNewActivity.class));
            }
        });

        mList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));



        adapter = new ContactAdapter(mList,getApplicationContext());
        recyclerView.setAdapter(adapter);

        new fetchUser().execute();
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
        else{
            //Toasty.success(getActivity(),"Done",Toast.LENGTH_SHORT).show();
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
    protected void onRestart() {
        super.onRestart();
        mList.clear();
        new fetchUser().execute();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();
    }

    class fetchUser extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            List<Contact> list = HomeActivity.myDatabase.contactDao().getContact();
            for(Contact c : list) {
                mList.add(c);
            }

            return null;
        }
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
               new deleteAllUser().execute();
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

    class deleteAllUser extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(HomeActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
            mList.clear();
            adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            HomeActivity.myDatabase.contactDao().deleteAll(mList);

            return null;
        }
    }

}







