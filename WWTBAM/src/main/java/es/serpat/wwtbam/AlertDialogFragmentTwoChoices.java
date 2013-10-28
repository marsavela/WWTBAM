package es.serpat.wwtbam;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by sergiu on 27/10/13.
 */

public class AlertDialogFragmentTwoChoices extends DialogFragment {

    private static OnClickAlertDialogFragmentTwoChoices onClick;

    public static AlertDialogFragmentTwoChoices newInstance( OnClickAlertDialogFragmentTwoChoices onClickInterface,
                                                     String title, String message) {
        onClick = onClickInterface;
        AlertDialogFragmentTwoChoices frag = new AlertDialogFragmentTwoChoices();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.btn_check_on)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.next),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                onClick.doPositiveClick();
                            }
                        })
                .setNegativeButton(getString(R.string.give_up),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                onClick.doNegativeClick();
                            }
                        }).create();
    }
}