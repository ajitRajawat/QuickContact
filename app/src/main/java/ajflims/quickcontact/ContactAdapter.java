package ajflims.quickcontact;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import ajflims.quickcontact.Activities.EditContactActivity;
import ajflims.quickcontact.RoomDB.Contact;

/**
 * Created by ajit on 9/15/2018.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> list;
    private Context mCtx;

    public ContactAdapter(List<Contact> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.contact_layout, parent, false);
        return new ContactAdapter.ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {

        Contact contact = list.get(position);
        holder.mName.setText(contact.getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        Button mName;

        public ContactViewHolder(View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.home_name);

            mName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Contact c = list.get(getAdapterPosition());
                    String number = c.getNumber();
                    Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));

                    if (ActivityCompat.checkSelfPermission(mCtx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", mCtx.getPackageName(), null);
                        intent.setData(uri);
                        mCtx.startActivity(intent);
                    }else {
                        mCtx.startActivity(i);
                    }

                }
            });
            mName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Contact c = list.get(getAdapterPosition());
                    Intent intent = new Intent(mCtx, EditContactActivity.class);
                    intent.putExtra("name",c.getName());
                    intent.putExtra("number",c.getNumber());
                    intent.putExtra("id",c.getId());
                    mCtx.startActivity(intent);
                    return true;
                }
            });

        }
    }

}
