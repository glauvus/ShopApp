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
import com.example.shopapp.db.Transaction;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class SubmitCartFragment extends Fragment {

    EditText firstName, lastName, address;
    Button submitCart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Φόρτωση της διεπαφής του fragment
        View view = inflater.inflate(R.layout.fragment_submit_cart, container, false);
        //Φόρτωση στοιχείων της διεπαφής
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        address = view.findViewById(R.id.address);
        submitCart = view.findViewById(R.id.submitCart);

        //Λήψη της λίστας με τις συναλλαγές μέσω ερωτήματος στη ΒΔ
        List<Transaction> transactions = MainActivity.shoppingAppDb.shopAppDao().getTransactions();

        //Ορισμός ακροατή για το submit button της διεπαφής
        submitCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Λήψη του καθαρού κειμένου των πεδίων χωρίς περιττά κενά
                String fName = firstName.getText().toString().trim();
                String lName = lastName.getText().toString().trim();
                String addr = address.getText().toString().trim();

                //Έλεγχος για κενά πεδία και εμφάνιση κατάλληλου μηνύματος
                if(TextUtils.isEmpty(fName) || TextUtils.isEmpty(lName) || TextUtils.isEmpty(addr)) {
                    Toast.makeText(getActivity(), R.string.toast_fill, Toast.LENGTH_LONG).show();
                    return;
                }

                int clientId;
                //Ορισμός του cartNumber ως +1 από το τελευταίο, δηλαδή το cartNumber αποτελεί Α/Α
                int cartNumber = MainActivity.shoppingAppDb.shopAppDao().getLastCartNumber()+1;
                //Λήψη της λίστας με τις συναλλαγές που υπάρχουν στο καρότσι
                List<Transaction> cart = MainActivity.shoppingAppDb.shopAppDao().getCart();

                /*Λήψη της λίστας των πελατών με όνομα, επίθετο και διεύθυνση ίδια
                με αυτά που καταχωρήθηκαν στη φόρμα της διεπαφής*/
                List<Client> clients = MainActivity.shoppingAppDb.shopAppDao().getClients(fName, lName, addr);

                /*Αν υπάρχει πελάτης με τα ίδια στοιχεία, update των πεδίων clientId και cartNumber
                με αυτά του πελάτη και του καροτσιού του αντίστοιχα.
                Αλλιώς, καταχώρηση νέου πελάτη στη ΒΔ και update των πεδίων clientId και cartNumber
                με αυτά του πελάτη και του καροτσιού του αντίστοιχα.*/
                if(clients.size()>0) {
                    clientId = clients.get(0).getId();
                    MainActivity.shoppingAppDb.shopAppDao().updateTransaction(clientId, cartNumber);
                } else {
                    Client client = new Client();
                    client.setFirstName(fName);
                    client.setLastName(lName);
                    client.setAddress(addr);
                    MainActivity.shoppingAppDb.shopAppDao().addClient(client);

                    clientId = MainActivity.shoppingAppDb.shopAppDao().getCurrentClientId();
                    MainActivity.shoppingAppDb.shopAppDao().updateTransaction(clientId, cartNumber);
                }
                Toast.makeText(getActivity(), R.string.toast_cart, Toast.LENGTH_LONG).show();
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new ShopFragment()).addToBackStack(null).commit();
            }
        });

        return view;
    }

}
