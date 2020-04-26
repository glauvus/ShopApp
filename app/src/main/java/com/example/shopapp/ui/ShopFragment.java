package com.example.shopapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ShopFragment extends Fragment {

    private List<Product> products = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductRecyclerAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    TextView emptyText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view;

        //Λήψη της λίστας με τα προϊόντα μέσω ερωτήματος στη ΒΔ
        products = MainActivity.shoppingAppDb.shopAppDao().getProducts();

        //Αν δεν υπάρχουν προϊόντα, φόρτωση διεπαφής με το κατάλληλο μήνυμα
        if (products.size()<1) {
            view = inflater.inflate(R.layout.fragment_empty, container, false);
            emptyText = view.findViewById(R.id.emptyText);
            emptyText.setText(R.string.empty_products);
            return view;
        }

        //Φόρτωση της διεπαφής του fragment
        view = inflater.inflate(R.layout.fragment_shop, container, false);
        //Φόρτωση στοιχείων της διεπαφής
        recyclerView = view.findViewById(R.id.shopRecyclerView);
        //Δημιουργία και φόρτωση του layout manager στο recycler view της διεπαφής
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //Δημιουργία και φόρτωση του adaptor στο recycler view της διεπαφής
        recyclerAdapter = new ProductRecyclerAdapter(products, "shop");
        recyclerView.setAdapter(recyclerAdapter);
        //Προσθήκη διαχωριστικής γραμμής στα αντικείμενα του recycler view
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));

        return view;
    }
}
