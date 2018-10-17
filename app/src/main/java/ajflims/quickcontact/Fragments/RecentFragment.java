package ajflims.quickcontact.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ajflims.quickcontact.Adapter.CalllogAdapter;
import ajflims.quickcontact.Adapter.ContactAdapter;
import ajflims.quickcontact.Model.Calllog;
import ajflims.quickcontact.Model.Contact;
import ajflims.quickcontact.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecentFragment extends Fragment {

    private RecyclerView recyclerView;
    private CalllogAdapter adapter;
    private List<Calllog> mList;
    int calllog_count = 0;

    public RecentFragment() {
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
        recyclerView.setHasFixedSize(true);
        mList = new ArrayList<>();
        calllog_count = 0;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return;
        }else{
            new CallLogFetch().execute();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CalllogAdapter(mList, getActivity());
        recyclerView.setAdapter(adapter);

    }

    class CallLogFetch extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void...voids) {

             @SuppressLint("MissingPermission") Cursor cursor = getContext().getContentResolver().query(CallLog.Calls.CONTENT_URI,
                        null, null, null, CallLog.Calls.DATE + " DESC");

            int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
            int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = cursor.getColumnIndex(CallLog.Calls.DATE);
            while (cursor.moveToNext()) {
                String num = cursor.getString(number);
                String calltype = cursor.getString(type);
                String callduration = cursor.getString(duration);
                String calldate = cursor.getString(date);
                String callType = null;
                int dir = Integer.parseInt(calltype);
                switch (dir) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        callType = "OUTGOING";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        callType = "INCOMING";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callType = "MISSED";
                        break;
                    default:
                        callType = "CALLED";
                }
                Calllog calllog = new Calllog("QuickContact", num, calldate, callType, callduration);
                mList.add(calllog);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
        }
    }
}





