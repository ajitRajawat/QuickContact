package ajflims.quickcontact.Fragments;


import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ajflims.quickcontact.Adapter.ContactAdapter;
import ajflims.quickcontact.HomeActivity;
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
        recyclerView.setHasFixedSize(true);
        mList = new ArrayList<>();
        favList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ContactAdapter(mList,getActivity(),favList);
        recyclerView.setAdapter(adapter);

        try {
            new checkForFav().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        new ContactFetch().execute();
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
