package com.gigabytes.freebee.homescreen.views.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigabytes.freebee.R;
import com.gigabytes.freebee.homescreen.views.adapters.LocalContactsRecyclerViewAdapter;
import com.gigabytes.freebee.homescreen.views.model.LocalContactsModel;
import com.opentok.android.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class CallFragment extends Fragment {

    private static final int RC_APP_CONTACT_PERM = 100;

    private FloatingActionButton dialerFloatingActionButton;
    private RecyclerView contactsRecyclerView;
    private View view;

    public static CallFragment createInstance() {
        return new CallFragment();
    }

    private void getContactList() {
        contactsRecyclerView = view.findViewById(R.id.contactsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        contactsRecyclerView.setLayoutManager(layoutManager);

        List<LocalContactsModel> localContactsModelList = new ArrayList<>();

        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        localContactsModelList.add(new LocalContactsModel(name, phoneNo));
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }

        Collections.sort(localContactsModelList, LocalContactsModel.ALPHABETICAL_ORDER);
        contactsRecyclerView.setAdapter(new LocalContactsRecyclerViewAdapter(localContactsModelList, getContext()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_APP_CONTACT_PERM)
    private void requestPermissions() {
        String[] perms = { Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.CALL_PHONE};
        if (EasyPermissions.hasPermissions(this.getActivity(), perms)) {
            getContactList();
        }
        else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_APP_CONTACT_PERM, perms);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.call_fragment, container, false);
        requestPermissions();

        return view;
    }
}
