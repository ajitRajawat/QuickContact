package ajflims.quickcontact.Fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ajflims.quickcontact.Activities.AddNewActivity;
import ajflims.quickcontact.Adapter.FavouriteAdapter;
import ajflims.quickcontact.HomeActivity;
import ajflims.quickcontact.R;
import ajflims.quickcontact.RoomDB.Contact;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends Fragment {

    private FloatingActionButton mAddNew;

    private RecyclerView recyclerView;
    private FavouriteAdapter adapter;
    private List<Contact> mList;

    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        mAddNew = view.findViewById(R.id.home_Add);
        recyclerView = view.findViewById(R.id.favourite_recyclerview);

        mAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),AddNewActivity.class));
            }
        });

        mList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        adapter = new FavouriteAdapter(mList,getActivity());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        mList.clear();
    }

    @Override
    public void onStart() {
        super.onStart();
        mList.clear();
        new fetchUser().execute();
    }

    class fetchUser extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            List<Contact> list = HomeActivity.myDatabase.contactDao().getContact();
            for(Contact c : list) {
                mList.add(c);
            }

            return null;
        }
    }

    class  deleteAllUser extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
            mList.clear();
            adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            HomeActivity.myDatabase.contactDao().deleteAll(mList);

            return null;
        }
    }
}
