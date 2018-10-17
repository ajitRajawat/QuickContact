package ajflims.quickcontact.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

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
        View v = LayoutInflater.from(mCtx).inflate(R.layout.reyclerview_contactlist,parent,false);
        return new ContactViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {

        Contact c = mList.get(position);
        holder.mName.setText(c.getName());
        boolean b = false;
        for(ajflims.quickcontact.RoomDB.Contact contact : favList){
            if(Objects.equals(contact.getNumber(), c.getNumber())){
                b = true;
                break;
            }
        }
        if(b){
           holder.mFavorite.setChecked(true);
        }else{
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

        }
    }
}
