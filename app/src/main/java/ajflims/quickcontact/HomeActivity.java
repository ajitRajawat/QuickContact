package ajflims.quickcontact;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import ajflims.quickcontact.Activities.SearchActivity;
import ajflims.quickcontact.Fragments.ContactFragment;
import ajflims.quickcontact.Fragments.FavouriteFragment;
import ajflims.quickcontact.Fragments.CalllogFragment;
import ajflims.quickcontact.RoomDB.Contact;
import ajflims.quickcontact.RoomDB.ContactDatabase;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemReselectedListener, View.OnClickListener, View.OnLongClickListener {

    private BottomNavigationView bottomNavigationView;
    public static ContactDatabase myDatabase;
    private android.support.v7.widget.Toolbar toolbar;
    private LinearLayout mSarchLayout;
    private FloatingActionButton mDialpad;
    private EditText mPhoneNumberField;
    private ImageView mDeleteText;
    int fragmentPos = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        myDatabase = Room.databaseBuilder(HomeActivity.this, ContactDatabase.class, "contactdb").build();
        toolbar = findViewById(R.id.home_toolbar);

        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemReselectedListener(this);
        mSarchLayout = findViewById(R.id.home_search_layout);
        mDialpad = findViewById(R.id.home_Add);

        mDialpad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialad();
            }
        });

        permission();

        displayFragment(new FavouriteFragment());

        mSarchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void displayDialad() {

        final BottomSheetDialog dialog = new BottomSheetDialog(HomeActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialer_layout, null);

        Button mOneButton, mTwoButton, mThreeButton, mFourButton, mFiveButton, mSixButton, mSevenButton, mEightButton, mNineButton, mZeroButton, mStarButton, mPoundButton;
        ImageView  mCloseDiapad;
        CircleImageView mDialButton;

        mPhoneNumberField = (EditText) view.findViewById(R.id.dialpad_edittext);
        mPhoneNumberField.setInputType(android.text.InputType.TYPE_NULL);
        mOneButton = (Button) view.findViewById(R.id.dialpad_one);
        mTwoButton = (Button) view.findViewById(R.id.dialpad_two);
        mThreeButton = (Button) view.findViewById(R.id.dialpad_three);
        mFourButton = (Button) view.findViewById(R.id.dialpad_four);
        mFiveButton = (Button) view.findViewById(R.id.dialpad_five);
        mSixButton = (Button) view.findViewById(R.id.dialpad_six);
        mSevenButton = (Button) view.findViewById(R.id.dialpad_seven);
        mEightButton = (Button) view.findViewById(R.id.dialpad_eight);
        mNineButton = (Button) view.findViewById(R.id.dialpad_nine);
        mZeroButton = (Button) view.findViewById(R.id.dialpad_zero);
        mStarButton = (Button) view.findViewById(R.id.dialpad_asterisk);
        mPoundButton = (Button) view.findViewById(R.id.dialpad_hash);
        mDeleteText = view.findViewById(R.id.dialpad_remove_text);
        mCloseDiapad = view.findViewById(R.id.dialpad_close);
        mDialButton = view.findViewById(R.id.dialpad_call);

        mZeroButton.setOnClickListener(this);
        mZeroButton.setOnLongClickListener(this);
        mOneButton.setOnClickListener(this);
        mTwoButton.setOnClickListener(this);
        mThreeButton.setOnClickListener(this);
        mFourButton.setOnClickListener(this);
        mFiveButton.setOnClickListener(this);
        mSixButton.setOnClickListener(this);
        mSevenButton.setOnClickListener(this);
        mEightButton.setOnClickListener(this);
        mNineButton.setOnClickListener(this);
        mStarButton.setOnClickListener(this);
        mPoundButton.setOnClickListener(this);
        mDialButton.setOnClickListener(this);
        mDeleteText.setOnClickListener(this);
        mDeleteText.setOnLongClickListener(this);

        mCloseDiapad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.show();

    }

    private void keyPressed(int keyCode) {
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        mPhoneNumberField.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.dialpad_one: {
                mDeleteText.setVisibility(View.VISIBLE);
                keyPressed(KeyEvent.KEYCODE_1);
                return;
            }
            case R.id.dialpad_two: {
                mDeleteText.setVisibility(View.VISIBLE);
                keyPressed(KeyEvent.KEYCODE_2);
                return;
            }
            case R.id.dialpad_three: {
                mDeleteText.setVisibility(View.VISIBLE);
                keyPressed(KeyEvent.KEYCODE_3);
                return;
            }
            case R.id.dialpad_four: {
                mDeleteText.setVisibility(View.VISIBLE);
                keyPressed(KeyEvent.KEYCODE_4);
                return;
            }
            case R.id.dialpad_five: {
                mDeleteText.setVisibility(View.VISIBLE);
                keyPressed(KeyEvent.KEYCODE_5);
                return;
            }
            case R.id.dialpad_six: {
                mDeleteText.setVisibility(View.VISIBLE);
                keyPressed(KeyEvent.KEYCODE_6);
                return;
            }
            case R.id.dialpad_seven: {
                mDeleteText.setVisibility(View.VISIBLE);
                keyPressed(KeyEvent.KEYCODE_7);
                return;
            }
            case R.id.dialpad_eight: {
                mDeleteText.setVisibility(View.VISIBLE);
                keyPressed(KeyEvent.KEYCODE_8);
                return;
            }
            case R.id.dialpad_nine: {
                mDeleteText.setVisibility(View.VISIBLE);
                keyPressed(KeyEvent.KEYCODE_9);
                return;
            }
            case R.id.dialpad_zero: {
                mDeleteText.setVisibility(View.VISIBLE);
                keyPressed(KeyEvent.KEYCODE_0);
                return;
            }
            case R.id.dialpad_hash: {
                mDeleteText.setVisibility(View.VISIBLE);
                keyPressed(KeyEvent.KEYCODE_POUND);
                return;
            }
            case R.id.dialpad_asterisk: {
                mDeleteText.setVisibility(View.VISIBLE);
                keyPressed(KeyEvent.KEYCODE_STAR);
                return;
            }
            case R.id.dialpad_remove_text: {
                mDeleteText.setVisibility(View.VISIBLE);
                keyPressed(KeyEvent.KEYCODE_DEL);
                return;
            }
            case R.id.dialpad_call: {
                dialNumber();
                return;
            }
        }
    }

    private void dialNumber() {
        String number = mPhoneNumberField.getText().toString();
        if (number.length() > 0) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(new Intent(Intent.ACTION_CALL,
                    Uri.parse("tel:" + number)));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.dialpad_remove_text: {
                Editable digits = mPhoneNumberField.getText();
                digits.clear();
                return true;
            }
            case R.id.dialpad_zero: {
                mDeleteText.setVisibility(View.VISIBLE);
                keyPressed(KeyEvent.KEYCODE_PLUS);
                return true;
            }
        }
        return false;
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
        builder.setMessage("Are you sure you want to Unfavourite all?");

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
            if(fragmentPos==1){
                displayFragment(new FavouriteFragment());
            }else if(fragmentPos == 2){
            }else if(fragmentPos == 3){
                displayFragment(new ContactFragment());
            }else{

            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            List<Contact> contacts = HomeActivity.myDatabase.contactDao().getContact();
            HomeActivity.myDatabase.contactDao().deleteAll(contacts);
            return null;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment f;
        switch (item.getItemId()){
            case R.id.nav_favourite:
                fragmentPos = 1;
                f = new FavouriteFragment();
                break;
            case R.id.nav_recents:
                fragmentPos = 2;
                f = new CalllogFragment();
                break;
            case R.id.nav_phonebook:
                fragmentPos = 3;
                f = new ContactFragment();
                break;
            default:
                fragmentPos = 1;
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







