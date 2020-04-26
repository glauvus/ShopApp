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
import com.example.shopapp.db.Product;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class AddProductFragment extends Fragment {

    private EditText productName, productPrice, productQuantity;
    private Button submitProduct;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Φόρτωση της διεπαφής του fragment
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        //Φόρτωση στοιχείων της διεπαφής
        productName = view.findViewById(R.id.productName);
        productPrice = view.findViewById(R.id.productPrice);
        productQuantity = view.findViewById(R.id.productQuantity);
        submitProduct = view.findViewById(R.id.submitProduct);

        //Ορισμός ακροατή για το submit button της διεπαφής
        submitProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Λήψη του καθαρού κειμένου των πεδίων
                String name = productName.getText().toString().trim();
                String price = productPrice.getText().toString();
                String quantity = productQuantity.getText().toString();
                //Έλεγχος για κενά πεδία και εμφάνιση κατάλληλου μηνύματος
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || TextUtils.isEmpty(quantity)) {
                    Toast.makeText(getActivity(), R.string.toast_fill, Toast.LENGTH_LONG).show();
                    return;
                }
                //Δημιουργία νέου στιγμιοτύπου Product και πέρασμα τιμών
                Product product = new Product();
                product.setName(name);
                product.setPrice(Double.parseDouble(price));
                product.setStock(Integer.parseInt(quantity));

                //Εισαγωγή του νέου προϊόντος στη ΒΔ και εμφάνιση αντίστοιχου μηνύματος
                MainActivity.shoppingAppDb.shopAppDao().addProduct(product);
                Toast.makeText(getActivity(), R.string.toast_add_product, Toast.LENGTH_LONG).show();

                //Άδεισμα των πεδίων για εισαγωγή νέων τιμών
                productName.setText("");
                productPrice.setText("");
                productQuantity.setText("");
            }
        });

        return view;
    }

}
