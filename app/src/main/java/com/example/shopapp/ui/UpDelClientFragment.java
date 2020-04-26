package com.example.shopapp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shopapp.MainActivity;
import com.example.shopapp.R;
import com.example.shopapp.db.Client;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class UpDelClientFragment extends Fragment {

    private EditText clientFirstName, clientLastName, clientAddress;
    private Button updateClient, deleteClient;
    private int id;
    private String firstName, lastName, address;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Φόρτωση της διεπαφής του fragment
        View view = inflater.inflate(R.layout.fragment_upd_del_client, container, false);
        //Φόρτωση στοιχείων της διεπαφής και ορισμός ακροατών στα buttons
        clientFirstName = view.findViewById(R.id.clientFirstName);
        clientLastName = view.findViewById(R.id.clientLastName);
        clientAddress = view.findViewById(R.id.clientAddress);
        updateClient = view.findViewById(R.id.updateClient);
        updateClient.setOnClickListener(updateClientClickListener);
        deleteClient = view.findViewById(R.id.deleteClient);
        deleteClient.setOnClickListener(deleteClientClickListener);
        /*Δημιουργία bundle και μέσω αυτού λήψη των παραμέτρων
        που περάστηκαν από το ProductRecyclerAdapter*/
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        firstName = bundle.getString("firstName");
        lastName = bundle.getString("lastName");
        address = bundle.getString("address");
        //Φόρτωση των παραμέτρων που λήφθηκαν μέσω του bundle στη διεπαφή
        clientFirstName.setText(firstName);
        clientLastName.setText(lastName);
        clientAddress.setText(address);

        return view;
    }

    //Ακροατής για το delete button
    private View.OnClickListener deleteClientClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Client client = new Client();
            client.setId(id);
            /*Διαγραφή της συναλλαγής με το αντίστοιχο id μέσω ερωτήματος στη ΒΔ
            και εμφάνιση κατάλληλου μηνύματος*/
            MainActivity.shoppingAppDb.shopAppDao().deleteClient(client);
            Toast.makeText(getActivity(), R.string.toast_del_client, Toast.LENGTH_LONG).show();

            //Επιστροφή στο προηγούμενο fragment (ClientsFragment)
            MainActivity.fragmentManager.popBackStackImmediate();
        }
    };

    //Ακροατής για το update button
    private View.OnClickListener updateClientClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Λήψη του καθαρού κειμένου των πεδίων χωρίς περιττά κενά
            String firstName = clientFirstName.getText().toString().trim();
            String lastName = clientLastName.getText().toString().trim();
            String address = clientAddress.getText().toString().trim();
            //Έλεγχος για κενά πεδία και εμφάνιση κατάλληλου μηνύματος
            if(TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(address)) {
                Toast.makeText(getActivity(), R.string.toast_fill, Toast.LENGTH_LONG).show();
                return;
            }
            Client client = new Client();
            client.setId(id);
            client.setFirstName(firstName);
            client.setLastName(lastName);
            client.setAddress(address);
            /*Update των παραμέτρων της συναλλαγής με το αντίστοιχο id μέσω ερωτήματος στη ΒΔ
            και εμφάνιση κατάλληλου μηνύματος*/
            MainActivity.shoppingAppDb.shopAppDao().updateClient(client);
            Toast.makeText(getActivity(), R.string.toast_upd_client, Toast.LENGTH_LONG).show();

            //Επιστροφή στο προηγούμενο fragment (ClientsFragment)
            MainActivity.fragmentManager.popBackStackImmediate();
        }
    };
}
