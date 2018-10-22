package ajflims.quickcontact.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import ajflims.quickcontact.Model.Calllog;
import ajflims.quickcontact.Model.GetTimeAlgo;
import ajflims.quickcontact.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ajit on 10/13/2018.
 */

public class CalllogAdapter extends RecyclerView.Adapter<CalllogAdapter.CalllogAdapterViewHolder> {

    List<Calllog> mList;
    Context mCtx;

    public CalllogAdapter(List<Calllog> mList, Context mCtx) {
        this.mList = mList;
        this.mCtx = mCtx;
    }

    @Override
    public CalllogAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_calllogs, parent, false);
        return new CalllogAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CalllogAdapterViewHolder holder, int position) {

        Calllog c = mList.get(position);
        String name = c.getName();
        if(TextUtils.isEmpty(name)){
            holder.mName.setText(c.getNumber());
        }else {
            holder.mName.setText(name);
        }

        holder.mNumber.setText("");
        holder.mNumber.append(c.getNumber());

        String type = c.getCalltype();
        switch (type){
            case "MISSED" :
                holder.mCallType.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.missed_call));
                break;
            case "INCOMING" :
                holder.mCallType.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.incoming_call));
                break;
            case "OUTGOING" :
                holder.mCallType.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.outgoing_call));
                break;
            default:
                holder.mCallType.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.missed_call));
        }

        String time = c.getCalldate();
        holder.mInfo.setText("");
        holder.mInfo.append(time);

        String duration = c.getCallduration();

        holder.mInfo.append(" , ");
        holder.mInfo.append(duration);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class CalllogAdapterViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mProfile;
        ImageView mCallType;
        TextView mName, mNumber, mInfo;

        public CalllogAdapterViewHolder(View itemView) {
            super(itemView);

            mProfile = itemView.findViewById(R.id.calllog_profile);
            mName = itemView.findViewById(R.id.calllog_name);
            mNumber = itemView.findViewById(R.id.calllog_number);
            mInfo = itemView.findViewById(R.id.calllog_info);
            mCallType = itemView.findViewById(R.id.calllog_calltype);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calllog c = mList.get(getAdapterPosition());
                    Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + c.getNumber()));
                    if (ActivityCompat.checkSelfPermission(mCtx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mCtx.startActivity(i);
                }
            });
        }
    }

}
