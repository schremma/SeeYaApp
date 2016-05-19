package com.group16.seeyaapp.activity.details;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.group16.seeyaapp.PresenterManager;
import com.group16.seeyaapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrea on 16/05/16.
 */
public class AddInvitedFragment extends DialogFragment implements AddInvitedView {
    private AddInvitedPresenterImpl presenter;
    private ArrayList<String> lstInvited;
    private ArrayList<String> lstInvitedOnDialogStart;

    private EditText txtInvitedUser;
    private Button btnAddInvitee;
    private Button btnRemoveSelected;
    private ListView lvUsers;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            presenter = new AddInvitedPresenterImpl();
        } else {
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        ArrayList<String> lstFromActivity = getArguments().getStringArrayList("invitedList");
        lstFromActivity = getArguments().getStringArrayList("invitedList");
        lstInvited = new ArrayList<>();
        lstInvitedOnDialogStart = new ArrayList<>();
        for(String i : lstFromActivity) {
            lstInvited.add(i);
            lstInvitedOnDialogStart.add(i);
        }
    }

    public static AddInvitedFragment newInstance(ArrayList<String> inivitedList) {
        AddInvitedFragment f = new AddInvitedFragment();

        Bundle args = new Bundle();
        args.putStringArrayList("invitedList", inivitedList);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.bindView(this);

    }

    @Override
    public void onPause() {
        super.onPause();

        presenter.unbindView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        presenter.setInvitedList(lstInvited);
        PresenterManager.getInstance().savePresenter(presenter, outState);
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_invited_fragment_dialog, null);


        btnAddInvitee = (Button)view.findViewById(R.id.btnAddInvitee);
        txtInvitedUser = (EditText)view.findViewById(R.id.txtInvitedUserName);
        lvUsers = (ListView)view.findViewById(R.id.lvUsers);
        btnRemoveSelected = (Button)view.findViewById(R.id.btnRemoveSelected);
        btnRemoveSelected.setEnabled(false);


        btnAddInvitee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usr = txtInvitedUser.getText().toString();
                presenter.checkIfUserExists(usr);
            }
        });

        btnRemoveSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = lvUsers.getCheckedItemPosition();
                if (position >= 0) {
                    String selected = lvUsers.getItemAtPosition(position).toString();
                    removeInvitedUser(selected);
                }
            }
        });

        lvUsers.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        updateInvitedListDisplay();


        lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!btnRemoveSelected.isEnabled())
                    btnRemoveSelected.setEnabled(true);
            }
        });


        builder.setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((AddInvitedListener) getActivity()).setListOfInvitedUsers(lstInvited);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((AddInvitedListener) getActivity()).setListOfInvitedUsers(lstInvitedOnDialogStart);
                    }
                });

        return builder.create();
    }

    private void removeInvitedUser(String selected) {
        int index = lstInvited.indexOf(selected);
        if (index >= 0) {
            lstInvited.remove(index);
        }
        updateInvitedListDisplay();
    }

    @Override
    public void onUserExistenceChecked(boolean userExists, String username) {

        if (userExists) {
            Toast toast = Toast.makeText(getActivity(), "User confirmed", Toast.LENGTH_SHORT);
            toast.show();

            txtInvitedUser.setText("");
            addInvited(username);
        }
        else {
            showOnError("User does not exist");
        }

    }

    @Override
    public void setInvitedUserList(List<String> invitedUserList) {

        lstInvited = new ArrayList<>();
        for(String i : invitedUserList)
            lstInvited.add(i);
        updateInvitedListDisplay();
    }


    @Override
    public void showOnError(String errorMessage) {
        Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void addInvited(String username) {
        if (lstInvited == null)
            lstInvited = new ArrayList<>();
        lstInvited.add(username);

        updateInvitedListDisplay();
    }

    private void updateInvitedListDisplay() {
        arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_users_item,
                lstInvited);

        lvUsers.setAdapter(arrayAdapter);
    }
}
