package com.example.campus360.ui.camera;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.campus360.R;

public class FilenameDialogFragment extends DialogFragment {

        public interface FilenameDialogListener {
            void onDialogPositiveClick(DialogFragment dialog, String herro);
        }

        FilenameDialogListener listener;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            // Verify that the host activity implements the callback interface
            try {
                // Instantiate the NoticeDialogListener so we can send events to the host
                listener = (FilenameDialogListener) context;
            } catch (ClassCastException e) {
                // The activity doesn't implement the interface, throw exception
                throw new ClassCastException(" must implement FilenameDialogListener");
            }
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getActivity().setContentView(R.layout.dialog_filename);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();

            builder.setView(inflater.inflate(R.layout.dialog_filename, null))
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            EditText editText = (EditText) getActivity().findViewById(R.id.filename);

                            if (editText.getText() != null) {
                                String filename = editText.getText().toString();
                                // Send the positive button event back to the host activity
                                listener.onDialogPositiveClick(FilenameDialogFragment.this, filename);
                            }
                        }
                    });
            return builder.create();
        }
}
