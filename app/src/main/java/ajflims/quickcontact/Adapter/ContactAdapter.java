package ajflims.quickcontact.Adapter;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import ajflims.quickcontact.HomeActivity;
import ajflims.quickcontact.Model.Contact;
import ajflims.quickcontact.R;
import ajflims.quickcontact.RoomDB.ContactDatabase;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    final String TAG = "Ajit";
    private List<Contact> mList;
    private List<ajflims.quickcontact.RoomDB.Contact> favList;
    private Context mCtx;

    public ContactAdapter(List<Contact> mList, Context mCtx, List<ajflims.quickcontact.RoomDB.Contact> favList) {
        this.mList = mList;
        this.mCtx = mCtx;
        this.favList = favList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mCtx).inflate(R.layout.reyclerview_contactlist, parent, false);
        return new ContactViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {

        Contact c = mList.get(position);
        holder.mName.setText(c.getName());
        boolean b = false;
        for (ajflims.quickcontact.RoomDB.Contact contact : favList) {
            if (Objects.equals(contact.getNumber(), c.getNumber())) {
                b = true;
                break;
            }
        }
        if (b) {
            holder.mFavorite.setChecked(true);
        } else {
            holder.mFavorite.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        CheckBox mFavorite;
        CircleImageView mProfile;
        TextView mName;

        public ContactViewHolder(View itemView) {
            super(itemView);

            mFavorite = itemView.findViewById(R.id.contact_favourite);
            mProfile = itemView.findViewById(R.id.contact_profile);
            mName = itemView.findViewById(R.id.contact_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Contact c = mList.get(position);
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + c.getNumber()));
                    if (ActivityCompat.checkSelfPermission(mCtx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mCtx.startActivity(intent);
                }
            });

            mFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Contact c = mList.get(position);
                    if(mFavorite.isChecked()){
                        new AddContact().execute(c);
                    }else{
                        new DeleteContact().execute(c);
                    }

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Contact contact = mList.get(getAdapterPosition());
                    String mCurrentLookupKey;
                    long mCurrentId;
                    Uri mSelectedContactUri;

                    mCurrentLookupKey = contact.getKey();
                    mCurrentId = Long.parseLong(contact.getId());

                    mSelectedContactUri = ContactsContract.Contacts.getLookupUri(mCurrentId,mCurrentLookupKey);

                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    intent.setDataAndType(mSelectedContactUri, ContactsContract.Contacts.CONTENT_ITEM_TYPE);
                    intent.putExtra("finishActivityOnSaveCompleted",true);
                    mCtx.startActivity(intent);

                    return true;
                }
            });
        }
    }

    class DeleteContact extends AsyncTask<Contact,Void,Void>{

        @Override
        protected Void doInBackground(Contact... contacts) {

            Contact c = contacts[0];
            List<ajflims.quickcontact.RoomDB.Contact> contactList ;
            contactList = HomeActivity.myDatabase.contactDao().getContact();
            int id = 0;

            for(ajflims.quickcontact.RoomDB.Contact con : contactList){
                if(con.getName().equals(c.getName()) && con.getNumber().equals(c.getNumber())){
                    id = con.getId();
                    break;
                }
            }
            for(ajflims.quickcontact.RoomDB.Contact con : favList){
                if(con.getName().equals(c.getName()) && con.getNumber().equals(c.getNumber())){
                    favList.remove(con);
                    break;
                }

            }
            ajflims.quickcontact.RoomDB.Contact contact = new ajflims.quickcontact.RoomDB.Contact();
            contact.setId(id);
            HomeActivity.myDatabase.contactDao().deleteContact(contact);

            return null;
        }
    }

    class AddContact extends AsyncTask<Contact,Void,Void>{

        @Override
        protected Void doInBackground(Contact... contacts) {
            Contact c = contacts[0];
            String name = c.getName();
            String number = c.getNumber();
            ajflims.quickcontact.RoomDB.Contact contact = new ajflims.quickcontact.RoomDB.Contact();
            contact.setName(name);
            contact.setNumber(number);

            favList.add(contact);

            HomeActivity.myDatabase.contactDao().addContact(contact);
            return null;
        }
    }
}
