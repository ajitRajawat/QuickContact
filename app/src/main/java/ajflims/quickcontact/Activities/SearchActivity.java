package ajflims.quickcontact.Activities;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ajflims.quickcontact.Adapter.ContactAdapter;
import ajflims.quickcontact.Adapter.SearchAdapter;
import ajflims.quickcontact.Fragments.ContactFragment;
import ajflims.quickcontact.HomeActivity;
import ajflims.quickcontact.Model.Contact;
import ajflims.quickcontact.R;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private Toolbar toolbar;
    private SearchView searchView;
    private ImageView mBack;
    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private List<Contact> mList;
    private List<ajflims.quickcontact.RoomDB.Contact> favList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.search_toolbar);
        searchView = findViewById(R.id.toolbar_searchView);
        mBack = findViewById(R.id.search_back_button);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.search_list);
        recyclerView.setHasFixedSize(true);
        mList = new ArrayList<>();
        favList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchAdapter(mList,this,favList);
        recyclerView.setAdapter(adapter);

        try {
            new checkForFav().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        new ContactFetch().execute();

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if (getSystemService(Context.INPUT_METHOD_SERVICE) != null) {
                        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).
                                toggleSoftInput(InputMethodManager.SHOW_FORCED,
                                        InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                }
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboardFrom(SearchActivity.this,v);
                onBackPressed();
            }
        });

        searchView.setOnQueryTextListener(this);
    }


    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        newText = newText.toLowerCase();
        ArrayList<Contact> newList = new ArrayList<>();
        for(Contact contact : mList){
            String name = contact.getName().toLowerCase();
            if(name.contains(newText)){
                newList.add(contact);
            }
        }
        adapter.setFilter(newList);
        return true;
    }

    class checkForFav extends AsyncTask<Void,Void,List<ajflims.quickcontact.RoomDB.Contact>> {

        @Override
        protected List<ajflims.quickcontact.RoomDB.Contact> doInBackground(Void... voids) {
            List<ajflims.quickcontact.RoomDB.Contact> list;
            list = HomeActivity.myDatabase.contactDao().getContact();
            return list;
        }
        @Override
        protected void onPostExecute(List<ajflims.quickcontact.RoomDB.Contact> contacts) {
            super.onPostExecute(contacts);
            favList.addAll(contacts);
            adapter.notifyDataSetChanged();
        }
    }

    class ContactFetch extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            String mName=null,mNumber=null;
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME +" ASC");
            if (phones != null) {
                while(phones.moveToNext()){
                    String  name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String  number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if(!name.equals(mName)&&!number.equals(mNumber)) {
                        Contact contact = new Contact(name, number);
                        mList.add(contact);
                    }
                    mName = name;
                    mNumber = number;
                }
            }
            if (phones != null) {
                phones.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Collections.sort(mList, new Comparator<Contact>() {
                @Override
                public int compare(Contact o1, Contact o2) {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
            });
            adapter.notifyDataSetChanged();
        }
    }

}
