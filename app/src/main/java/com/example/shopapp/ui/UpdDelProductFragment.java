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

public class UpdDelProductFragment extends Fragment {

    private EditText productName, productPrice, productStock;
    private Button updateProduct, deleteProduct;
    private int id, stock;
    private double price;
    private String name;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Φόρτωση της διεπαφής του fragment
        View view = inflater.inflate(R.layout.fragment_upd_del_product, container, false);
        //Φόρτωση στοιχείων της διεπαφής και ορισμός ακροατών στα buttons
        productName = view.findViewById(R.id.productName);
        productPrice = view.findViewById(R.id.productPrice);
        productStock = view.findViewById(R.id.productQuantity);
        updateProduct = view.findViewById(R.id.updateProduct);
        updateProduct.setOnClickListener(updateProductClickListener);
        deleteProduct = view.findViewById(R.id.deleteProduct);
        deleteProduct.setOnClickListener(deleteProductClickListener);
        /*Δημιουργία bundle και μέσω αυτού λήψη των παραμέτρων
        που περάστηκαν από το ProductRecyclerAdapter*/
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        name = bundle.getString("name");
        price = bundle.getDouble("price");
        stock = bundle.getInt("stock");

        //Φόρτωση των παραμέτρων που λήφθηκαν μέσω του bundle στη διεπαφή
        productName.setText(name);
        productPrice.setText(String.valueOf(price));
        productStock.setText(String.valueOf(stock));

        return view;
    }

    //Ακροατής για το delete button
    private View.OnClickListener deleteProductClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Product product = new Product();
            product.setId(id);
            /*Διαγραφή του προϊόντος με το αντίστοιχο id μέσω ερωτήματος στη ΒΔ
            και εμφάνιση κατάλληλου μηνύματος*/
            MainActivity.shoppingAppDb.shopAppDao().deleteProduct(product);
            Toast.makeText(getActivity(), R.string.toast_del_product, Toast.LENGTH_LONG).show();

            //Επιστροφή στο προηγούμενο fragment (ProductsFragment)
            MainActivity.fragmentManager.popBackStackImmediate();
        }
    };

    //Ακροατής για το update button
    private View.OnClickListener updateProductClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            //Λήψη του καθαρού κειμένου των πεδίων
            String name = productName.getText().toString().trim();
            String price = productPrice.getText().toString();
            String quantity = productStock.getText().toString();
            //Έλεγχος για κενά πεδία και εμφάνιση κατάλληλου μηνύματος
            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || TextUtils.isEmpty(quantity)) {
                Toast.makeText(getActivity(), R.string.toast_fill, Toast.LENGTH_LONG).show();
                return;
            }
            //Δημιουργία νέου Product και πέρασμα τιμών
            Product product = new Product();
            product.setId(id);
            product.setName(productName.getText().toString());
            product.setPrice(Double.parseDouble(productPrice.getText().toString()));
            product.setStock(Integer.parseInt(productStock.getText().toString()));
            /*Update των παραμέτρων του προϊόντος με το αντίστοιχο id μέσω ερωτήματος στη ΒΔ
            και εμφάνιση κατάλληλου μηνύματος*/
            MainActivity.shoppingAppDb.shopAppDao().updateProduct(product);
            Toast.makeText(getActivity(), R.string.toast_upd_product, Toast.LENGTH_LONG).show();

            //Επιστροφή στο προηγούμενο fragment (ProductsFragment)
            MainActivity.fragmentManager.popBackStackImmediate();
        }
    };
}
