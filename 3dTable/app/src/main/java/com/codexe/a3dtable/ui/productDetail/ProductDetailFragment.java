package com.codexe.a3dtable.ui.productDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.codexe.a3dtable.R;
import com.codexe.a3dtable.model.Product;
import com.codexe.a3dtable.ui.basket.BasketModelView;
import com.codexe.a3dtable.ui.products.SelectedProductVM;
import com.google.android.material.snackbar.Snackbar;

//Urun detayini gosteren fragment
public class ProductDetailFragment extends Fragment {

    private TextView txt_name, txt_price, txt_quantity, txt_notification;
    private Button btnAddToBasket;
    private SelectedProductVM selectedProductVM;
    private NavController navController;

    private void init(View v) {
        txt_name = v.findViewById(R.id.txtName);
        txt_price = v.findViewById(R.id.txtPrice);
        txt_quantity = v.findViewById(R.id.txtQuantity);
        btnAddToBasket = v.findViewById(R.id.btnAddToBasket);
    }


    public static ProductDetailFragment newInstance() {
        return new ProductDetailFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product_detail, container, false);
        init(root);
        registerHandlers();

        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        selectedProductVM = new ViewModelProvider(getActivity()).get(SelectedProductVM.class);

        selectedProductVM.getProduct().observe(getViewLifecycleOwner(), new Observer<Product>() {
            @Override
            public void onChanged(Product p) { // secilen urun guncellendigi zaman gerekli ui guncelleniyor
                txt_name.setText("Ürün Adı: " + p.getName());
                txt_price.setText(p.getPrice() + " ₺");
                txt_quantity.setText("Adet: " + p.getQuantity());

            }
        });

    }


    private void btnAddToBasket_Click() {
        btnAddToBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BasketModelView basketModelView = new ViewModelProvider(requireActivity()).get(BasketModelView.class); //
                SelectedProductVM productVM = new ViewModelProvider(requireActivity()).get(SelectedProductVM.class);

                Product p = productVM.getProduct().getValue(); //secilen urun alindi

                basketModelView.addProduct(p); // ve sepete eklendi

                Snackbar.make(v, getResources().getString(R.string.add_to_basket_msg), Snackbar.LENGTH_LONG).show();


            }
        });
    }


    private void registerHandlers() {
        btnAddToBasket_Click();
    }

}
