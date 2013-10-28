package es.serpat.wwtbam;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 * Created by SergiuDaniel on 27/10/13.
 */
public class AlertDialogFragmentList extends DialogFragment {

    private static OnClickAlertDialogFragmentList onClick;

    public static AlertDialogFragmentList newInstance(OnClickAlertDialogFragmentList onClickInterface, String[] jokers) {
        onClick = onClickInterface;
        AlertDialogFragmentList fragment = new AlertDialogFragmentList();
        Bundle arguments = new Bundle();
        arguments.putStringArray("array_jokers", jokers);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        String[] jokers = getArguments().getStringArray("array_jokers");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pick_joker).setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, jokers), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                onClick.doAskForJoker(which);
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}