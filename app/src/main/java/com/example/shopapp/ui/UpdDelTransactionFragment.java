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
import com.example.shopapp.db.Transaction;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class UpdDelTransactionFragment extends Fragment {

    private EditText transactionQuantity;
    private Button updateTransaction, deleteTransaction;
    private int id, quantityEdited, quantityBeforeEdit, productId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Φόρτωση της διεπαφής του fragment
        View view = inflater.inflate(R.layout.fragment_upd_del_transaction, container, false);
        //Φόρτωση στοιχείων της διεπαφής και ορισμός ακροατών στα buttons
        transactionQuantity = view.findViewById(R.id.transactionQuantity);
        updateTransaction = view.findViewById(R.id.updateTransaction);
        updateTransaction.setOnClickListener(updateTransactionClickListener);
        deleteTransaction = view.findViewById(R.id.deleteTransaction);
        deleteTransaction.setOnClickListener(deleteTransactionClickListener);
        /*Δημιουργία bundle και μέσω αυτού λήψη των παραμέτρων
        που περάστηκαν από το ProductRecyclerAdapter*/
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        quantityBeforeEdit = bundle.getInt("quantity");
        productId = bundle.getInt("productId");
        //Φόρτωση των παραμέτρων που λήφθηκαν μέσω του bundle στη διεπαφή
        quantityEdited = quantityBeforeEdit;
        transactionQuantity.setText(String.valueOf(quantityEdited));

        return view;
    }

    //Ακροατής για το delete button
    private View.OnClickListener deleteTransactionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Transaction transaction = new Transaction();
            transaction.setId(id);
            //Επαναφορά της ποσότητας που είχε αφαιρεθεί
            MainActivity.shoppingAppDb.shopAppDao().updateStock(quantityEdited, productId);
            /*Διαγραφή της συναλλαγής με το αντίστοιχο id μέσω ερωτήματος στη ΒΔ
            και εμφάνιση κατάλληλου μηνύματος*/
            MainActivity.shoppingAppDb.shopAppDao().deleteTransaction(transaction);
            Toast.makeText(getActivity(), R.string.toast_del_transaction, Toast.LENGTH_LONG).show();

            //Επιστροφή στο προηγούμενο fragment (CartFragment)
            MainActivity.fragmentManager.popBackStackImmediate();
        }
    };

    //Ακροατής για το update button
    private View.OnClickListener updateTransactionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Λήψη του καθαρού κειμένου του πεδίου
            String quantity = transactionQuantity.getText().toString();
            //Έλεγχος για κενό πεδίο και εμφάνιση κατάλληλου μηνύματος
            if(TextUtils.isEmpty(quantity)) {
                Toast.makeText(getActivity(), R.string.toast_fill, Toast.LENGTH_LONG).show();
                return;
            }
            //Λήψη του αποθέματος του προϊόντος
            int stock = MainActivity.shoppingAppDb.shopAppDao().getProduct(id).getStock();
            //Έλεγχος για υπέρβαση του αποθέματος και εμφάνιση κατάλληλου μηνύματος
            if(stock-Integer.parseInt(quantity)<0) {
                Toast.makeText(view.getContext(), R.string.toast_stock, Toast.LENGTH_LONG).show();
                return;
            }
            //Λήψη της διαφοράς της ποσότητας πριν και μετά την επεξεργασία
            int quantityDiff = quantityBeforeEdit-Integer.parseInt(quantity);
            //Ενημέρωση του stock του προϊόντος
            MainActivity.shoppingAppDb.shopAppDao().updateStock(quantityDiff, id);

            Transaction transaction = new Transaction();
            transaction.setId(id);
            transaction.setProductId(productId);
            transaction.setProductQuantity(Integer.parseInt(quantity));
            transaction.setClientId(1);
            transaction.setCartNumber(1);
            /*Update των παραμέτρων της συναλλαγής με το αντίστοιχο id μέσω ερωτήματος στη ΒΔ
            και εμφάνιση κατάλληλου μηνύματος*/
            MainActivity.shoppingAppDb.shopAppDao().updateTransaction(transaction);
            Toast.makeText(getActivity(), R.string.toast_upd_transaction, Toast.LENGTH_LONG).show();

            //Επιστροφή στο προηγούμενο fragment (CartFragment)
            MainActivity.fragmentManager.popBackStackImmediate();
        }
    };

}
