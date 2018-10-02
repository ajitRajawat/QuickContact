package ajflims.quickcontact.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import ajflims.quickcontact.HomeActivity;
import ajflims.quickcontact.R;
import ajflims.quickcontact.RoomDB.Contact;

public class EditContactActivity extends AppCompatActivity {

    private TextInputLayout mName,mContact;
    private FloatingActionButton mSave;
    private ImageView mBack,mDeleteOption;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        String name = getIntent().getStringExtra("name");
        String number = getIntent().getStringExtra("number");
        id = getIntent().getIntExtra("id",0);

        mName = findViewById(R.id.edit_name);
        mContact = findViewById(R.id.edit_contact);
        mSave = findViewById(R.id.edit_save);
        mDeleteOption = findViewById(R.id.edit_option_button);
        mBack = findViewById(R.id.edit_back_button);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mName.getEditText().setText(name);
        mContact.getEditText().setText(number);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new updateUser().execute();
            }
        });

        mDeleteOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteOption(v);
            }
        });

    }

    private void showDeleteOption(View v) {

        PopupMenu popup = new PopupMenu(EditContactActivity.this,v);
        // This activity implements OnMenuItemClickListener
        popup.inflate(R.menu.edit_delete);
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditContactActivity.this);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to Delete?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new deleteUser().execute();
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
                return true;
            }
        });

    }

    class deleteUser extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(EditContactActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            int userid = id;
            Contact contact = new Contact();
            contact.setId(userid);

            HomeActivity.myDatabase.contactDao().deleteContact(contact);

            return null;
        }
    }


    class updateUser extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(EditContactActivity.this, "Updated", Toast.LENGTH_SHORT).show();
            mName.getEditText().setText("");
            mContact.getEditText().setText("");
        }

        @Override
        protected Void doInBackground(Void... voids) {

            int userid = id;
            String name = mName.getEditText().getText().toString();
            String contact = mContact.getEditText().getText().toString();

            if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(contact)){

                Contact c = new Contact();
                c.setId(userid);
                c.setName(name);
                c.setNumber(contact);

                HomeActivity.myDatabase.contactDao().updateContact(c);

            }else{
                Toast.makeText(EditContactActivity.this, "Some Fields are Empty", Toast.LENGTH_SHORT).show();
            }

            return null;
        }
    }
}


