package com.example.shopapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shopapp.MainActivity;
import com.example.shopapp.R;
import com.example.shopapp.adapters.ProductRecyclerAdapter;
import com.example.shopapp.db.Product;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsFragment extends Fragment {

    Button addProduct;
    List<Product> products = new ArrayList<>();
    RecyclerView recyclerView;
    ProductRecyclerAdapter recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    TextView emptyText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view;

        //Λήψη της λίστας με τα προϊόντα μέσω ερωτήματος στη ΒΔ
        products = MainActivity.shoppingAppDb.shopAppDao().getProducts();

        //Αν δεν υπάρχουν προϊόντα, φόρτωση διεπαφής με το κατάλληλο μήνυμα
        if (products.size()<1) {
            view = inflater.inflate(R.layout.fragment_empty_products, container, false);
            addProduct = view.findViewById(R.id.addProduct);
            addProduct.setOnClickListener(addProductClickListener);
            emptyText = view.findViewById(R.id.emptyText);
            emptyText.setText(R.string.empty_products);
            return view;
        }

        //Φόρτωση της διεπαφής του fragment
        view = inflater.inflate(R.layout.fragment_products, container, false);

        //Φόρτωση στοιχείων της διεπαφής και ορισμός ακροατών
        addProduct = view.findViewById(R.id.addProduct);
        addProduct.setOnClickListener(addProductClickListener);
        recyclerView = view.findViewById(R.id.productRecyclerView);

        //Δημιουργία και φόρτωση του layout manager στο recycler view της διεπαφής
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //Δημιουργία και φόρτωση του adaptor στο recycler view της διεπαφής
        recyclerAdapter = new ProductRecyclerAdapter(products, "products");
        recyclerView.setAdapter(recyclerAdapter);
        //Προσθήκη διαχωριστικής γραμμής στα αντικείμενα του recycler view
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        return view;
    }

    private View.OnClickListener addProductClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Μετάβαση στο AddProductFragment
            MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,
                    new AddProductFragment()).addToBackStack(null).commit();
        }
    };
}
