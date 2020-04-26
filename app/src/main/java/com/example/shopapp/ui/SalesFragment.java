package com.example.shopapp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopapp.MainActivity;
import com.example.shopapp.R;
import com.example.shopapp.db.Product;
import com.example.shopapp.db.Transaction;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class SalesFragment extends Fragment {
    private TextView totalSales, productSales, productStock;
    private AutoCompleteTextView autoCompleteSearch;
    private Button searchButton;
    private List<Transaction> totalTransactions, productTransactions;
    private double total=0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Φόρτωση της διεπαφής του fragment
        View view = inflater.inflate(R.layout.fragment_sales, container, false);
        //Φόρτωση στοιχείων της διεπαφής
        totalSales = view.findViewById(R.id.totalSales);
        autoCompleteSearch = view.findViewById(R.id.autocompleteSearch);
        searchButton = view.findViewById(R.id.searchProductButton);
        productSales = view.findViewById(R.id.productSales);
        productStock = view.findViewById(R.id.productStock);

        //Λήψη της λίστας με τις συναλλαγές μέσω ερωτήματος στη ΒΔ
        totalTransactions = MainActivity.shoppingAppDb.shopAppDao().getTransactions();
        /*Υπολογισμός των συνολικών πωλήσεων σε € μέσω ερωτήματος στη ΒΔ
        για την απόκτηση της τιμής κάθε προϊόντος*/
        for(Transaction transaction: totalTransactions) {
            Product product = MainActivity.shoppingAppDb.shopAppDao().getProduct(transaction.getProductId());
            total += transaction.getProductQuantity()*product.getPrice();
        }
        //Φόρτωση του αποτελέσματος στη διεπαφή
        totalSales.setText("Total sales: " + total + " €");

        //Λήψη της λίστας με τα ονόμα των προϊόντων μέσω ερωτήματος στη ΒΔ
        List<String> productsNames = MainActivity.shoppingAppDb.shopAppDao().getProductsNames();
        //Πέρασμα της λίστας στον adaptor του autocomplete text view και φόρτωση του adaptor
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, productsNames);
        autoCompleteSearch.setAdapter(adapter);

        //Ορισμός ακροατή για το search button της διεπαφής
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double totalProduct=0;
                //Λήψη του ονόματος από το πεδίο συμπλήρωσης
                String productName = autoCompleteSearch.getText().toString().trim();
                //Έλεγχος για κενό πεδίο και εμφάνιση κατάλληλου μηνύματος
                if(TextUtils.isEmpty(productName)) {
                    Toast.makeText(getActivity(), R.string.toast_fill, Toast.LENGTH_LONG).show();
                    return;
                }
                //Λήψη του id του προϊόντος με αυτό το όνομα
                int productId = MainActivity.shoppingAppDb.shopAppDao().getProductId(productName);
                //Αν υπάρχει προϊόν με αυτό το όνομα
                if(productId>0) {
                    //Λήψη του προϊόντος με βάση το παραπάνω id
                    Product product = MainActivity.shoppingAppDb.shopAppDao().getProduct(productId);
                    //Λήψη λίστας με τις συναλλαγές που έχουν γίνει για το παραπάνω προϊόν
                    productTransactions = MainActivity.shoppingAppDb.shopAppDao().getProductTransactions(productId);
                    //Υπολογισμός των συνολικών πωλήσεων για το προϊόν
                    for(Transaction transaction: productTransactions) {
                        totalProduct += transaction.getProductQuantity()*product.getPrice();
                    }
                    //Φόρτωση του αποτελέσματος στη διεπαφή
                    productSales.setText(totalProduct+" €");
                    //Φόρτωση του αποθέματος του προϊόντος στη διεπαφή
                    productStock.setText(product.getStock()+" left");
                } else { //Αν δεν υπάρχει προϊόν με αυτό το όνομα, εμφάνιση κατάλληλου μηνύματος και άδειασμα πεδίων
                    Toast.makeText(getActivity(), R.string.toast_product, Toast.LENGTH_LONG).show();
                    productSales.setText("");
                    productStock.setText("");
                }
            }
        });

        return view;
    }
}
