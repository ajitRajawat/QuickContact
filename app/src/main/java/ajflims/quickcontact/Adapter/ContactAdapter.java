package ajflims.quickcontact.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import ajflims.quickcontact.Model.Contact;
import ajflims.quickcontact.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ajit on 10/13/2018.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> mList;
    private Context mCtx;

    public ContactAdapter(List<Contact> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mCtx).inflate(R.layout.reyclerview_contactlist,parent,false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {

        Contact c = mList.get(position);
        holder.mName.setText(c.getName());
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
