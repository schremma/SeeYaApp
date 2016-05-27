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
 * Displays a view for selecting a number of user names, representing specific users that are to be
 * invited to an activity.
 * The list of already invited users, if there are any, are received from the invoking activity.
 * Newly added users are appended to that list. The original state of the list is also saved in case
 * the user cancels the changes made in the dialog.
 */
public class AddInvitedFragment extends DialogFragment implements AddInvitedView {
    private AddInvitedPresenterImpl presenter;
    private ArrayList<String> lstInvited; //the list to which newly added invited users are appended
    private ArrayList<String> lstInvitedOnDialogStart; // the list of already invited users as
                                                        // received from the invoking activity

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

        // The invoking activity sends a list that might already contain some names the user
        // have already added to the invitee list
        ArrayList<String> lstFromActivity = getArguments().getStringArrayList("invitedList");
        lstFromActivity = getArguments().getStringArrayList("invitedList");
        lstInvited = new ArrayList<>();
        lstInvitedOnDialogStart = new ArrayList<>();
        for(String i : lstFromActivity) {
            lstInvited.add(i);
            lstInvitedOnDialogStart.add(i);
        }
    }

    /**
     * Constructor method for the fragment.
     * Retrieves the array list as an argument that the invoking activity sent so that
     * it can be accessed in the onCreate() method.
     * @param inivitedList
     * @return
     */
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

        // save the current list of invited users in the presenter
        presenter.setInvitedList(lstInvited);
        PresenterManager.getInstance().savePresenter(presenter, outState);
    }


    /**
     * The GUI of the fragment is set here.
     * @param savedInstanceState
     * @return
     */
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
                        // send the updated list back to the invoking activity
                        ((AddInvitedListener) getActivity()).setListOfInvitedUsers(lstInvited);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // no changes should be saved: send the original list back to the invoking activity
                        ((AddInvitedListener) getActivity()).setListOfInvitedUsers(lstInvitedOnDialogStart);
                    }
                });

        return builder.create();
    }

    /**
     * Removes a user with a specific name from the list of invited, and updates the GUI.
     * @param selected
     */
    private void removeInvitedUser(String selected) {
        int index = lstInvited.indexOf(selected);
        if (index >= 0) {
            lstInvited.remove(index);
        }
        updateInvitedListDisplay();
    }

    /**
     * Response received from the Presenter that a user exists or not.
     * If the user exists, add it to the list of invited.
     * @param userExists True if the name input by the user denotes an existing user
     * @param username The user name that has been checked for existence
     */
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

    /**
     * Update the list of invited users with the provided list
     */
    @Override
    public void setInvitedUserList(List<String> invitedUserList) {

        lstInvited = new ArrayList<>();
        for(String i : invitedUserList)
            lstInvited.add(i);
        updateInvitedListDisplay();
    }


    /**
     * Displays error message as a Toast
     * @param errorMessage the error message to show
     */
    @Override
    public void showOnError(String errorMessage) {
        Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Adds the given string to the list of invited users and initiates
     * updating the GUI accordingly.
     * @param username
     */
    private void addInvited(String username) {
        if (lstInvited == null)
            lstInvited = new ArrayList<>();
        lstInvited.add(username);

        updateInvitedListDisplay();
    }

    /**
     * Updates the list of of invited users reflect the current state of the instance variable list.
     */
    private void updateInvitedListDisplay() {
        arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_users_item,
                lstInvited);

        lvUsers.setAdapter(arrayAdapter);
    }
}
