package com.example.shopapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shopapp.MainActivity;
import com.example.shopapp.R;
import com.example.shopapp.adapters.TransactionRecyclerAdapter;
import com.example.shopapp.db.Product;
import com.example.shopapp.db.Transaction;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartFragment extends Fragment {

    private List<Transaction> transactions = new ArrayList<>();
    private RecyclerView recyclerView;
    private TransactionRecyclerAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView totalCost, emptyText;
    private Button submitCart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view;

        //Λήψη της λίστας με τις συναλλαγές που υπάρχουν στο καρότσι μέσω ερωτήματος στη ΒΔ
        transactions = MainActivity.shoppingAppDb.shopAppDao().getCart();

        //Αν δεν υπάρχουν συναλλαγές, φόρτωση διεπαφής με το κατάλληλο μήνυμα
        if (transactions.size()<1) {
            view = inflater.inflate(R.layout.fragment_empty, container, false);
            emptyText = view.findViewById(R.id.emptyText);
            emptyText.setText(R.string.empty_cart);
            return view;
        }

        //Φόρτωση της διεπαφής με τις συναλλαγές που υπάρχουν στο καρότσι
        view = inflater.inflate(R.layout.fragment_cart, container, false);

        //Φόρτωση στοιχείων της διεπαφής
        totalCost = view.findViewById(R.id.totalCost);
        submitCart = view.findViewById(R.id.submitCart);
        recyclerView = view.findViewById(R.id.cartRecyclerView);

        //Δημιουργία και φόρτωση του layout manager στο recycler view της διεπαφής
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //Δημιουργία και φόρτωση του adaptor στο recycler view της διεπαφής
        recyclerAdapter = new TransactionRecyclerAdapter(transactions);
        recyclerView.setAdapter(recyclerAdapter);

        //Προσθήκη διαχωριστικής γραμμής στα αντικείμενα του recycler view
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));

        /*Υπολογισμός του συνολικού κόστους μέσω ερωτήματος στη ΒΔ
        για την απόκτηση της τιμής και της ποσότητας κάθε προϊόντος*/
        double total=0;
        for(Transaction transaction : transactions) {
            Product product = MainActivity.shoppingAppDb.shopAppDao().getProduct(transaction.getProductId());
            total+=product.getPrice()*transaction.getProductQuantity();
        }
        //Φόρτωση του αποτελέσματος στη διεπαφή
        totalCost.setText(String.valueOf(total));

        //Ορισμός ακροατή για το submit button της διεπαφής
        submitCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Μετάβαση στο SubmitCartFragment
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new SubmitCartFragment()).addToBackStack(null).commit();
            }
        });

        return view;
    }
}
