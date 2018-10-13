package ajflims.quickcontact.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.mName.setText(c.getName());

        holder.mNumber.setText("");
        holder.mNumber.append(c.getNumber());
        holder.mNumber.append(" , ");
        holder.mNumber.append(c.getCalltype());

        Date date = new Date(Long.valueOf(c.getCalldate()));
        String dateTime = String.valueOf(date.getTime());

        GetTimeAlgo getTimeAlgo = new GetTimeAlgo();
        String time = getTimeAlgo.getTimeAgo(Long.parseLong(dateTime),mCtx);

        holder.mInfo.setText("");
        holder.mInfo.append(time);

        String duration = callduration(c.getCallduration());

        holder.mInfo.append(" , ");
        holder.mInfo.append(duration);
    }

    private String callduration(String callduration) {
        String duration = "";
        int minutes = 0;
        int sec = 0;
        int call = Integer.parseInt(callduration);
        if(call >=60){
            while(call>=60){
                call -= 60;
                minutes++;
            }
            sec = call;
        }else{
            sec = call;
        }
        if(minutes != 0){
            duration = String.valueOf(minutes) + "m" + String.valueOf(sec) + "s";
        }else{
            duration = String.valueOf(sec);
            duration = duration + "s";
        }
        return duration;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class CalllogAdapterViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mProfile, mCall;
        TextView mName, mNumber, mInfo;

        public CalllogAdapterViewHolder(View itemView) {
            super(itemView);

            mProfile = itemView.findViewById(R.id.calllog_profile);
            mCall = itemView.findViewById(R.id.calllog_call);
            mName = itemView.findViewById(R.id.calllog_name);
            mNumber = itemView.findViewById(R.id.calllog_number);
            mInfo = itemView.findViewById(R.id.calllog_info);

            mCall.setOnClickListener(new View.OnClickListener() {
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
