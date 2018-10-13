package ajflims.quickcontact.Fragments;


import android.database.Cursor;
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
import java.util.List;

import ajflims.quickcontact.Adapter.ContactAdapter;
import ajflims.quickcontact.Model.Contact;
import ajflims.quickcontact.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private List<Contact> mList;
    String mName=null,mNumber=null;

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

        Cursor phones = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME +" ASC");
        if (phones != null) {
            while(phones.moveToNext()){
                String  name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String  number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                // Log.i("Ajit", "onViewCreated: 11111111");
               // Log.i("Ajit", "onViewCreated: " + name + "  " +  number);
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

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ContactAdapter(mList,getActivity());
        recyclerView.setAdapter(adapter);
    }
}
