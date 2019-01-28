//package com.app.kaioc.simpletodo.dialogFragments;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.widget.Toast;
//
//import com.app.kaioc.simpletodo.dialogFragments.CustomDialogFragment.CustomDialogListener;
//
//public class MyParentFragment extends Fragment implements CustomDialogListener {
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        showEditDialog();
//    }
//
//    // Call this method to launch the edit dialog
//    private void showEditDialog() {
//        FragmentManager fm = getFragmentManager();
//        CustomDialogFragment editNameDialogFragment = CustomDialogFragment.newInstance("Parent Title");
//        // SETS the target fragment for use later when sending results
//        editNameDialogFragment.setTargetFragment(MyParentFragment.this, 300);
//        editNameDialogFragment.show(fm, "parent_fragment");
//    }
//
//    // This is called when the dialog is completed and the results have been passed
//    @Override
//    public void onFinishEditDialog(String inputText) {
//        Toast.makeText(getContext(), "Hello, " + inputText, Toast.LENGTH_SHORT).show();
//    }
//}

