package ajflims.quickcontact.Fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ajflims.quickcontact.Adapter.CalllogAdapter;
import ajflims.quickcontact.Model.CallLog;
import ajflims.quickcontact.Model.GetTimeAlgo;
import ajflims.quickcontact.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalllogFragment extends Fragment {

    private RecyclerView recyclerView;
    private CalllogAdapter adapter;
    private List<CallLog> mList;
    int calllog_count = 0;
    private ProgressBar mProgress;

    public CalllogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recent, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.calllog_recyclerview);
        mProgress = view.findViewById(R.id.calllog_progress);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        mList = new ArrayList<>();
        calllog_count = 0;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            new CallLogFetch().execute();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CalllogAdapter(mList, getActivity());
        recyclerView.setAdapter(adapter);

    }

    class CallLogFetch extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Cursor cursor = getContext().getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI,
                    null, null, null, android.provider.CallLog.Calls.DATE + " DESC");

            int number = cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER);
            int duration = cursor.getColumnIndex(android.provider.CallLog.Calls.DURATION);
            int type = cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE);
            int date = cursor.getColumnIndex(android.provider.CallLog.Calls.DATE);
            int name = cursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME);
            String n = "";
            while (cursor.moveToNext()) {
                String num = cursor.getString(number);
                if(num.equals(n)){
                    continue;
                }
                n=num;
                String userName = cursor.getString(name);
                String calltype = cursor.getString(type);
                String callduration = cursor.getString(duration);
                String calldate = cursor.getString(date);
                String callType = null;
                int dir = Integer.parseInt(calltype);
                switch (dir) {
                    case android.provider.CallLog.Calls.OUTGOING_TYPE:
                        callType = "OUTGOING";
                        break;
                    case android.provider.CallLog.Calls.INCOMING_TYPE:
                        callType = "INCOMING";
                        break;
                    case android.provider.CallLog.Calls.MISSED_TYPE:
                        callType = "MISSED";
                        break;
                    default:
                        callType = "CALLED";
                }

                Date d = new Date(Long.parseLong(calldate));
                String dateTime = String.valueOf(d.getTime());

                GetTimeAlgo getTimeAlgo = new GetTimeAlgo();
                String time = getTimeAlgo.getTimeAgo(Long.parseLong(dateTime),getContext());

                String dur = callduration(String.valueOf(callduration));

                CallLog calllog = new CallLog(userName, num, time, callType, dur);
                mList.add(calllog);
            }
            cursor.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
            mProgress.setVisibility(View.INVISIBLE);
        }
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
}





