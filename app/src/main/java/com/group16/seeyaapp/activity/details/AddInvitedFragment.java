package com.group16.seeyaapp.activity.details;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    private List<String> lstInvited;

    private TextView tvInvitedList;
    private EditText txtInvitedUser;
    private Button btnAddInvitee;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            presenter = new AddInvitedPresenterImpl();
        } else {
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }


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

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_invited_fragment_dialog, null);


        btnAddInvitee = (Button)view.findViewById(R.id.btnAddInvitee);
        txtInvitedUser = (EditText)view.findViewById(R.id.txtInvitedUserName);
        tvInvitedList = (TextView)view.findViewById(R.id.tvListOfInvited);


        btnAddInvitee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usr = txtInvitedUser.getText().toString();
                presenter.checkIfUserExists(usr);
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
                        // do something
                    }
                });

        return builder.create();
    }

    @Override
    public void onUserExistenceChecked(boolean userExists, String username) {

        if (userExists) {
            Toast toast = Toast.makeText(getActivity(), "User confirmed", Toast.LENGTH_SHORT);
            toast.show();

            txtInvitedUser.setText("");
            tvInvitedList.setText(tvInvitedList.getText() + username + "; " );
            addInvited(username);
        }
        else {
            showOnError("User does not exist");
        }

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
    }
}
