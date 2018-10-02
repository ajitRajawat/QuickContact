package ajflims.quickcontact.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import ajflims.quickcontact.HomeActivity;
import ajflims.quickcontact.R;
import ajflims.quickcontact.RoomDB.Contact;

public class AddNewActivity extends AppCompatActivity {

    private ImageView mBackBtn;
    private TextInputLayout mName,mContact;
    private Button mChooseContact;
    private FloatingActionButton mSave;
    private static int PICK_CONTACT = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        mBackBtn = findViewById(R.id.add_back_button);
        mName = findViewById(R.id.add_name);
        mContact = findViewById(R.id.add_contact);
        mSave = findViewById(R.id.add_save);
        mChooseContact = findViewById(R.id.add_choose_contact);

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mChooseContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent,PICK_CONTACT);
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = mName.getEditText().getText().toString();
                String number = mContact.getEditText().getText().toString();

                if(!TextUtils.isEmpty(name) || !TextUtils.isEmpty(number)){

                    new AddUser().execute(name,number);

                }else{
                    Toast.makeText(AddNewActivity.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_CONTACT){
            if(resultCode == RESULT_OK){
                Cursor cursor = null;

                try {
                    String phoneNo;
                    String name;
                    Uri uri = data.getData();
                    cursor = getContentResolver().query(uri,null,null,null,null);
                    cursor.moveToFirst();
                    int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    phoneNo = cursor.getString(phoneIndex);
                    name = cursor.getString(nameIndex);

                    mName.getEditText().setText(name);
                    mContact.getEditText().setText(phoneNo);

                }catch (Exception e){
                }

            }else{
                //Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        }else{
           // Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
        }

    }

    public class AddUser extends AsyncTask<String,Void,Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(AddNewActivity.this, "Contact Added", Toast.LENGTH_SHORT).show();
            mName.getEditText().setText("");
            mContact.getEditText().setText("");
        }

        @Override
        protected Void doInBackground(String... strings) {

            String name = strings[0];
            String number = strings[1];

            Contact contact = new Contact();
            contact.setName(name);
            contact.setNumber(number);

            HomeActivity.myDatabase.contactDao().addContact(contact);

            return null;
        }
    }

}
