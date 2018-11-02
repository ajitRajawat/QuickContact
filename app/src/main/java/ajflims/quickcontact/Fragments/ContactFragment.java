package ajflims.quickcontact.Fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ajflims.quickcontact.Adapter.ContactAdapter;
import ajflims.quickcontact.HomeActivity;
import ajflims.quickcontact.Model.CallLog;
import ajflims.quickcontact.Model.Contact;
import ajflims.quickcontact.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private List<Contact> mList;
    private List<ajflims.quickcontact.RoomDB.Contact> favList;
    private FloatingActionButton mNewContact;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.contact_list);
        mNewContact = view.findViewById(R.id.contact_add_contact);
        recyclerView.setHasFixedSize(true);
        mList = new ArrayList<>();
        favList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ContactAdapter(mList,getActivity(),favList);
        recyclerView.setAdapter(adapter);

        new checkForFav().execute();

        mNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                intent.putExtra("finishActivityOnSaveCompleted",true);
                startActivity(intent);
            }
        });

       // new ContactFetch().execute();
    }
    class checkForFav extends AsyncTask<Void,Void,List<ajflims.quickcontact.RoomDB.Contact>>{

        @Override
        protected List<ajflims.quickcontact.RoomDB.Contact> doInBackground(Void... voids) {
            List<ajflims.quickcontact.RoomDB.Contact> list;
            list = HomeActivity.myDatabase.contactDao().getContact();
            return list;
        }
        @Override
        protected void onPostExecute(List<ajflims.quickcontact.RoomDB.Contact> contacts) {
            super.onPostExecute(contacts);
            for(ajflims.quickcontact.RoomDB.Contact contact : contacts){
                favList.add(contact);
            }
            adapter.notifyDataSetChanged();
        }
    }

    class ContactFetch extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            String mName=null,mNumber=null;
            Cursor phones = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME +" ASC");
            if (phones != null) {
                while(phones.moveToNext()){
                    String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String key = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY));
                    String  name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String  number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if(!name.equals(mName)&&!number.equals(mNumber)) {
                         Contact contact = new Contact(name,number,id,key,"");
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


    @Override
    public void onStart() {
        super.onStart();
        mList.clear();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
            dialog.setMessage("Please Allow Contact Permission !!");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setTitle("Contact Permission");
            dialog.setButton("Allow", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            });
            dialog.show();
        } else {
            new ContactFetch().execute();
        }
    }
}
