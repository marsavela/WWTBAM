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

    public static AlertDialogFragmentTwoChoices newInstance(OnClickAlertDialogFragmentTwoChoices onClickInterface,
                                                            String title, String message, String negative, String positive) {
        onClick = onClickInterface;
        AlertDialogFragmentTwoChoices frag = new AlertDialogFragmentTwoChoices();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        args.putString("negative", negative);
        args.putString("positive", positive);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        String negative = getArguments().getString("negative");
        String positive = getArguments().getString("positive");

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.btn_check_on)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positive,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                onClick.doPositiveClick();
                            }
                        })
                .setNegativeButton(negative,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                onClick.doNegativeClick();
                            }
                        }).create();
    }
}