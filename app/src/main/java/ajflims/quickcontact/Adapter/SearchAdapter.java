package ajflims.quickcontact.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ajflims.quickcontact.HomeActivity;
import ajflims.quickcontact.Model.Contact;
import ajflims.quickcontact.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ASR on 21-10-2018.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    final String TAG = "Ajit";
    private List<Contact> mList;
    private List<ajflims.quickcontact.RoomDB.Contact> favList;
    private Context mCtx;

    public SearchAdapter(List<Contact> mList, Context mCtx, List<ajflims.quickcontact.RoomDB.Contact> favList) {
        this.mList = mList;
        this.mCtx = mCtx;
        this.favList = favList;
    }

    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mCtx).inflate(R.layout.reyclerview_contactlist, parent, false);
        return new SearchAdapter.SearchViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(SearchAdapter.SearchViewHolder holder, int position) {

        Contact c = mList.get(position);
        holder.mName.setText(c.getName());
        holder.mNumber.setText(c.getNumber());
        holder.mNumber.setVisibility(View.VISIBLE);
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

    class SearchViewHolder extends RecyclerView.ViewHolder {

        CheckBox mFavorite;
        CircleImageView mProfile;
        TextView mName,mNumber;

        public SearchViewHolder(View itemView) {
            super(itemView);

            mFavorite = itemView.findViewById(R.id.contact_favourite);
            mProfile = itemView.findViewById(R.id.contact_profile);
            mName = itemView.findViewById(R.id.contact_name);
            mNumber = itemView.findViewById(R.id.contact_number);

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
                        new SearchAdapter.AddContact().execute(c);
                    }else{
                        new SearchAdapter.DeleteContact().execute(c);
                    }
                }
            });
        }
    }

    public void setFilter(ArrayList<Contact> newList){
        mList = new ArrayList<>();
        mList.addAll(newList);
        notifyDataSetChanged();
    }


    class DeleteContact extends AsyncTask<Contact,Void,Void> {

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

            HomeActivity.myDatabase.contactDao().addContact(contact);
            return null;
        }
    }
}
