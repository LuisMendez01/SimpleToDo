package com.app.kaioc.simpletodo.dialogFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.app.kaioc.simpletodo.R;

// ...
//The implements onEditorActionListener is for the onEditorAction method only
public class CustomDialogFragment extends DialogFragment implements OnEditorActionListener {

    //track edit text
    private EditText mEditText;
    //position of edited item in list
    int position;

    public CustomDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    // 1. Defines the listener interface with a method passing back data result.
    public interface CustomDialogListener {
        void onFinishEditDialog(String inputText, int pos);
    }


    public static CustomDialogFragment newInstance(String title) {
        CustomDialogFragment frag = new CustomDialogFragment();//changed to class name

        //put title when obj created into the args and return it in MainActivity
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    //to remove title bar
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        // request a window without the title
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        return dialog;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //There is a bug so sending this seem to fix it, keep using it like this
        //return getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment, container);

        //this is the normal way to use it but it's buggy use above
        View rootView = inflater.inflate(R.layout.dialog_fragment, container, false);//change to layout name dialog_fragment;

        View onSaveBtn = rootView.findViewById(R.id.btnSave);
        onSaveBtn.setOnClickListener(new View.OnClickListener() {

            //handler for save button, button is going to call this function on click button
            @Override
            public void onClick(View v) {
                // Return input text back to activity through the implemented listener
                CustomDialogListener listener = (CustomDialogListener) getActivity();
                listener.onFinishEditDialog(mEditText.getText().toString(), position);
                // Close the dialog and return back to the parent activity
                dismiss();
            }

        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.etItemText);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //getting the item and position of item being edited from MainActivity
        mEditText.setText(getArguments().getString("item"));
        position = getArguments().getInt("pos");

        // 2. Setup a callback when the "Done" button is pressed on keyboard
        mEditText.setOnEditorActionListener(this);
    }

    // Fires whenever the textfield has an action performed
    // In this case, when the "Done" button is pressed
    // REQUIRES a 'soft keyboard' (virtual keyboard)
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text back to activity through the implemented listener
            CustomDialogListener listener = (CustomDialogListener) getActivity();
            listener.onFinishEditDialog(mEditText.getText().toString(), 0);
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }

}