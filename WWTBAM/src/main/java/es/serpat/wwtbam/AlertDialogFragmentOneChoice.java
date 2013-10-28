package es.serpat.wwtbam;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by SergiuDaniel on 27/10/13.
 */
public class AlertDialogFragmentOneChoice extends DialogFragment {

    public static AlertDialogFragmentOneChoice newInstance(String title, String message) {
        AlertDialogFragmentOneChoice frag = new AlertDialogFragmentOneChoice();
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

        if (message.equals("wrong"))
            return  new AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_delete)
                    .setTitle(getString(R.string.you_lost))
                    .setMessage(getString(R.string.try_again))
                    .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }

                    }).create();
        else if (message.equals(getString(R.string.audience_message)))
            return  new AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_menu_call)
                    .setTitle(getString(R.string.audience))
                    .setMessage(getString(R.string.audience_message) + " " + title + "\"")
                    .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }

                    }).create();
        else if (message.equals(getString(R.string.phone_message)))
            return new AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_menu_call)
                    .setTitle(getString(R.string.phone))
                    .setMessage(getString(R.string.phone_message) + " " + title + "\"")
                    .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }

                    }).create();
        else return new AlertDialog.Builder(getActivity()).setIcon(android.R.drawable.ic_menu_help)
                    .setTitle(title).setMessage(message)
                    .setNeutralButton(getString(R.string.understood), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }

                    }).create();
    }
}