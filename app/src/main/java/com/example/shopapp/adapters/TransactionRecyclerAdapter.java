package com.example.shopapp.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shopapp.MainActivity;
import com.example.shopapp.R;
import com.example.shopapp.db.Product;
import com.example.shopapp.db.Transaction;
import com.example.shopapp.ui.UpdDelTransactionFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionRecyclerAdapter extends RecyclerView.Adapter<TransactionRecyclerAdapter.TransactionViewHolder> {
    private List<Transaction> transactions;

    //Δημιουργία κλάσης για το view holder
    public class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView productName, productQuantity, productPrice;

        //Constructor του view holder με φόρτωση των στοιχείων της διεπαφής και ορισμό ακροατή
        public TransactionViewHolder(@NonNull View view) {
            super(view);
            productName = view.findViewById(R.id.productName);
            productPrice = view.findViewById(R.id.productPrice);
            productQuantity = view.findViewById(R.id.productQuantity);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Λήψη της θέσης στην οποία πυροδοτήθηκε το γεγονός
            Transaction transaction = transactions.get(getLayoutPosition());
            //Δημιουργία bundle και φόρτωμα παραμέτρων
            Bundle bundle = new Bundle();
            bundle.putInt("id", transaction.getId());
            bundle.putInt("quantity", transaction.getProductQuantity());
            bundle.putInt("productId", transaction.getProductId());

            //Δημιουργία στιγμιοτύπου UpDelTransactionFragment και πέρασμα παραμέτρων
            UpdDelTransactionFragment updDelTransactionFragment = new UpdDelTransactionFragment();
            updDelTransactionFragment.setArguments(bundle);

            //Μετάβαση στο UpDelTransactionFragment
            MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,
                    updDelTransactionFragment).addToBackStack(null).commit();
        }
    }

    //Constructor του TransactionRecyclerAdapter
    public TransactionRecyclerAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Φόρτωση της διεπαφής του recycler view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_cart,
                parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        //Λήψη της συναλλαγής με βάση τη θέση της στη λίστα
        Transaction transaction = transactions.get(position);
        //Φόρτωση στοιχείων της συναλλαγής στη διεπαφή
        Product product = MainActivity.shoppingAppDb.shopAppDao().getProduct(transaction.getProductId());
        holder.productName.setText(product.getName());
        holder.productQuantity.setText("x"+transaction.getProductQuantity());
        holder.productPrice.setText(String.valueOf(transaction.getProductQuantity()*product.getPrice()));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}
